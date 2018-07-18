package io.github.gabriel.alex;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;




/**
 * Main controller of the web site
 * 
 * @author Alex Gabriel
 * @version 1.0
 * @since 1.0
 */

@EnableAutoConfiguration
@SpringBootApplication
public class Main {

	

	public static void main(String[] args) throws Exception {
		SpringApplication.run(Main.class, args);

	}


}
