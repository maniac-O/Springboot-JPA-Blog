package com.cos.blog.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

// ORM -> JAVA(다른언어) : Object -> 테이블로 매핑해주는 기술
@Entity // User 클래스가 MySQL에 테이블이 생성이 된다.
public class User {
	
	@Id // PK
	@GeneratedValue(strategy = GenerationType.IDENTITY) // 해당 프로젝트에서 연결된 DB의 넘버링 전략을 따라간다.
	private int id ; // 시퀀스, auto_increment
	
	@Column(nullable = false, length=30)
	private String username;

	@Column(nullable = false, length=100)
	private String password;
	

	@Column(nullable = false, length=50)
	private String email;
	
	@ColumnDefault("'user'")
	private String role; // Enum을 쓰는게 좋다. -> 도메인 설정을 한다(내가 설정한 범위만의 값만 설정 가능). // Admin, user, manager 
	
	@CreationTimestamp // 시간이 자동 입력
	private Timestamp createDate;
}
