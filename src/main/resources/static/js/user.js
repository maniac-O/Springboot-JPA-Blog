let index = {
	init:function(){
		$("#btn-save").on("click",()=>{
			this.save();
		});
		
		$("#btn-update").on("click",()=>{
			this.update();
		});
	},
	save:function(){
		//alert('user의 save 함수 호출됨');
		let data = {
			username:$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val(),
		}
		
		// ajax 호출 시 default가 비동기 호출
		// ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청!!
		// ajax가 통신을 성공하고 서버가 json을 리턴해주면 자동으로 자바 오브젝트 변환
		$.ajax({
			type:"POST",
			url:"/auth/joinProc",
			data:JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",	// body 데이터가 어떤 타입인지(MIME)
			dataType:"json"	// 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json 이라면 => javascript 오브젝트로 변경)
		}).done(function(response){
			if(response.status ===500){
				alert("회원가입에 실패하였습니다.");
			}else{
				alert("회원가입이 완료되었습니다.");
				location.href="/";
			}
		}).fail(function(error){
			alert(JSON.stringify(error));
		}); // ajax 통신을 이용해서 3개의 데이터를 json으로 변경하여 insert 요청
	},
	
	// update 기능 맵핑
	// id 값 hidden으로 가져와서 데이터를 넘겨줘야 업데이트 가능
	update:function(){
		let data = {
			id:$("#id").val(),
			username:$("#username").val(),
			password:$("#password").val(),
			email:$("#email").val(),
		}
		
		$.ajax({
			type:"PUT",
			url:"/user",
			data:JSON.stringify(data), // http body 데이터
			contentType:"application/json; charset=utf-8",	// body 데이터가 어떤 타입인지(MIME)
			dataType:"json"	// 요청을 서버로해서 응답이 왔을 때 기본적으로 모든 것이 문자열 (생긴게 json 이라면 => javascript 오브젝트로 변경)
		}).done(function(response){
			alert("회원수정이 완료되었습니다.");
			location.href="/";
		}).fail(function(error){
			alert(JSON.stringify(error));
		});
	}
}

index.init();