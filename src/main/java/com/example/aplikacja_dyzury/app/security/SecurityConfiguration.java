package com.example.aplikacja_dyzury.app.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configures spring security, doing the following:
 * <li>Bypass security checks for static resources,</li>
 * <li>Restrict access to the application, allowing only logged in users,</li>
 * <li>Set up the login form</li>

 */
@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


	@Bean
	public PasswordEncoder passwordEncoder() {
		PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		return passwordEncoder;
	}

//	@Autowired
//	private UserTableRepo userTableRepo;

	private static final String LOGIN_PROCESSING_URL = "/login";
	private static final String LOGIN_FAILURE_URL = "/login?error";
	private static final String LOGIN_URL = "/loggedOutMainPage";
	private static final String LOGOUT_SUCCESS_URL = "/loggedOutMainPage";
	private static final String LOGIN_SUCCESS_URL = "/";

	/**
	 * Require login to access internal pages and configure login form.
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Not using Spring CSRF here to be able to use plain HTML for the login page
		http.csrf().disable()


				// Register our CustomRequestCache, that saves unauthorized access attempts, so
				// the user is redirected after login.
				.requestCache().requestCache(new CustomRequestCache())

				// Restrict access to our application.
				.and().authorizeRequests()
//				.antMatchers("/").permitAll()
				.antMatchers("/formUser").permitAll()
//				.antMatchers("/h2Console/**").permitAll()  //ta linia pozwala wejść do konsoli H2

				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

				// Allow all requests by logged in users.
				.anyRequest().authenticated()
				// Configure the login page.
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL).defaultSuccessUrl(LOGIN_SUCCESS_URL)


				// Configure logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL).permitAll();
		//rozwiązuje problemy z wyświetlaniem konsoli h2 database
//		http.headers().frameOptions().disable();
	}



	/**
	 * Allows access to static resources, bypassing Spring security.
	 */
	@Override
	public void configure(WebSecurity web) {
		web.ignoring().antMatchers(
				// Vaadin Flow static resources
				"/VAADIN/**",

				// the standard favicon URI
				"/favicon.ico",

				// the robots exclusion standard
				"/robots.txt",

				// web application manifest
				"/manifest.webmanifest",
				"/sw.js",
				"/offline-page.html",

				// icons and images
				"/icons/**",
				"/images/**",

				// (development mode) static resources
				"/frontend/**",

				// (development mode) webjars
				"/webjars/**",

				// (development mode) H2 debugging console
				"/h2-console/**",

				// (production mode) static resources
				"/frontend-es5/**", "/frontend-es6/**");
	}
}
