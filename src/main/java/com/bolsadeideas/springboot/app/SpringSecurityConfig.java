package com.bolsadeideas.springboot.app;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter.Directive;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

import com.bolsadeideas.springboot.app.auth.handler.LoginSuccesHandler;
import com.bolsadeideas.springboot.app.models.services.JpaUserDetailsService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class SpringSecurityConfig {

    @Autowired
    private LoginSuccesHandler succesHandler;

    @Autowired
    private JpaUserDetailsService userDetailsService;

    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, MvcRequestMatcher.Builder mvc) throws Exception {
        http.authorizeHttpRequests((authz) -> {
            try {
                authz.requestMatchers(mvc.pattern("/"),
                        mvc.pattern("/css/**"),
                        mvc.pattern("/js/**"),
                        mvc.pattern("/images/**"),
                        mvc.pattern("/listar**"),
                        mvc.pattern("/locale"),
                        mvc.pattern("/api/clientes/**")).permitAll()
                        // .requestMatchers(mvc.pattern("/uploads/**")).hasAnyRole("USER")
                        // .requestMatchers(mvc.pattern("/ver/**")).hasRole("USER")
                        // .requestMatchers(mvc.pattern("/factura/**")).hasRole("ADMIN")
                        // .requestMatchers(mvc.pattern("/form/**")).hasRole("ADMIN")
                        // .requestMatchers(mvc.pattern("/eliminar/**")).hasRole("ADMIN")
                        .anyRequest().authenticated();
                // .and()
                // .formLogin(formLogin -> formLogin.permitAll()).logout
                // (logout -> logout.permitAll());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).formLogin(
                formLogin -> formLogin
                        .successHandler(succesHandler)
                        .loginPage("/login") // Especifica la página de inicio de sesión personalizada
                        .permitAll())
                .logout(logout -> logout.addLogoutHandler(
                        new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(Directive.COOKIES))))
                .exceptionHandling(
                        exceptionHandling -> exceptionHandling
                                .accessDeniedHandler(accessDeniedHandler()) // Configura el manejador de acceso denegado
                );

        return http.build();

    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new AccessDeniedHandler() {
            @Override
            public void handle(HttpServletRequest request, HttpServletResponse response,
                    AccessDeniedException accessDeniedException) throws IOException, ServletException {
                // Personaliza el manejo del acceso denegado aquí
                response.sendRedirect("/error_403"); // Redirige a tu página de acceso denegado personalizada
            }
        };
    }

    @Bean
    AuthenticationManager authManager(HttpSecurity http) throws Exception {

        AuthenticationManagerBuilder authBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);

        authBuilder.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

        return authBuilder.build();
    }

    @Bean
    public static BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}