<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    
    
<h1>Sample List...!!</h1>

<c:forEach var = "dto" items = "${list}">
	${dto.id} ${dto.pw}<br />
</c:forEach>