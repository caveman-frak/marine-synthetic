package uk.co.bluegecko.marine.synthetic.configuration;

import com.thedeanda.lorem.Lorem;
import com.thedeanda.lorem.LoremIpsum;
import java.util.Random;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SytheticDataConfiguration {

	@Bean
	public Random random() {
		return new Random();
	}

	@Bean
	public Lorem loremIpsum(Random random) {
		return new LoremIpsum(random);
	}

}