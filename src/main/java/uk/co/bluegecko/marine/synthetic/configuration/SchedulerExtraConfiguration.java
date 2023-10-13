package uk.co.bluegecko.marine.synthetic.configuration;

import org.springframework.boot.task.TaskSchedulerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchedulerExtraConfiguration {

	@Bean
	public TaskSchedulerCustomizer taskSchedulerCustomizer() {
		return taskScheduler -> taskScheduler.setDaemon(true);
	}

}