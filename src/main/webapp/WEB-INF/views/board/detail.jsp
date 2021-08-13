<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ include file="../layout/header.jsp" %>
<div class="container">

  <c:if test="${board.user.id == principal.user.id}">
	  <a href="/board/${board.id}/updateForm" class="btn btn-warning">수정</a>
	  <button id="btn-delete" class="btn btn-danger">삭제</button>
  </c:if>
  <button class="btn btn-secondary">돌아가기</button>
  
  <div>
  	글 번호 : <span id="id"><i>${board.id} </i></span>
  	작성자 : <span id="id"><i>${board.user.username} </i></span>
  </div>
  
  <br>
  
  <div class="form-group">
	<h3>${board.title}</h3>
  </div>
  
  <hr>
  
  <div class="form-group">
	  <div>${board.content}</div>
  </div>
  
  <hr>
  <br/>
  
  <div>
  	<div class="card">
  		<form>
  			<input type="hidden" id="userId" value="${principal.user.id }"/>
  			<input type="hidden" id="boardId" value="${board.id }"/>
	  		<div class="card-body">
	  			<textarea id="reply-content" class="form-control" rows="1" ></textarea>
	  		</div>
	  		<div class="card-footer">
	  			<button type="button" id="btn-reply-save" class="btn btn-primary">등록</button>
	  		</div>
  		</form>
  	</div>
  	<br />
  	<div class="card">
  		<div class="card-header">댓글 리스트</div>
  		<ul id="reply--box" class="list-group">
  		  <c:forEach var="reply" items="${board.replys }">
			  <li id="reply--1" class="list-group-item d-flex justify-content-between">
			  	<div>${reply.content }</div>
			  	<div class="d-flex">
			  		<div class="font-italic">작성자 : ${reply.user.username} &nbsp;</div>
			  		<button class="btn btn-danger badge">삭제</button>
			  	</div>
			  </li>
  		  </c:forEach>
		</ul>
  	</div>
  </div>
  
</div>

<script src="/js/board.js"></script>
<%@ include file="../layout/footer.jsp" %>