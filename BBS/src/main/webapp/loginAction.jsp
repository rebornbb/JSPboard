<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ page import="user.UserDAO" %>
<%@ page import="java.io.PrintWriter" %>
<% request.setCharacterEncoding("UTF-8"); %>
<jsp:useBean id="user" class="user.User" scope="page"></jsp:useBean><!-- ���� �����Ѵ�. -->
<jsp:setProperty name="user" property="userID"/><!-- �� ���� �����Ѵ�. -->
<jsp:setProperty name="user" property="userPassword" />
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>�α��ξ׼�</title>
</head>
<body>
	<%
		UserDAO userDAO=new UserDAO();//�ϳ��� �ν��Ͻ�
		int result=userDAO.login(user.getUserID(), user.getUserPassword());//�������� �Էµ� ���̵�� ����� login�Լ��� �־���
		if(result==1){ //�α��μ���
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("location.href='main.jsp'");//�α��ο� �����ϸ� main��������
			script.println("</script>");
		}
		else if(result==0){ //�α��ν���
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('��й�ȣ�� Ʋ���ϴ�.')");
			script.println("history.back()");
			script.println("</script>");
		}
		else if(result==-1){ //���������������̵�
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('�������� �ʴ� ���̵��Դϴ�.')");
			script.println("history.back()");
			script.println("</script>");
		}
		else if(result==-2){ //�����ͺ��̽�����
			PrintWriter script=response.getWriter();
			script.println("<script>");
			script.println("alert('�����ͺ��̽� ������ �߻��߽��ϴ�.')");
			script.println("history.back()");
			script.println("</script>");
		}
	%>
</body>
</html>