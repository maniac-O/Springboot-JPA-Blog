package com.cos.blog.test;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// 사용자가 요청 -> 응답(HTML 파일)
// @Controller

// 사용자가 요청 -> 응답(Data)
@RestController
public class HttpControllerTest {
	private static final String TAG="HttpControllerTest : ";

	@GetMapping("/blog/http/lombok")
	public String lombokText() {
		Member m = Member.builder().username("ssar").password("1234").email("ssar@naver.com").build();
		System.out.println(TAG + "getter : "+m.getUsername() + ", " +m.getId());
		m.setUsername("cos");
		System.out.println(TAG + "setter : "+m.getUsername());
		
		return "lombok test 완료";
	}
	
	// 인터넷 브라우저 요청은 무조건 get요청밖에 할 수 없다.
	// http://localhost:8080/http/get (select)
	@GetMapping("/blog/http/get")
	// @RequestParam 어노테이션을 사용해도되지만  Member 변수를 사용하면 한번에 다 받아올 수 있다.
	public String getTest(Member m) { // id=1&username=ssar&password=1234&email=ssar@naver.com
		return "get 요청 : "+m.getId() + ", "+m.getUsername()+", "+ m.getPassword()+", "+m.getEmail();
	}

	// http://localhost:8080/http/post (insert)
	@PostMapping("/blog/http/post") // postman에서 body 영역에서 raw 데이터로 plainText로 보내면 text/plain 으로 보내는 것이다.  --> @RequestBody String text
															// @Application/json
	public String postTest(@RequestBody Member m) { // MessageConverter (스프링부트)가 알아서 Member변수에 파싱해줌
		return "post 요청 : "+m.getId() + ", "+m.getUsername()+", "+ m.getPassword()+", "+m.getEmail();
	}

	// http://localhost:8080/http/put (update)
	@PutMapping("/blog/http/put")
	public String putTest(@RequestBody Member m) {
		return "put 요청 : "+m.getId() + ", "+m.getUsername()+", "+ m.getPassword()+", "+m.getEmail();
	}

	// http://localhost:8080/http/delete (delete)
	@DeleteMapping("/blog/http/delete")
	public String deleteTest(@RequestBody Member m) {
		return "delete 요청 : "+m.getId() + ", "+m.getUsername()+", "+ m.getPassword()+", "+m.getEmail();
	}
	
}
