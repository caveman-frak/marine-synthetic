package uk.co.bluegecko.marine.synthetic;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.boot.Banner.Mode;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.shell.command.annotation.CommandScan;
import org.springframework.shell.jline.PromptProvider;

@SpringBootApplication(exclude = {
		DataSourceAutoConfiguration.class,
		DataSourceTransactionManagerAutoConfiguration.class,
		HibernateJpaAutoConfiguration.class
})
@CommandScan
@ComponentScan
public class SyntheticApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(SyntheticApplication.class)
				.registerShutdownHook(true)
				.setAddConversionService(true)
				.web(WebApplicationType.NONE)
				.bannerMode(Mode.OFF)
				.run(args);
	}

	@Bean
	public PromptProvider promptProvider() {
		return () -> new AttributedString("synth:>",
				AttributedStyle.DEFAULT.foreground(AttributedStyle.CYAN));
	}

}