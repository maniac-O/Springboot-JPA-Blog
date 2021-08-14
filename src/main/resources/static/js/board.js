let index = {
	init:function(){
		$("#btn-save").on("click",()=>{
			this.save();
		});
		$("#btn-delete").on("click",()=>{
			this.deleteById();
		});
		$("#btn-update").on("click",()=>{
			this.update();
		});
		$("#btn-reply-save").on("click",()=>{
			this.replySave();
		});
	},
	save:function(){
		//alert('user의 save 함수 호출됨');
		let data = {
			title:$("#title").val(),
			content:$("#content").val(),
		}
		
		$.ajax({
			type:"POST",
			url:"/api/board",
			data:JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",	// body 데이터가 어떤 타입인지(MIME)
			dataType:"json"	// 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json 이라면 => javascript 오브젝트로 변경)
		}).done(function(response){
			alert("글쓰기가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	deleteById:function(){
		let id = $("#id").text();
		
		$.ajax({
			type:"DELETE",
			url:"/api/board/"+id,
			dataType:"json"
		}).done(function(response){
			alert("삭제가 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	},
	
	update:function(){
		//alert('user의 save 함수 호출됨');
		let id = $("#id").val();
		let data = {
			title:$("#title").val(),
			content:$("#content").val(),
		}
		
		$.ajax({
			type:"PUT",
			url:"/api/board/"+id,
			data:JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",	// body 데이터가 어떤 타입인지(MIME)
			dataType:"json"	// 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json 이라면 => javascript 오브젝트로 변경)
		}).done(function(response){
			alert("글 수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	replySave:function(){
		//alert('user의 save 함수 호출됨');
		let data = {
			userId : $("#userId").val(),
			boardId : $("#boardId").val(),
			content:$("#reply-content").val(),
		}
		
		$.ajax({
			type:"POST",
			url:`/api/board/${data.boardId}/reply`,
			data:JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",	// body 데이터가 어떤 타입인지(MIME)
			dataType:"json"	// 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json 이라면 => javascript 오브젝트로 변경)
		}).done(function(response){
			alert("댓글작성이 완료되었습니다.");
			location.href=`/board/${data.boardId}`;
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	replyDelete:function(boardId, replyId){
		$.ajax({
			type:"DELETE",
			url:`/api/board/${boardId}/reply/${replyId}`,
			dataType:"json"
		}).done(function(response){
			alert("댓글삭제 성공");
			location.href=`/board/${boardId}`;
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	}
}

index.init();