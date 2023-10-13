package com.br.authorizationserver.core.security;

import java.time.OffsetDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.br.authorizationserver.entity.userEntity.UserEntity;
import com.br.authorizationserver.service.userEntity.UserEntityRepository;


@Component
public class FirstUserConfig implements ApplicationRunner {

	private final Logger logger = LoggerFactory.getLogger(FirstUserConfig.class);
	private final UserEntityRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	public FirstUserConfig(UserEntityRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(ApplicationArguments args) throws Exception {
		if (userRepository.count() != 0) {
			return;
		}

		logger.info("Nenhum usuário encontrado, cadastrando usuários padrão.");

		userRepository.save(
            UserEntity.builder()
                .fullname("Henrique Dias")
                .username("admin")
                .email("admin@email.com")
                .password(passwordEncoder.encode("sucesso"))
                .scopes(EAuthorites.ADMIN.toString())
                .createdAt(OffsetDateTime.now())
                .build()
		);

		userRepository.save(
            UserEntity.builder()
                .fullname("Henrique Dias")
                .username("henrique")
                .email("henrique@email.com")
                .password(passwordEncoder.encode("sucesso"))
                .scopes(EAuthorites.USER.toString())
                .createdAt(OffsetDateTime.now())
                .build()
		);
	}
}