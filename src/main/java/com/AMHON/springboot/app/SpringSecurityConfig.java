package com.AMHON.springboot.app;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.AMHON.springboot.app.auth.handler.LoginSuccessHandler;
import com.AMHON.springboot.app.models.service.JpaUserDetailsService;

@EnableGlobalMethodSecurity(securedEnabled=true, prePostEnabled=true)//para las anotaciones @secured y preAuthorize
@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter{
    
	@Autowired //para inyectar, inyeccion de dependencia
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private DataSource dataSource;
	
	@Autowired
	private JpaUserDetailsService userDetailsService;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*http.headers()
		.contentSecurityPolicy("default-src 'self' 'unsafe-inline' https://use.fontawesome.com/releases/v5.6.3/css/all.css; font-src https://use.fontawesome.com/releases/v5.6.3/css/all.css; ");//buscar solucion inyeccion de script
		*/
		/*
		 * http.headers() .defaultsDisabled() .cacheControl();//parchar content type
		 * options http.headers().frameOptions().sameOrigin();
		 */
		http.authorizeRequests().antMatchers("/","/css/**","/js/**","/img/**","/favicon.ico/**","/images/**","/ayuda/indice",
				"/ayuda/Inicio-de-Sesion","/imgs/1.jpg","/imgs/2.jpg","/imgs/3.jpg","/imgs/4.jpg","/imgs/4cc.jpg","/imgs/5cc.jpg","/imgs/6cc.jpg","/recuparPass","/formRecovPass","/formRecupPass").permitAll()
		/*.antMatchers("/ver/**").hasAnyRole("USER")
		.antMatchers("/uploads/**").hasAnyRole("USER")
		.antMatchers("/form/**").hasAnyRole("ADMIN")
		.antMatchers("/eliminar/**").hasAnyRole("ADMIN")*/
		.anyRequest().authenticated()
		.and()
		    .formLogin()
		    .defaultSuccessUrl("/landing")
		    .successHandler(successHandler) //maneja los mensajes de success del login
		    .loginPage("/login")
		    .permitAll()
		.and()
		.logout()
		 .logoutSuccessUrl("/login")
		.permitAll()
		.and()
		.exceptionHandling().accessDeniedPage("/error_403");
		http.sessionManagement()
		  .sessionFixation().migrateSession()
		  .and()
		.sessionManagement().maximumSessions(2).maxSessionsPreventsLogin(true);
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder build) throws Exception {
		
		build.userDetailsService(userDetailsService)
		.passwordEncoder(passwordEncoder);
		
		/* consulta con jdbc build.jdbcAuthentication()
		.dataSource(dataSource)
		.passwordEncoder(passwordEncoder)
		.usersByUsernameQuery("select username, password, enabled from users where username=?")
		.authoritiesByUsernameQuery("select u.username, r.authority from roles r inner join users u on (r.user_id=u.id) where u.username=?");
		*/
		
	/*	PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
		UserBuilder users = User.builder().passwordEncoder(encoder::encode);
		
		build.inMemoryAuthentication()
		.withUser(users.username("chris").password("1993").roles("ADMIN","USER"))
		.withUser(users.username("amhon").password("12345").roles("USER"));*/
	}
}
