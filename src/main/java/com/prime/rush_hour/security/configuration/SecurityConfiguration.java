package com.prime.rush_hour.security.configuration;

import com.prime.rush_hour.jwt.JwtAuthenticationFilter;
import com.prime.rush_hour.jwt.JwtConfig;
import com.prime.rush_hour.jwt.JwtTokenVerifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.crypto.SecretKey;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity (prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final SecretKey secretKey;
    private final JwtConfig jwtConfig;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfiguration( SecretKey secretKey, JwtConfig jwtConfig, UserDetailsService userDetailsService){
        this.secretKey = secretKey;
        this.jwtConfig = jwtConfig;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilter(getJwtAuthenticationFilter())
                .addFilterAfter(new JwtTokenVerifier(secretKey, jwtConfig), JwtAuthenticationFilter.class)
                .authorizeRequests()
                .antMatchers( "/login", "/api/v1/users/register").permitAll()
                .anyRequest()
                .authenticated();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(getPasswordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }

    private JwtAuthenticationFilter getJwtAuthenticationFilter() throws Exception {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authenticationManager(), jwtConfig , secretKey);
        filter.setFilterProcessesUrl("/api/v1/login");
        return filter;
    }

    @Bean
    public PasswordEncoder getPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
