/**
 * Copyright (c) 2020 Grupo TMM; All rights reserved. This software contains confidential information
 * owned by the Grupo TMM Corp and therefore can not be reproduced, distributed or altered without the prior
 * consent of the Grupo TMM Corp
 */
package com.tmm.myre.base.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;

import com.tmm.myre.base.service.UserDetailService;




/**
 * @author Josue Moreno (Grupo TMM - Desarrollo de Software)
 * @version 1.0 Date Creation: 12 abr. 2021
 * 
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	@Autowired
    private UserDetailService userDetailService;
 
 
	@Autowired
    public BCryptPasswordEncoder passwordEncoder() {
        BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
        return bCryptPasswordEncoder;
    }
 
    public SpringSecurityDialect springSecurityDialect(){
        return new SpringSecurityDialect();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailService).passwordEncoder(passwordEncoder());
    }
 
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.csrf().disable();
		
		http.authorizeRequests().antMatchers("/login", "/logout").permitAll();
		
		http.authorizeRequests().antMatchers("/login/register").permitAll();
 
        http.authorizeRequests()
    	.antMatchers(
            "/error/**",
    		"/js/**",
            "/css/**",
            "/images/**").permitAll()
    	.antMatchers("/administration/**").hasAnyRole("ADMIN", "MASTER")
    	.anyRequest().authenticated();

    // Configuracion del login
        http.authorizeRequests()
    		.and().formLogin()//
            // Submit URL of login page.
            	.loginProcessingUrl("/j_spring_security_check") // Submit URL
            	.loginPage("/login")//
            	.defaultSuccessUrl("/index")//
            	.failureUrl("/login?error=true")//
            	.usernameParameter("username")//
            	.passwordParameter("password")
            	.permitAll()
            // Config for Logout Page
            .and().logout()
            	.invalidateHttpSession(true)
            	.clearAuthentication(true)
            	.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            	.logoutSuccessUrl("/login?logout")
            	.permitAll();
            
 
    }
    
}