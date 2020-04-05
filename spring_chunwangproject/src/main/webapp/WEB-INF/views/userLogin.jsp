<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.io.PrintWriter" %>

<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<meta name="viewport" content="width-device-width, initial-scale=1">
	<title>천왕광장</title>
	<link rel="stylesheet" href="resources/css/bootstrap.min.css">
	<link rel="stylesheet" href="resources/css/custom.css">
	<script src="https://code.jquery.com/jquery-3.1.1.min.js"></script>
	<script src="resources/js/bootstrap.js"></script>
</head>
<body>
<%
	String userID = null;
	
	if(session.getAttribute("userID") != null) {
		userID = (String)session.getAttribute("userID");
	}
	
	if(userID != null && session.getAttribute("msgType") == null) {
		PrintWriter script = response.getWriter();
		script.println("<script>");
		script.println("alert('이미 로그인 상태입니다.');");
		script.println("location.href = 'index';");
		script.println("</script>");
		script.close();
		return;
	}
%>
	<nav class="navbar navbar-expand-lg navbar-light bg-light">
		<a class="navbar-brand mr-5" href="index" style="color:#aaaaaa;">천왕 광장</a>
		<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbar">
		</button>
		<div id="navbar" class="collapse navbar-collapse">
			<ul class="navbar-nav mr-auto">
				<li class="nav-item">
					<a class="nav-link" href="freeboard">자유광장</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="anonboard">익명광장</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="nanumboard">나눔광장</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="promoteboard">홍보광장</a>
				</li>
			</ul>
		</div>
		<div id="navbar" class="collapse navbar-collapse justify-content-end" style="display:inline; right:0px;">
			<ul class="navbar-nav">
<%
	if(userID == null) {
%>
				<li class="nav-item">
					<a class="nav-link active bold" style="font-weight: bold; border-bottom: solid 2px #343A40;" href="userLogin">로그인</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="userJoin">회원가입</a>
				</li>
<%
	} else {
%>
				<li class="nav-item">
					<a class="nav-link" href="myPage">마이페이지</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="userLogout">로그아웃</a>
				</li>
<%
	}
%>
			</ul>
		</div>
	</nav>
	<section class="container" style="max-width: 560px;">
		<h2 class="bg-dark p-3 mb-3" style="color: #FFFFFF;">
			로그인
		</h2>
		<form method="post" action="loginAction">
			<div class="form-group">
				<label>아이디</label>
				<input type="text" id="userID" name="userID" class="form-control" maxlength="20" autocomplete="off">
			</div>
			<div class="form-group">
				<label>비밀번호</label>
				<input type="password" id="userPassword" name="userPassword" class="form-control" maxlength="64">
			</div>
			<div class="form-group">
				<a class="mr-3" href="idFind" style="text-decoration:none"><small>아이디 찾기</small></a>
				<a href="pwFind" style="text-decoration:none"><small>비밀번호 찾기</small></a>
			</div>
			<input type="submit" class="btn btn-primary" value="로 그 인">
			<a href="userJoin">
				<button type="button" class="btn btn-success ml-2">회원가입</button>
			</a>
		</form>
	</section>
	<%
 		String msgContent = null;
		if(session.getAttribute("msgContent") != null)
			msgContent = (String)session.getAttribute("msgContent");
		String msgType = null;
		if(session.getAttribute("msgType") != null)
			msgType = (String)session.getAttribute("msgType");
		if(msgType != null) {
	%>
	<div class="modal fade" id="messageModal" tabindex="-1" role="dialog" aria-hidden="true">
		<div class="vertical-alignment-helper">
			<div class="modal-dialog vertical-align-center">
				<div class="modal-content" >
					<div class="modal-header panel-heading">
						<h4 class="modal-title">
							<% if(msgType.equals("1")) out.println("성공"); else out.println("실패"); %>
						</h4>
						<button type="button" class="close" data-dismiss="modal">
							<span aria-hidden="true">&times;</span>
							<span class="sr-only">Close</span>
						</button>
					</div>
					<div class="modal-body">
						<%= msgContent %>
					</div>
					<div class="modal-footer">
						<button class="btn btn-<%if(msgType.equals("1")) out.println("primary"); 
						else out.println("danger"); %>" data-dismiss="modal" <% 
								if(msgType.equals("1")) out.print("onclick='goindex()'"); %>>확인</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$('#messageModal').modal({backdrop: 'static'});
		$('#messageModal').modal('show');
	</script>
	<script>
		function goindex() {
			location.replace("index");
		}
	</script>
	<%
		session.removeAttribute("msgType");
		session.removeAttribute("msgContent");
		}
	%>
	<footer class="bg-dark mt-4 p-5 text-center" style="color: #FFFFFF;">
		Copyright &copy; 2018 엄정기 All Rights Reserved.
		<br><br>
		<a style="color: #bbbbbb;"> &lt; Contact &gt; email:
			<a style="color: #ffffff;">umjugnki@Naver.com</a>&nbsp;&nbsp;
			<a style="color: #bbbbbb;">instagram:</a> 
			<a style="color: #ffffff;" href="http://www.instagram.com/spirit_umm" target="_blank">spirit_umm</a>
		</a>
	</footer>
	<!-- 스크립트 추가 -->
	<script src="resources/js/jquery.min.js"></script>
	<script src="resources/js/popper.min.js"></script>
	<script src="resources/js/bootstrap.min.js"></script>
</body>
</html>