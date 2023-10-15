package uk.co.bluegecko.marine.synthetic.ais.raw;

import com.thedeanda.lorem.Lorem;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.concurrent.ScheduledFuture;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ExecutorConfigurationSupport;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.shell.command.annotation.Command;
import org.springframework.shell.command.annotation.Option;
import org.springframework.shell.context.InteractionMode;
import org.springframework.stereotype.Component;
import uk.co.bluegecko.marine.shared.configuration.SchedulerConfiguration;

@Command
@Component
@Value
@Slf4j
@Import({SchedulerConfiguration.class})
public class AisFeed {

	TaskScheduler scheduler;
	TaskExecutor executor;
	Lorem lorem;

	@Command(description = "Publish synthetic AIS feed.")
	public void publish(
			@Option(shortNames = 'p', defaultValue = "10000", description = "Port to publish feed on.") int port,
			@Option(shortNames = 'd', defaultValue = "1000", description = "Delay between messages in ms.") long delay) {
		startServer(port, Duration.ofMillis(delay));
	}

	@Command(description = "Stop publishing feed", interactionMode = InteractionMode.INTERACTIVE)
	public void stop() {
		if (isRunning()) {
			log.info("Stopping publisher");
			if (getScheduler() instanceof ExecutorConfigurationSupport executor) {
				executor.shutdown();
			}
			if (getExecutor() instanceof ExecutorConfigurationSupport executor) {
				executor.shutdown();
			}
		}
	}

	private void startServer(int port, Duration delay) {
		if (isRunning()) {
			getExecutor().execute(() -> {
				try {
					SelectorProvider provider = SelectorProvider.provider();
					Selector selector = provider.openSelector();
					try (ServerSocketChannel server = provider.openServerSocketChannel()) {
						server.bind(new InetSocketAddress("127.0.0.1", port));
						server.configureBlocking(false);
						server.register(selector, SelectionKey.OP_ACCEPT);
						log.info("Publishing AIS Feed on port {} every {} ns", port, delay);
						while (isRunning()) {
							selector.select(key -> {
								if (key.isAcceptable()) {
									startPublishing(selector, server, delay);
								}
							});
						}
					}
				} catch (IOException ex) {
					log.error("Unable to create server on port {} due to {}", port, ex.getMessage());
				}
			});
		} else {
			log.info("Publisher already stopped");
		}
	}

	private void startPublishing(Selector selector, ServerSocketChannel server, Duration delay) {
		log.info("Incoming connection...");
		try {
			SocketChannel client = server.accept();
			log.info("Connected to {} :: {}", client.getLocalAddress(), client.getRemoteAddress());
			client.configureBlocking(false);
			SelectionKey key = client.register(selector, SelectionKey.OP_WRITE);
			ScheduledFuture<?> future = getScheduler().scheduleAtFixedRate(() -> writeMessage(key), delay);
			key.attach(future);
		} catch (IOException ex) {
			log.error("Unable to connect to client: {}", ex.getMessage());
		}
	}

	private void writeMessage(SelectionKey key) {
		try {
			if (key.isWritable()) {
				ByteBuffer buffer = StandardCharsets.UTF_8.encode(generateMessage());
				((WritableByteChannel) key.channel()).write(buffer);
			}
		} catch (IOException ex) {
			log.debug("Client disconnected, stopping writing.");
			((ScheduledFuture<?>) key.attachment()).cancel(true);
			try {
				key.channel().close();
			} catch (IOException e) {
				log.error("Error while closing channel: {}", e.getMessage());
			}
		}
	}

	private String generateMessage() {
		return lorem.getWords(5, 9).replace(' ', '_') + "\n";
	}

	private boolean isRunning() {
		if (scheduler instanceof ThreadPoolTaskScheduler poolTaskScheduler) {
			return !poolTaskScheduler.getScheduledExecutor().isShutdown();
		}
		return true;
	}

	@Bean
	public ApplicationListener<ContextClosedEvent> registerPublishMonitor() {
		return e -> stop();
	}

}