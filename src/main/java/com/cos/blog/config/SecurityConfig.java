package com.cos.blog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

// 빈 등록 : 스프링 컨테이너에서 객체를 관리할 수 있게 하는 것
// 사실상 3개는 스프링 시큐리티의 셋트이다.
@Configuration	// 빈 등록(IoC관리)
@EnableWebSecurity  // 시큐리티 필터가 등록이 된다.
@EnableGlobalMethodSecurity(prePostEnabled = true)		// 특정 주소로 접근을 하면 권한 및 인증을 미리 체크하겠다는 뜻.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Bean // IoC가 된다.
	public BCryptPasswordEncoder encodePWD() {
		// 이 값은 스프링에서 관리함
		return new BCryptPasswordEncoder();
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
			.csrf().disable()	// csrf 토큰 비활성화
			.authorizeRequests()
				.antMatchers("/","/auth/**","/js/**","/css/**","/image/**")
				.permitAll()
				.anyRequest()
				.authenticated()
			.and()
				.formLogin()
				.loginPage("/auth/loginForm")	// 인증이 필요한 사용자는 /auth/loginForm 으로 간다.
				.loginProcessingUrl("/auth/loginProc") // 스프링 시큐리티가 해당 주소로 접근 시 로그인을 가로채서 대신 로그인 해준다. --> (loginForm.jsp 로 접근 후 로그인 버튼 클릭 시 동작함)
				.defaultSuccessUrl("/");		// 로그인 성공 시 최상위로 이동한다.
//				.failureUrl("/fail")		// 만약 실패 시 이동
	}
}
