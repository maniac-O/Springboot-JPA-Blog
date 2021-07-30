package com.cos.blog.test;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TempController {
	
	// http://localhost:8080/blog/temp/home
	@GetMapping("/temp/home")
	public String tempHome() {
		
		// 파일리턴 기본경로 : src/main/resources/static
		// 리턴명 : /home.html
		// 풀 경로 : src/main/resources/static/home.html
		// static 폴더는 png, html 처럼 정적이고 브라우저가 인식할 수 있는 파일만 인식한다. = jsp 파일인식불가 (의존성을 더해줘야함)
		return "/home.html";
	}
	
	@GetMapping("/temp/img")
	public String tempImg() {
		return "/a.png";
	}
	
	@GetMapping("/temp/jsp")
	public String tempJsp() {
		// prefix : /WEB-INF/views/
		// sufix: .jsp
		// 풀네임 : /WEB-INF/views/test.jsp
		return "/test";
	}
}
