package com.example.finalprj.web.config;

import com.example.finalprj.db.service.UserSecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final UserSecurityService userSecurityService;

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSecurityService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER");  // ADMIN은 USER가 가진 모든 권한 사용 가능
        return roleHierarchy;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        final SpLoginFilter filter = new SpLoginFilter(
                authenticationManagerBean()
        );
        http
                .csrf().disable()
                .formLogin(login -> {
                    login
                            .loginPage("/login")
                    ;
                })
                .logout(logout -> {
                    logout.logoutSuccessUrl("/login")
                    ;
                })
                .addFilterAt(filter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(error ->
                        error.accessDeniedPage("/access-denied")
                )
                .authorizeRequests(config -> {
                    config
                            .antMatchers("/").permitAll()
                            .antMatchers("/login").permitAll()
                            .antMatchers("/error").permitAll()
                            .antMatchers("/signup").permitAll() // teacher, stu 회원가입 permit
                            .antMatchers("/api/**").permitAll()
                            //.antMatchers("/swagger-ui/").permitAll()  api쓰면 swagger 못쓰더라
                            .antMatchers("/admin/**").hasAnyAuthority("ROLE_ADMIN") //permitAll()
                            .antMatchers("/manager/**").hasAnyAuthority("ROLE_MANAGER")
                            .antMatchers("/user/**").hasAnyAuthority("ROLE_USER")
                            .anyRequest().authenticated()
                    ;
                })

        ;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

}
