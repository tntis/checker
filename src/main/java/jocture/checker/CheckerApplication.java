package jocture.checker;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CheckerApplication {

	public static void main(String[] args) {
		System.exit(SpringApplication.exit(
				SpringApplication.run(CheckerApplication.class, args)));
	}

}
