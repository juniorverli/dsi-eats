package br.univille.eats;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.univille.eats.service.MyUserDetailsService;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private String[] resources = new String[]{
            "/","/about", "/register/**","/webjars/**","/include/**",
            "/css/**","/icons/**","/image/**","/js/**","/layer/**"
    };
	@Autowired
	private MyUserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
		
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
	    DaoAuthenticationProvider authProvider
	      = new DaoAuthenticationProvider();
	    authProvider.setUserDetailsService(userDetailsService);
	    authProvider.setPasswordEncoder(passwordEncoder());
	    return authProvider;
	}
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(resources);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		
		http
		.csrf().disable()
		.authorizeRequests()
			.anyRequest().authenticated()
		.and()
			.formLogin()
            .loginPage("/login")
            .defaultSuccessUrl("/home")
            .permitAll()
        .and()
	        .logout()
	        .logoutUrl("/logout")
	        .logoutSuccessUrl("/home")
	        .invalidateHttpSession(true)
	        .permitAll();
		
		
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return NoOpPasswordEncoder.getInstance();
	}
	
	
}