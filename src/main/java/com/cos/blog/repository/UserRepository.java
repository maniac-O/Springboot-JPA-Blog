package com.cos.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.cos.blog.model.User;

// 이렇게 파일을 하나 더 만들어 주는 이유? 관리의 용이함?
// DAO
// 자동으로 bean 등록이 된다. (@Repository 어노테이션 생략 가능)
public interface UserRepository extends JpaRepository<User, Integer> {
	
}
