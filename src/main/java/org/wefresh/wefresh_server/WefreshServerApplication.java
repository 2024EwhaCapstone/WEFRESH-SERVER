package org.wefresh.wefresh_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class WefreshServerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WefreshServerApplication.class, args);
	}

}
