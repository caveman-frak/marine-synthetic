package uk.co.bluegecko.marine.synthetic.configuration;

import org.springframework.boot.task.TaskExecutorCustomizer;
import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskConfiguration {

	@Bean
	public TaskSchedulerCustomizer taskSchedulerCustomizer() {
		return taskScheduler -> taskScheduler.setDaemon(true);
	}

	@Bean
	public TaskExecutorCustomizer taskExecutorCustomizer() {
		return taskExecutor -> taskExecutor.setDaemon(true);
	}

}