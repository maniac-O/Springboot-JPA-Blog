package com.cos.blog.model;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import org.hibernate.annotations.CreationTimestamp;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Board {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY) // auto_increment
	private int id;
	
	@Column(nullable = false, length = 100)
	private String title;

	@Lob // 대용량 데이터
	private String content; // 섬머노트 라이브러리 : <html>태그가 섞여서 디자인이 됨
	
	private int count; // 조회수
	
	@ManyToOne(fetch = FetchType.EAGER) // Board = Many, User = One
	@JoinColumn(name="userId")
	private User user; // 작성자 : DB는 오브젝트를 저장할 수 없다( FK사용 )    <-->  자바는 오브젝트를 저장할 수 있다.   (User라는 오브젝트로 지정을 하면 타입이 충돌난다)
	
	// CascadeType.REMOVE 는 게시글이 삭제된다면 Reply를 같이 삭제하는 속성이다.
	// 하지만 단점은 댓글이 100개가 되면 delete 쿼리가 100개가 날아가서 비효율 적이다.
	// 해결 방법 : Cascade를 달지 않고 삭제 서비스에서 같이 삭제하면 된다. deleteAll 함수로!
	@OneToMany(mappedBy = "board", fetch = FetchType.EAGER, cascade = CascadeType.REMOVE) // mappedBy 연관관계의 주인이 아니다 (난 FK가 아니에요) = DB에 칼럼을 만들지 마세요   (SELECT를 위한 코드)
	@JsonIgnoreProperties({"board"})	// 무한참조 방지, Replys의 board를 무시하는 어노테이션
	@OrderBy("id desc")
	private List<Reply> replys;
	
	@CreationTimestamp
	private Timestamp createDate;
}
