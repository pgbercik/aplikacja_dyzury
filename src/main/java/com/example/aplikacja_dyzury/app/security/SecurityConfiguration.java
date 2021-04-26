package com.example.aplikacja_dyzury.app.security;

//import com.example.aplikacja_dyzury.DataModelAndRepo.stare_nieuzywane_teraz.UserTableRepo;
//import org.springframework.beans.factory.annotation.Autowired;
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
				// Allow all flow internal requests.
				.requestMatchers(SecurityUtils::isFrameworkInternalRequest).permitAll()

				// Allow all requests by logged in users.
				.anyRequest().authenticated()
				// Configure the login page.
				.and().formLogin().loginPage(LOGIN_URL).permitAll().loginProcessingUrl(LOGIN_PROCESSING_URL)
				.failureUrl(LOGIN_FAILURE_URL).defaultSuccessUrl(LOGIN_SUCCESS_URL)


				// Configure logout
				.and().logout().logoutSuccessUrl(LOGOUT_SUCCESS_URL).permitAll();
	}

//	@Autowired
//	private MyUserDetailsService myUserDetailsService;

//	szyfrowane logowanie in memory przez bcrypt
//	@Override
//	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		String password = passwordEncoder().encode("pass1");
//		auth.inMemoryAuthentication().withUser("admin1").password(password).roles("Admin");
//
////		auth.userDetailsService(myUserDetailsService).passwordEncoder(passwordEncoder());
//
//
//////		String password2 = passwordEncoder().encode("jones");
//////		auth.inMemoryAuthentication().withUser("stachu").password(password2).roles("User");
////
////
////
//		List<UserTable> users = userTableRepo.findAll();
////
////
////
//		for (UserTable userTable : users) {
//			String password1 = passwordEncoder().encode(userTable.getPassword());
//			auth.inMemoryAuthentication().withUser(userTable.getUsername()).password(userTable.getPassword()).roles(userTable.getRole());
//			String aaa = "jones";
//			System.out.println(passwordEncoder().matches(aaa,userTable.getPassword()) +"WYNIK");
//			System.out.println(userTable.toString()+"ABCDE");
//		}
//
//
//
//	}
//




//	@Bean
//	@Override
//	public UserDetailsService userDetailsService() {
////		UserDetails normalUser =
////				User.withUsername("user")
////						.password("{noop}password")
////						.roles("User")
////						.build();
////
////		UserDetails normalUser2 =
////				User.withUsername("john")
////						.password("{noop}doe")
////						.roles("User")
////						.build();
////
////		// admin user with all privileges
////		UserDetails adminUser =
////				User.withUsername("admin")
////						.password("{noop}password")
////						.roles("Admin")
////						.build();
//
//		List<UserDetails> userList = new ArrayList<>();
////
//		List<UserTable> users = userTableRepo.findAll();
//		List<GrantedAuthority> uprawnienia = Arrays.asList(
//				new SimpleGrantedAuthority("ROLE_User"),
//				new SimpleGrantedAuthority("ROLE_Admin"));
//
//
//		InMemoryUserDetailsManager inMemoryUserDetailsManager = new InMemoryUserDetailsManager();
//		for (UserTable userTable : users) {
////		userList.add(new User(userTable.getUsername(),userTable.getPassword(),uprawnienia));
//			inMemoryUserDetailsManager.createUser(User.withUsername(userTable.getUsername())
//					.password(userTable.getPassword())
//					.roles("Admin")
//					.build());
//		}
//
//
//
//
//
//
////		userList.add(normalUser);
////		userList.add(normalUser2);
////		userList.add(adminUser);
//
////		System.out.println(userList.toString() +"DDDD"+"\n");
//
//		System.out.println(inMemoryUserDetailsManager.toString());
////		return new InMemoryUserDetailsManager(userList);
//		return inMemoryUserDetailsManager;
//	}

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
