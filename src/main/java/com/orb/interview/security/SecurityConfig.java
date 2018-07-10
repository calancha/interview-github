package com.orb.interview.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Configuration class for security-related beans.
 */
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
  @Autowired
  private AuthenticatedUserProvider authenticatedUserProvider;

  @Autowired
  private AuthenticationTokenFilter authenticationTokenFilter;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    // Disable CSRF
    http.csrf().disable();

    // Disable JSESSION cookie
    http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

    http.cors()
            .and()
            .authorizeRequests()
            .antMatchers("/users").permitAll()
            .antMatchers("/users/login").permitAll()
            .antMatchers("/**/*").authenticated()
            .and()
            .addFilterAfter(authenticationTokenFilter, BasicAuthenticationFilter.class);
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticatedUserProvider);
  }
}