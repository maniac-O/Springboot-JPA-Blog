package com.cos.blog.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.cos.blog.model.KakaoProfile;
import com.cos.blog.model.OAuthToken;
import com.cos.blog.model.User;
import com.cos.blog.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// 인증이 안된 사용자들이 출입할 수 있는 경로를 /auth/**허용 (인증이 안되서 회원가입, 로그인을 할려는데 그거조차 못들어가면 어떡하냐)
// 그냥 주소가 / 이면 index.jsp 허용
// static 이하에 있는 /js/**, /css/**, /image/** 허용
@Controller
public class UserController {
	
	@Value("${cos.key}")
	private String cosKey;
	
	@Autowired
	private UserService userService;	
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@GetMapping("/auth/joinForm")
	public String joinForm() {
		
		return "/user/joinForm";
	}
	
	@GetMapping("/auth/loginForm")
	public String loginForm() {
		
		return "/user/loginForm";
	}

	@GetMapping("/auth/kakao/callback")
	public String kakaoCallback(String code) { 	// Data를 리턴해주는 컨트롤러 함수
		
		// POST 방식으로 key=value 데이터를 요청해야한다.	 (카카오쪽으로)
		// Retrofit2
		// OkHttp
		// RestTemplate
		
		// 토큰 생성 요청 RestTemplate 생성
		RestTemplate rt = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers = new HttpHeaders();
		// key=value 형태라고 선언해줌
		headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpBody 오브젝트 생성
		MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
		params.add("grant_type", "authorization_code");
		params.add("client_id", "0fd5a0bca97406852777dcf00b3958d0");
		params.add("redirect_uri ", "http://localhost:8080/auth/kakao/callback");
		params.add("code", code);
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
		
		// exchange 함수가 HttpEntity를 받게(3번째 파라미터) 되어 있기 때문에 하나의 오브젝트에 담은 것이다.
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity response = rt.exchange(
				"https://kauth.kakao.com/oauth/token", 
				HttpMethod.POST,
				kakaoTokenRequest,
				String.class
		);
		
		// Gson, Json Simple, ObjectMapper
		ObjectMapper objectMapper = new ObjectMapper();
		String responseBody = (String) response.getBody();
		OAuthToken oauthToken = null;
		try {
			// 토큰으로 들어온 데이터 파싱
			oauthToken = objectMapper.readValue(responseBody, OAuthToken.class);
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		
		RestTemplate rt2 = new RestTemplate();
		
		// HttpHeader 오브젝트 생성
		HttpHeaders headers2 = new HttpHeaders();
		// key=value 형태라고 선언해줌
		headers2.add("Authorization","Bearer "+oauthToken.getAccess_token());
		headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
		
		// HttpHeader와 HttpBody를 하나의 오브젝트에 담기
		HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);
		
		// exchange 함수가 HttpEntity를 받게(3번째 파라미터) 되어 있기 때문에 하나의 오브젝트에 담은 것이다.
		// Http 요청하기 - Post방식으로 - 그리고 response 변수의 응답 받음
		ResponseEntity response2 = rt2.exchange(
				"https://kapi.kakao.com/v2/user/me", 
				HttpMethod.POST,
				kakaoProfileRequest2,
				String.class
		);

		ObjectMapper objectMapper2 = new ObjectMapper();
		String responseBody2 = (String) response2.getBody();
		KakaoProfile kakaoProfile = null;
		try {
			kakaoProfile = objectMapper2.readValue(responseBody2, KakaoProfile.class);
		}catch (JsonMappingException e) {
			e.printStackTrace();
		}catch (JsonProcessingException e) {
			e.printStackTrace();
		}
		System.out.println("카카오 아이디(번호) : "+kakaoProfile.getId());
		
		System.out.println("블로그 서버 유저네임 : "+kakaoProfile.getKakao_account().getProfile().nickname);
		// UUID란 -> 중복되지 않는 어떤 특정 값을 만들어내는 알고리즘
		// UUID garbagePassword = UUID.randomUUID();
		System.out.println("블로그 서버 패스워드 : "+cosKey);

		User kakaoUser = User.builder()
				.username(kakaoProfile.getId().toString())
				.password(cosKey)
				.email("tmp")
				.oauth("kakao")
				.build();
		
		// 가입자 혹은 비가입자 체크 해서 처리
		User originUser = userService.회원찾기(kakaoUser.getUsername().toString());
		
		if(originUser.getUsername() == null) {
			System.out.println("기존 회원이 아니기 때문에 자동으로 회원가입을 진행합니다.");
			userService.회원가입(kakaoUser);
		}
		
		System.out.println("자동 로그인을 진행합니다.");
		
		// 로그인 처리
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(kakaoUser.getUsername(), cosKey));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		return "redirect:/";
	}
	
	
	@GetMapping("/user/updateForm")
	public String updateForm() {
		
		return "/user/updateForm";
	}
	
}
