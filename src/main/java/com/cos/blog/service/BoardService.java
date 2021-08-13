package com.cos.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cos.blog.controller.dto.ReplySaveRequestDto;
import com.cos.blog.model.Board;
import com.cos.blog.model.Reply;
import com.cos.blog.model.User;
import com.cos.blog.repository.BoardRepository;
import com.cos.blog.repository.ReplyRepository;
import com.cos.blog.repository.UserRepository;

// 스프링이 컴포넌트 스캔을 통해서 Bean에 등록을 해줌. IoC를 해준다.
// @Service 역할
// 1. 트랜젝션 관리 -> 
// 2. 서비스의 의미 -> 
@Service
public class BoardService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BoardRepository boardRepository;
	
	@Autowired
	private ReplyRepository replyRepository;
	
	// 전체 성공 시 commit
	// 실패 시 roleback
	@Transactional
	public void 글쓰기(Board board, User user) {	// title, content
		board.setCount(0);
		board.setUser(user);
		boardRepository.save(board);
	}

	@Transactional(readOnly=true)
	public Page<Board> 글목록(Pageable pageable){
		return boardRepository.findAll(pageable);
	} 

	@Transactional(readOnly=true)
	public Board 글상세보기(int id) {
		return boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 상세보기 실패 : 아이디를 찾을 수 없습니다.");
				});
	}
	
	@Transactional
	public void 글삭제하기(int id) {
		boardRepository.deleteById(id);
	}
	
	@Transactional
	public void 글수정하기(int id, Board requestBoard) {
		Board board = boardRepository.findById(id)
				.orElseThrow(()->{
					return new IllegalArgumentException("글 업데이트 실패 : 아이디를 찾을 수 없습니다.");
				});	// 영속화 완료
		
		board.setTitle(requestBoard.getTitle());
		board.setContent(requestBoard.getContent());
		// 해당 함수로 종료시에 (Service가 종료될 때) 트랜잭션이 종료됩니다. 이때 더티채킹이 일어남 -> 자동 업데이트가 DB 쪽으로 Flush 된다(Commit 해주는데, 당연히 Transactional을 걸어줘야 함).
	}
	
	@Transactional
	public void 댓글쓰기(ReplySaveRequestDto replySaveRequestDto) {	// title, content
		
		User user = userRepository.findById(replySaveRequestDto.getUserId())
				.orElseThrow(()->{
					return new IllegalArgumentException("댓글 쓰기 실패 : 유저 아이디를 찾을 수 없습니다.");
				});	// 영속화 완료
		
		Board board = boardRepository.findById(replySaveRequestDto.getBoardId())
				.orElseThrow(()->{
					return new IllegalArgumentException("댓글 쓰기 실패 : 게시글 아이디를 찾을 수 없습니다.");
				});	// 영속화 완료
		
		Reply reply = Reply.builder()
				.user(user)
				.board(board)
				.content(replySaveRequestDto.getContent())
				.build();
		
		replyRepository.save(reply);
	}
}
