package com.br.authorizationserver.core.security;

import static org.springframework.security.config.Customizer.withDefaults;

import java.io.InputStream;
import java.security.KeyStore;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.ClassPathResource;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.ClientSettings;
import org.springframework.security.oauth2.server.authorization.config.ProviderSettings;
import org.springframework.security.oauth2.server.authorization.config.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.br.authorizationserver.service.userEntity.UserEntityRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public SecurityFilterChain defaultFilterChain(HttpSecurity http)
            throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);
        return http
                .formLogin(withDefaults())
                .build();
    }

    @Bean
    public SecurityFilterChain authFilterChain(HttpSecurity http)
            throws Exception {

        final var matchers = new ArrayList<String>();
        matchers.add("/h2-console/**");
        matchers.add("/assets/**");
        matchers.add("/js/**");
        matchers.add("/styles/**");
        matchers.add("/images/**");

        matchers.add("/sign-up");
        matchers.add("/forgot-password");
        matchers.add("/sign-up");

        http
                    .csrf().disable()
                    .authorizeRequests()
                    // .antMatchers("", "/oauth2/**", "/styles/**", "/js/**", "/images/**", "/forgot-password", "/patos")
                    .antMatchers(matchers.toArray(String[]::new))
                    .permitAll()
                .and()
                    .authorizeRequests()
                    .antMatchers("/user/**").hasAuthority("ADMIN")
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                .and()
                    .oauth2ResourceServer()
                    .jwt()
                    .jwtAuthenticationConverter(jwtAuthenticationConverter())
                .and().and()
                    .formLogin( a -> {
                        a.loginPage("/login").permitAll();

                    })
                        // .loginPage("/login")

                    // .formLogin(withDefaults())
                    // .build()
                    ;

        return http
        //         .formLogin(withDefaults())
                .build();
    }

    private JwtAuthenticationConverter jwtAuthenticationConverter() {
        var jwtAthConverter = new JwtAuthenticationConverter();
        jwtAthConverter.setJwtGrantedAuthoritiesConverter(
                jwt -> {
                    List<String> roleAuthority = jwt.getClaimAsStringList("authorites");
                    if (Objects.isNull(roleAuthority)) {
                        return Collections.emptyList();
                    }

                    JwtGrantedAuthoritiesConverter scopes = new JwtGrantedAuthoritiesConverter();

                    Set<GrantedAuthority> scopesAuthority = scopes.convert(jwt).stream().collect(Collectors.toSet());
                    scopesAuthority.addAll(
                            roleAuthority.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
                    return scopesAuthority;
                });

        return jwtAthConverter;
    }

    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> oAuth2TokenCustomizer(UserEntityRepository userRepository) {
        return (context -> {
            Authentication authentication = context.getPrincipal();
            if (authentication.getPrincipal() instanceof UserDetails) {
                final var user = UserDetails.class.cast(authentication.getPrincipal());

                final var userEntity = userRepository.findByEmail(user.getUsername()).orElseThrow();

                Set<String> authorites = new HashSet<>();
                for (GrantedAuthority authority : user.getAuthorities()) {
                    authorites.add(authority.toString());
                }

                context.getClaims()
                        .claim("user_id", userEntity.getId().toString())
                        .claim("user_fullname", userEntity.getUsername())
                        .claim("authorites", authorites);
            }
        });
    }

    @Bean
    public RegisteredClientRepository registeredClientRepository(PasswordEncoder passwordEncoder) {
        RegisteredClient firstClient = RegisteredClient
                .withId("1")
                .clientId("clientid")
                .clientSecret(passwordEncoder.encode("clientsecret"))
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                // .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:3000/authorized")
                .redirectUri("https://oidcdebugger.com/debug")
                .redirectUri("https://oauth.pstmn.io/v1/callback")
                .redirectUri("http://localhost:8080/login/oauth2/code/login-client-authorization-code")
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/login-client-authorization-code")
                .scope(EAuthorites.ADMIN.toString())
                .scope(EAuthorites.USER.toString())
                .scope(OidcScopes.OPENID)
                .tokenSettings(TokenSettings.builder()
                        .accessTokenTimeToLive(Duration.ofMinutes(30))
                        .refreshTokenTimeToLive(Duration.ofDays(1))
                        .reuseRefreshTokens(false)
                        .build())
                .clientSettings(ClientSettings.builder()
                        .requireAuthorizationConsent(false)
                        .build())
                .build();

        return new InMemoryRegisteredClientRepository(Arrays.asList(firstClient));
    }

    @Bean
    public ProviderSettings providerSettings(AuthProperties authProperties) {
        return ProviderSettings.builder()
                .issuer(authProperties.getProviderUri())
                .build();
    }

    @Bean
    public JWKSet jwkSet(AuthProperties authProperties) throws Exception {
        final var jksProperties = authProperties.getJks();
        final String jksPath = jksProperties.getPath();
        final InputStream inputStream = new ClassPathResource(jksPath).getInputStream();

        final KeyStore keyStore = KeyStore.getInstance("JKS");
        keyStore.load(inputStream, jksProperties.getStorepass().toCharArray());

        RSAKey rsaKey = RSAKey.load(keyStore,
                jksProperties.getAlias(),
                jksProperties.getKeypass().toCharArray());

        return new JWKSet(rsaKey);
    }

    @Bean
    public JWKSource<SecurityContext> jwkSource(JWKSet jwkSet) {
        return ((jwkSelector, securityContext) -> jwkSelector.select(jwkSet));
    }

    @Bean
    public JwtEncoder jwtEncoder(JWKSource<SecurityContext> jwkSource) {
        return new NimbusJwtEncoder(jwkSource);
    }
}