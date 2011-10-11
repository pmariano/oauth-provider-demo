<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

	Você aceita a aplicação ${client.name} a ter seus dados?
	<form action="/oauth/accept" method="post">
		<input type="hidden" name="client_id" value="${client.id}"/>
		<input type="hidden" name="redirect_uri" value="${callback}"/>
		<input type="submit" value="Aceitar">
	</form>
	
	<form action="/oauth/deny" method="post">
		<input type="hidden" name="redirect_uri" value="${callback}"/>
		<input type="submit" value="Nao aceitar">
	</form>
</body>
</html>