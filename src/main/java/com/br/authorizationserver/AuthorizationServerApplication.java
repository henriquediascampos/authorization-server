package com.br.authorizationserver;

import com.br.authorizationserver.core.banner.CustomBanner;

import org.springframework.boot.Banner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class AuthorizationServerApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AuthorizationServerApplication.class)
			.banner(new CustomBanner())
			.bannerMode(Banner.Mode.CONSOLE)
			.logStartupInfo(true)
			.run(args);
	}


	@Bean
	public PasswordEncoder getPasswordEncoder() {
		final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}
