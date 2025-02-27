package ru.alex.testcasebankapp.security.filter;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.security.web.context.RequestAttributeSecurityContextRepository;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.alex.testcasebankapp.model.entity.Logout;
import ru.alex.testcasebankapp.repository.LogoutRepository;
import ru.alex.testcasebankapp.security.authntication.TokenUser;


import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.util.Date;

public class JwtLogoutFilter extends OncePerRequestFilter {


    private RequestMatcher requestMatcher = new AntPathRequestMatcher("/v1/jwt/logout", HttpMethod.POST.name());

    private final SecurityContextRepository securityContextRepository = new RequestAttributeSecurityContextRepository();


    private final LogoutRepository logoutRepository;

    public JwtLogoutFilter(LogoutRepository logoutRepository) {
        this.logoutRepository = logoutRepository;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (this.requestMatcher.matches(request)) {
            if (this.securityContextRepository.containsContext(request)) {
                var context = this.securityContextRepository.loadDeferredContext(request).get();
                if (context != null && context.getAuthentication() instanceof PreAuthenticatedAuthenticationToken &&
                        context.getAuthentication().getPrincipal() instanceof TokenUser user &&
                        context.getAuthentication().getAuthorities().contains(new SimpleGrantedAuthority("JWT_LOGOUT"))) {
                    logoutRepository.save(new Logout(user.getToken().id(), Date.from(user.getToken().expireAt())));

                    response.setStatus(HttpStatus.NO_CONTENT.value());

                    return;
                }
            }
            throw new AccessDeniedException("User must be auth with JWT");
        }
        filterChain.doFilter(request, response);
    }
}
