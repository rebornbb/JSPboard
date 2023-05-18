<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page"></jsp:useBean><!-- 빈을 생성한다. -->
<jsp:setProperty name="user" property="userID"/><!-- 빈에 값을 저장한다. -->
<jsp:setProperty name="user" property="userPassword" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인액션</title>
</head>
<body>
	<%
		UserDAO userDAO=new UserDAO();//하나의 인스턴스
		int result=userDAO.login(user.getUserID(), user.getUserPassword());//페이지에 입력된 아이디와 비번을 login함수에 넣어줌
		if(result==1){ //로그인성공
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("location.href='main.jsp'");//로그인에 성공하면 main페이지로
			script.println("</script>");
		}
		else if(result==0){ //로그인실패
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('비밀번호가 틀립니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
		else if(result==-1){ //존재하지않은아이디
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('존재하지 않는 아이디입니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
		else if(result==-2){ //데이터베이스오류
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('데이터베이스 오류가 발생했습니다.')");
			script.println("history.back()");
			script.println("</script>");
		}
	%>
</body>
</html>