package com.cos.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.cos.blog.config.auth.PrincipalDetail;
import com.cos.blog.service.BoardService;

@Controller
public class BoardController {
	
	@Autowired
	private BoardService boardService;
	
	//@AuthenticationPrincipal PrincipalDetail principal	   // 컨트롤러에서는 세선을 어떻게 찾아?
	@GetMapping({"","/"})
	public String index(Model model, @PageableDefault(size=3, sort="id", direction=Sort.Direction.DESC) Pageable pageable) {
		// 리턴할때 model 정보를 들고 이동
		model.addAttribute("boards", boardService.글목록(pageable));
		// /WEB-INF/views/index.jsp
		return "index";	// @Controller는 viewResolver가 작동			
	}
	
	// USER 권한이 필요
	@GetMapping({"/board/saveForm"})
	public String saveForm() {
		// /WEB-INF/views/index.jsp
		return "/board/saveForm";
	}
}
