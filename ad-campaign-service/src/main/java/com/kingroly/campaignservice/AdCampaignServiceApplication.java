package com.kingroly.campaignservice;

import static org.springframework.boot.SpringApplication.run;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class AdCampaignServiceApplication {

	public static void main(String[] args) {
		run(AdCampaignServiceApplication.class, args);
	}

}
