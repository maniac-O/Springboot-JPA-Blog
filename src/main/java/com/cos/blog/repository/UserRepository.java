package com.cos.blog.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cos.blog.model.User;

// 이렇게 파일을 하나 더 만들어 주는 이유? 관리의 용이함?
// DAO
// 자동으로 bean 등록이 된다. (@Repository 어노테이션 생략 가능)
public interface UserRepository extends JpaRepository<User, Integer> {
	
	// SELECT * FROM user WHERE username = 1?;
	Optional<User> findByUsername(String username);
}

// JPA Naming 전략
// 원래는 다음과 같은 함수는 JPA에 없다. 하지만 함수는 다음과 같이 변경된다.
// SELECT * FROM user WHERE username = ?1 AND password = ?2;
//User findByUsernameAndPassword(String username, String password);

// native query를 만들 수 있다
// native query는 UserRepository.login()을  호출하면 동작해서 User를 반환한다.
//@Query(value="SELECT * FROM user WHERE username = ?1 AND password = ?2", nativeQuery = true)
//User login(String username, String password);

