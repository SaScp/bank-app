package ru.alex.testcasebankapp.security.configurer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.AuthenticationEntryPointFailureHandler;
import org.springframework.security.web.authentication.AuthenticationFilter;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationProvider;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.session.DisableEncodeUrlFilter;
import org.springframework.stereotype.Component;
import ru.alex.testcasebankapp.repository.LogoutRepository;
import ru.alex.testcasebankapp.security.authntication.JwtAuthenticationConverter;
import ru.alex.testcasebankapp.security.authntication.TokenAuthUserDetailsService;
import ru.alex.testcasebankapp.security.filter.DeniedRequestFilter;
import ru.alex.testcasebankapp.security.filter.JwtLogoutFilter;
import ru.alex.testcasebankapp.security.filter.RefreshTokenFilter;
import ru.alex.testcasebankapp.security.jwt.deserializer.AccessTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.deserializer.RefreshTokenJwsStringDeserializer;
import ru.alex.testcasebankapp.security.jwt.serializer.AccessTokenJwsStringSerializer;
import ru.alex.testcasebankapp.security.jwt.serializer.RefreshTokenJweStringSerializer;


@Slf4j
@Component
@RequiredArgsConstructor
public class RequestConfigurer extends AbstractHttpConfigurer<RequestConfigurer, HttpSecurity> {


    private AccessTokenJwsStringSerializer accessTokenJwsStringSerializer;

    private RefreshTokenJweStringSerializer refreshTokenJweStringSerializer;

    private RefreshTokenJwsStringDeserializer refreshTokenJwsStringDeserializer;

    private AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer;

    private LogoutRepository logoutRepository;
    @Override
    public void init(HttpSecurity builder) throws Exception {
        super.init(builder);
    }

    @Override
    public void configure(HttpSecurity builder) throws Exception {
        AuthenticationManager authenticationManager = builder.getSharedObject(AuthenticationManager.class);

        AuthenticationFilter authenticationFilter = getAuthenticationFilter(authenticationManager);
        PreAuthenticatedAuthenticationProvider authenticationProvider = new PreAuthenticatedAuthenticationProvider();
        authenticationProvider.setPreAuthenticatedUserDetailsService(new TokenAuthUserDetailsService(this.logoutRepository));

        RefreshTokenFilter refreshTokenFilter = getRefreshTokenFilter(accessTokenJwsStringSerializer, refreshTokenJweStringSerializer);
        JwtLogoutFilter jwtLogoutFilter =  new JwtLogoutFilter(this.logoutRepository);
        DeniedRequestFilter deniedRequestFilter = new DeniedRequestFilter();

        builder.addFilterBefore(authenticationFilter, CsrfFilter.class)
                .addFilterBefore(refreshTokenFilter, ExceptionTranslationFilter.class)
                .addFilterAfter(jwtLogoutFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(deniedRequestFilter, DisableEncodeUrlFilter.class)
                .authenticationProvider(authenticationProvider);

        log.info("configure {} successful", this.getClass());
    }

    private RefreshTokenFilter getRefreshTokenFilter(AccessTokenJwsStringSerializer accessTokenJwsStringSerializer,
                                                     RefreshTokenJweStringSerializer refreshTokenJweStringSerializer) {
        RefreshTokenFilter refreshTokenFilter = new RefreshTokenFilter();
        refreshTokenFilter.setAccessTokenJwsStringSerializer(accessTokenJwsStringSerializer);
        refreshTokenFilter.setRefreshTokenJweStringSerializer(refreshTokenJweStringSerializer);
        return refreshTokenFilter;
    }

    private AuthenticationFilter getAuthenticationFilter(AuthenticationManager authenticationManager) {
        AuthenticationFilter authenticationFilter = new AuthenticationFilter(authenticationManager,
                new JwtAuthenticationConverter(refreshTokenJwsStringDeserializer, accessTokenJwsStringDeserializer));
        authenticationFilter.setSuccessHandler((request, response, authentication) -> CsrfFilter.skipRequest(request));
        authenticationFilter.setFailureHandler(new AuthenticationEntryPointFailureHandler(((request, response, authException) -> response.sendError(HttpStatus.FORBIDDEN.value()))));
        return authenticationFilter;
    }

    public RequestConfigurer accessTokenJwsStringSerializer(AccessTokenJwsStringSerializer accessTokenJwsStringSerializer) {
        this.accessTokenJwsStringSerializer = accessTokenJwsStringSerializer;
        return this;
    }

    public RequestConfigurer refreshTokenJweStringSerializer(RefreshTokenJweStringSerializer refreshTokenJweStringSerializer) {
        this.refreshTokenJweStringSerializer = refreshTokenJweStringSerializer;
        return this;
    }

    public RequestConfigurer refreshTokenJwsStringDeserializer(RefreshTokenJwsStringDeserializer refreshTokenJwsStringDeserializer) {
        this.refreshTokenJwsStringDeserializer = refreshTokenJwsStringDeserializer;
        return this;
    }

    public RequestConfigurer accessTokenJwsStringDeserializer(AccessTokenJwsStringDeserializer accessTokenJwsStringDeserializer) {
        this.accessTokenJwsStringDeserializer = accessTokenJwsStringDeserializer;
        return this;
    }

    public RequestConfigurer logoutRepository(LogoutRepository logoutRepository) {
        this.logoutRepository = logoutRepository;
        return this;
    }
}
