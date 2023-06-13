<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %> <!-- 자바스크립트를사용하기위함 -->
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page"></jsp:useBean><!-- 빈을 생성한다. -->
<jsp:setProperty name="user" property="userID"/><!-- 로그인 페이지에서 받아온 사용자 ID를 userID(빈)_에 저장 -->
<jsp:setProperty name="user" property="userPassword" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>로그인액션</title>
</head>
<body>
	<%
		String userID = null;
		if(session.getAttribute("userID") != null){
			userID = (String)session.getAttribute("userID");
		}//현재 접속한 세션이 있는지 체크
		
		if(userID != null){
			PrintWriter script = response.getWriter(); /* 자바스크립트문장실행 */
			script.println("<script>");
			script.println("alert('이미 로그인이 되어 있습니다')");
			script.println("location.href='main.jsp'");
			script.println("</script>");
		}// 이미 로그인했으면 다시 로그인을 할 수 없게 한다
		
		UserDAO userDAO=new UserDAO();//하나의 인스턴스
		int result=userDAO.login(user.getUserID(), user.getUserPassword());//페이지에 입력된 아이디와 비번을 login함수에 넣어줌
		
		if(result==1){ //로그인성공
			session.setAttribute("userID", user.getUserID()); // 로그인에 성공하면 세션을 부여
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