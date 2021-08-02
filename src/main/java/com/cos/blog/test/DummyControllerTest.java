package com.cos.blog.test;

import com.cos.blog.model.RoleType;
import com.cos.blog.model.User;
import com.cos.blog.repository.UserRepository;

import java.util.List;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DummyControllerTest {
	
	@Autowired // 의존성 주입(DI)
	private UserRepository userRepository;

	@DeleteMapping("/dummy/user/{id}")
	public String delete(@PathVariable int id) {
		try {
			userRepository.deleteById(id);
		}
		// catch (Exception e) // 모든 예외는 Exception이 부모이기 때문에 이렇게 해도 오류캐치가 되긴 한다.
		catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
			return "삭제에 실패하였습니다. 해당 id는 DB에 없습니다.";
		}
		
		return "삭제 되었습니다.";
	}
	// save()는  삽입 메소드가 맞지만, 이미 있는 id 값이면 update로 고쳐준다.
	// email, password
	@Transactional	// 함수 종료시 자동 commit
	@PutMapping("/dummy/user/{id}")
	public User updateUser(@PathVariable int id, @RequestBody User requestUser) { //json 데이터를 요청 => Java Object(MessageConverter의 Jackson 라이브러리가 변환)로 변환해서 받아준다.
		System.out.println("id : "+id);
		System.out.println("password : "+requestUser.getPassword());
		System.out.println("email : "+requestUser.getEmail());

		// 이 때 영속성 컨텍스트에 영속화 됨
		User user = userRepository.findById(id).orElseThrow(()->{
			return new IllegalArgumentException("수정에 실패하였습니다.");
		});
		// 여기서 변경되기 때문에 @Transaction 이 commit 시키면서 영속화 되었던 값이 update문을 실행시켜줌 (변경감지) -> 더티체킹
		user.setPassword(requestUser.getPassword());
		user.setEmail(requestUser.getEmail());
		
		// 그냥 이렇게 집어넣으면 username은 비어있기 때문에 
		// findById로 값을 가져와서 그 꽉찬 User 객체에 수정할 값을 넣는게 맞다.
		//requestUser.setId(id);
		//requestUser.setUsername("ssar");
		
		//userRepository.save(user);
		
		// @Transactional 어노테이션을 사용시? --> userRepository.save(); 를 하지 않아도 업데이트가 가능하다.
		// 더티 체킹을 하게됨
		return user;
	}
	
	
	@GetMapping("/dummy/users")
	public List<User> list(){
		return userRepository.findAll();
	}
	
	// uri로 페이징된 값 볼 수 있음
	// http://localhost:8080/blog/dummy/user?page=0
	@GetMapping("/dummy/user")
	public List<User> pageList(@PageableDefault(size=2, sort="id", direction=Sort.Direction.DESC) Pageable pageable){
		Page<User> pagingUser = userRepository.findAll(pageable);
		
		List<User> users = pagingUser.getContent();
		return users;
	}
	
	
	// {id} 주소로 파라미터를 전달 받을 수 있음
	// http://localhost:8080/blog/dummy/user/3
	@GetMapping("/dummy/user/{id}")
	public User detail(@PathVariable int id) {
		
		
//    이렇게 Supplier 타입을 알고 오버라이드하는게 번거롭다 -> "람다식"으로 해결할 수 있음
//		User user = userRepository.findById(id).orElseThrow(()-> {
//				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
//		});

		// user/4 를 찾다가 DB에서 못찾아오게 되면 user가 null이 될 것 아냐? 
		// 그럼 return null 이 리턴 되잖아 >> 그럼 프로그램에 문제가 있지 않을까?
		// Optional로 너의 User 객체를 감싸서 가져올테니 null 인지 아닌지 판단해서 return 해

		User user = userRepository.findById(id).orElseThrow(new Supplier<IllegalArgumentException>() {
			@Override
			public IllegalArgumentException get() {
				return new IllegalArgumentException("해당 유저는 없습니다. id : " + id);
			}
		});
		
		// 요청 : 웹브라우저
		// user 객체 = 자바 오브젝트
		// 변환 (웹브라우저가 이해할 수 있는 데이터) -> json (옛날엔 Gson 라이브버리)
		// 스프링부트 = MessageConverter 라는 애가 응답시에 자동 작동
		// 만약에 자바 오브젝트를 리턴하게 되면 MessageConverter가 Jackson 라이브러리를 호출해서
		// user 오브젝트를 json으로 변환해서 브라우저에 던져준다.
		return user;
	}
	
	// 얘는 Form 태그로 받는 데이터
	@PostMapping("/dummy/join")
	public String join(User user) {
		System.out.println("id : " + user.getId() );
		System.out.println("username : " + user.getUsername() );
		System.out.println("password : " + user.getPassword() );
		System.out.println("email : " + user.getEmail() );
		
		user.setRole(RoleType.USER);
		userRepository.save(user);
		return "회원가입이 완료되었습니다.";
	}
}
