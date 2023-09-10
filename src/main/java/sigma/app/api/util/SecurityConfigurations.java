package sigma.app.api.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfigurations {
	
	@Autowired
	SecurityFilter securityFilter;

	@Bean
	public SecurityFilterChain securyFilterChain(HttpSecurity http) throws Exception {
		return http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
				.and().csrf().disable().cors().and().authorizeHttpRequests()
				.requestMatchers(HttpMethod.POST, "/login").permitAll().requestMatchers(HttpMethod.POST, "/user").permitAll().requestMatchers(HttpMethod.GET, "/user/email/{email}").permitAll()
				.requestMatchers(HttpMethod.POST, "/user/confirm/forgot/{email}/{code}").permitAll().requestMatchers(HttpMethod.POST, "/user/send/forgot/{email}").permitAll()
				.requestMatchers(HttpMethod.PUT, "/user/password").permitAll()
				.anyRequest().authenticated()
				.and().addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
				.build();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
