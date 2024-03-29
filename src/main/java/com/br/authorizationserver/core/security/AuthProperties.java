package com.br.authorizationserver.core.security;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@ConfigurationProperties("auth")
@Getter
@Setter
public class AuthProperties {

    @NotBlank
    private String providerUri;
    @NotNull
    private JksProperties jks;

    @Getter
    @Setter
    static class JksProperties {
        @NotBlank
        private String keypass;
        @NotBlank
        private String storepass;
        @NotBlank
        private String alias;
        @NotBlank
        private String path;
    }
}
