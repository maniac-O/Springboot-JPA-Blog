package com.cos.blog.controller.api;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ldap.embedded.EmbeddedLdapProperties.Credential;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.controller.dto.ResponseDto;
import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;

@RestController
public class UserApiController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	// 이미지 하이퍼링크로는 무조건 GET 방식만 사용할 수 있다. (CSRF 공격 간접 방지)
	// TODO: 얘 그냥 들어가면 들어가지는데?
	@PostMapping("/auth/joinProc")
	public ResponseDto<Integer> save(@RequestBody User user) {
		System.out.println("UserApiController : save 호출됨");
		userService.회원가입(user);
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1); // 자바 오브젝트를 JSON으로 변환해서 리턴 (Jackson)
	}
	// security 라이브러리를 실행 후 사이트 접근 시 메인페이지로 가지않고
	// security 에서 가로채, localhost:8080/login 페이지로 이동한다.
	// 이동 시 로그인 하라고 뜨는데 id : "user" / pwd : "서버 시작시에 자바콘솔"
	
	
	
	// @PostMapping("/auth/loginProc") 는 만들지 않는다. 
	// spring-security가 가로채도록 만들 것이다.
	
	
	
	
	@PutMapping("/user")
	public ResponseDto<Integer> update(@RequestBody User user){	// key=value, x-www-form-urlencoded
		userService.회원수정(user);
		// 이 시점에 트랜잭션이 종료되기 때문에 DB에 값은 변경이 됬지만
		// 세션값은 변경되지 않은 상태이기 때문에 우리가 직접 세션값을 변경해줄 것이다.
		
		// 세션 재등록
		// 회원수정이 완료되고 데이터가 제대로 반영되기 위해 새션을 강제로 재등록 해준다.
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
	}
	
	
	
//	@PostMapping("/api/user/login")
//	// 함수의 메개변수에 선언해서 사용할 수도 있고, DI로 받아서 사용할 수도 있음
//	//public ResponseDto<Integer> login(@RequestBody User user, HttpSession session)

//	@Autowired
//	private HttpSession session;
//	public ResponseDto<Integer> login(@RequestBody User user){
//		System.out.println("UserApiController : save 호출됨");
//		User principal = userService.로그인(user);	// principal (접근 주체)
//		
//		if(principal != null) {
//			session.setAttribute("principal", principal);
//		}
//		return new ResponseDto<Integer>(HttpStatus.OK.value(), 1);
//	}
}
