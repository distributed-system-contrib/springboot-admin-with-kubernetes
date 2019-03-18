package io.github.hamelmoon;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class SpringbootAdminWithKubernetesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootAdminWithKubernetesApplication.class, args);
	}

}
