package ru.alex.testcasebankapp.config;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.KeyLengthException;
import com.nimbusds.jose.crypto.DirectDecrypter;
import com.nimbusds.jose.crypto.DirectEncrypter;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.jwk.OctetSequenceKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.alex.testcasebankapp.repository.LogoutRepository;
import ru.alex.testcasebankapp.security.configurer.RequestConfigurer;
import ru.alex.testcasebankapp.security.jwt.deserializer.AccessTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.deserializer.DefaultAccessTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.deserializer.DefaultRefreshTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.deserializer.RefreshTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.serializer.AccessTokenJwsStringSerializer;
import ru.alex.testcasebankapp.security.jwt.serializer.DefaultAccessTokenJwsStringSerializer;
import ru.alex.testcasebankapp.security.jwt.serializer.DefaultRefreshTokenJweStringSerializer;
import ru.alex.testcasebankapp.security.jwt.serializer.RefreshTokenJweStringSerializer;


import java.text.ParseException;

@Slf4j
@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Value("${jwt.secret.access}")
    private String accessToken;
    @Value("${jwt.secret.refresh}")
    private String refreshToken;

    @Bean
    public RequestConfigurer requestConfigurer(LogoutRepository logoutRepository) throws JOSEException, ParseException {
        return new RequestConfigurer()
                .accessTokenJwsStringSerializer(
                        accessTokenJwsStringSerializer()
                )
                .refreshTokenJweStringSerializer(
                        refreshTokenJweStringSerializer()
                )
                .accessTokenJwsStringDeserializer(
                        accessTokenJwsStringDeserializer()
                )
                .refreshTokenJwsStringDeserializer(
                        refreshTokenJwsStringDeserializer()
                )
                .logoutRepository(logoutRepository);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, RequestConfigurer requestConfigurer) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.cors(Customizer.withDefaults());
        http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->
                authorizationManagerRequestMatcherRegistry
                        .requestMatchers("/v1/auth/**", "/v1/admin/**", "/swagger-doc/**")
                        .permitAll()
                        .anyRequest()
                        .hasRole("USER")
        );

        http.sessionManagement(httpSecuritySessionManagementConfigurer ->
                httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.apply(requestConfigurer);

        log.info("configure {} successful", this.getClass());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AccessTokenJwsStringSerializer accessTokenJwsStringSerializer() throws ParseException, KeyLengthException {
        return new DefaultAccessTokenJwsStringSerializer(
                new MACSigner(OctetSequenceKey.parse(accessToken)));
    }

    @Bean
    public RefreshTokenJweStringSerializer refreshTokenJweStringSerializer() throws ParseException, KeyLengthException {
        return new DefaultRefreshTokenJweStringSerializer(
                new DirectEncrypter(OctetSequenceKey.parse(refreshToken)));
    }
    @Bean
    public AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer() throws ParseException, JOSEException {
        return new DefaultAccessTokenJwsStringDeserializer(
                new MACVerifier(OctetSequenceKey.parse(accessToken)));
    }
    @Bean
    public RefreshTokenJwsStringDeserializer refreshTokenJwsStringDeserializer() throws ParseException, KeyLengthException {
        return new DefaultRefreshTokenJwsStringDeserializer(
                new DirectDecrypter(OctetSequenceKey.parse(refreshToken)));
    }
}
