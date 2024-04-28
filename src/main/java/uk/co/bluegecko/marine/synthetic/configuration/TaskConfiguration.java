package uk.co.bluegecko.marine.synthetic.configuration;

import org.springframework.boot.task.ThreadPoolTaskExecutorCustomizer;
import org.springframework.boot.task.ThreadPoolTaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {

	@Bean
	public ThreadPoolTaskSchedulerCustomizer schedulerCustomizer() {
		return taskScheduler -> taskScheduler.setDaemon(true);
	}

	@Bean
	public ThreadPoolTaskExecutorCustomizer executorCustomizer() {
		return taskExecutor -> taskExecutor.setDaemon(true);
	}

}