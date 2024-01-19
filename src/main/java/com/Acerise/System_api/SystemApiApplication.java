package com.Acerise.System_api;

import com.Acerise.System_api.Config.Security.RsaKeyProp;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.mapping.event.ValidatingMongoEventListener;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties(RsaKeyProp.class)
//@EnableCaching
public class SystemApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(SystemApiApplication.class, args);
	}


}
