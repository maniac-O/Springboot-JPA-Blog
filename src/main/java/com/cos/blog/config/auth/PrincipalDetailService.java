package com.cos.blog.config.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

// 이 loadUserByUsername 메소드를 오버라이드 하지 않으면 
// 로그인 하는 유저의 정보를 줄 수 없다.(PrincipalDetail.java 에서 username을 받아주기 때문)
// 만들지 않으면 기본 제공하는 username과 password를 리턴해 줄것이다.
@Service
public class PrincipalDetailService implements UserDetailsService{
	
	@Autowired
	private UserRepository userRepository;
	
	// 스프링이 로그인 요청을 가로챌 때, username, password 변수 2개를 가로채는데,
	// password 부분 처리는 알아서 한다.
	// username이 DB에 있는지만 확인해주면 됨.
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User principal = userRepository.findByUsername(username)
				.orElseThrow(()->{
					return new UsernameNotFoundException("해당 사용자를 찾을 수 없습니다. : "+username);
				});
		
		// 만약 password 부분은 맞으면 username이 있는 부분을 리턴해준다.
		return new PrincipalDetail(principal);	// 시큐리티의 세션에 유저 정보가 저장이 됨.
	}
}
