package com.eden.hackerdesktopapi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@Slf4j
@EnableScheduling
public class HackerDesktopApiApplication {

	public static void main(String[] args) throws UnknownHostException {
		// Start application and get ip/port from environment
		ConfigurableApplicationContext application = SpringApplication.run(HackerDesktopApiApplication.class, args);
		Environment env = application.getEnvironment();
		String ip = InetAddress.getLocalHost().getHostAddress();
		String port = env.getProperty("server.port");

		// Log out the url
		log.info("\n----------------------------------------------------------\n\t" +
				"Application hacker-desktop is running! Access URLs:\n\t" +
				"Local: \t\thttp://localhost:" + port  + "/\n\t" +
				"External: \thttp://" + ip + ":" + port  + "/\n\t" +
				"\n----------------------------------------------------------");
	}

}
