<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
	<!-- CSS only -->
	<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet" integrity="sha384-rbsA2VBKQhggwzxH7pPCaAqO46MgnOM80zW1RWuH61DGLwZJEdK2Kadq2F9CUG65" crossorigin="anonymous">
	<!-- JavaScript Bundle with Popper -->
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js" integrity="sha384-kenU1KFdBIe4zVF0s0G1M5b4hcpxyD9F7jL+jjXkk+Q2h455rYXK/7HAuoJl+0I4" crossorigin="anonymous"></script>
	
	<script>
	function openFileInput() {
		document.getElementById('imageInput').click();
	}
	document.getElementById('imageInput').addEventListener('change',handleFileSelect);
	function handleFileSelect(event) {
		const file = event.target.files[0];
		const previewImage = document.getElementById('previewImage');
		const selectedFileName = document.getElementById('selectedFileName');
		if (file) {
			const reader = new FileReader();
			reader.onload = function(e) {
				previewImage.src = e.target.result;
				selectedFileName.value = file.name;
			};
			reader.readAsDataURL(file);
		}
	}
</script>
	
</head>
<body>
  <!-- Responsive navbar-->
        <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
            <div class="container">
                <a class="navbar-brand" href="/blog/member/main">whoU</a>
                <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"><span class="navbar-toggler-icon"></span></button>
                <div class="collapse navbar-collapse" id="navbarSupportedContent">
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                        <li class="nav-item"><a class="nav-link active" aria-current="page" href="/blog/member/main">Home</a></li>
                        <li class="nav-item"><a class="nav-link" href="/blog/board/list">board</a></li>
                        <li class="nav-item"><a class="nav-link" href="/blog/imgBoard1/list">imgBoard</a></li>
                        <li class="nav-item"><a class="nav-link" href="/blog/img_board2/list">imgBoard2</a></li>
                        <li class="nav-item"><a class="nav-link" href="/blog/guest/guestbook">guestBook</a></li>
                    </ul>
                    <ul class="navbar-nav ms-auto mb-2 mb-lg-0">
                    	<c:if test="${sessionScope.memId != null}">
	                        <li class="nav-item"><a class="nav-link" href="/blog/member/updateForm">myPage</a></li>
	                        <li class="nav-item"><a class="nav-link" href="/blog/member/logout">logout</a></li>
                    	</c:if>
                    	<c:if test="${sessionScope.memId == null}">
	                        <li class="nav-item"><a class="nav-link" href="/blog/member/inputForm">join</a></li>
	                        <li class="nav-item"><a class="nav-link" href="/blog/member/login">login</a></li>
                        </c:if>
                    </ul>
                </div>
            </div>
        </nav>
        <!-- Page content-->
        <div class="container mt-5">
            <div class="row">
                <div class="col-lg-8">
                <c:if test="${board != null}">
				<form action="/blog/img_board2/updatePro" method="post">
                	<table border="1">
					   <tr>
					    <td align="right" colspan="2">
						    <a href="/blog/img_board2/list"> 글목록</a> 
					   </td>
					   </tr>
					   <tr>
					    <td>아이디</td>   
					    <td>
					      ${sessionScope.memId}
					    </td>
					  </tr>
					  <tr>
					    <td>제 목</td>
					    <td>   
					       <input type="text" size="40" maxlength="50" name="subject" value="${board.subject}">
					    </td>
					  </tr>
					  <tr>
					    <td>내 용</td>
					    <td>
					     <textarea name="content" rows="13" cols="40">${board.content}</textarea> 
					    </td>
					  </tr>
					  <tr>
					  	<td>이미지 첨부</td>
					  	<td><input type="file" id="imageInput" name="imgFile"/></td>
					  </tr>
					<tr>      
					 <td> 
					  <input type="hidden" value="${board.img_board2_num}" name="img_board2_num"/>
					  <input type="submit" value="수정" >
					  <input type="button" value="목록보기" OnClick="window.location='/blog/img_board2/list'">
					</td></tr>
					</table>
				</form>
				</c:if>
				
                <c:if test="${board == null }">
                <form method="post" name="writeform" action="/blog/img_board2/writePro" enctype="multipart/form-data">
				<table border="1">
				   <tr>
				    <td>아이디</td>
				    <td>
				      ${sessionScope.memId}
				    </td>
				  </tr>
				  <tr>
				    <td>제 목</td>
				    <td>   
				       <input type="text" size="40" maxlength="50" name="subject">
				    </td>
				  </tr>
				  <tr>
				    <td>내 용</td>
				    <td>
				     <textarea name="content" rows="13" cols="40"></textarea> 
				    </td>
				  </tr>
				  <tr>
				  	<td>이미지 첨부</td>
				  	<td><input type="file" id="imageInput" name="imgFile"/></td>
				  </tr>
				<tr>      
				 <td> 
				  <input type="hidden" name="img" id="selectedFileName">
				  <input type="submit" value="글작성" >
				  <input type="button" value="목록보기" OnClick="window.location='/blog/img_board2/list'">
				</td></tr></table>         
				</form>
				</c:if>   
                    
                </div>
                <!-- Side widgets-->
                <div class="col-lg-4">
                    <!-- Search widget-->
                    <div class="card mb-4">
                        <div class="card-header">Search</div>
                        <div class="card-body">
                            <div class="input-group">
                                <input class="form-control" type="text" placeholder="Enter search term..." aria-label="Enter search term..." aria-describedby="button-search" />
                                <button class="btn btn-primary" id="button-search" type="button">Go!</button>
                            </div>
                        </div>
                    </div>
                    <!-- Categories widget-->
                    <div class="card mb-4">
                        <div class="card-header">Categories</div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col-sm-6">
                                    <ul class="list-unstyled mb-0">
                                        <li><a href="#!">Web Design</a></li>
                                        <li><a href="#!">HTML</a></li>
                                        <li><a href="#!">Freebies</a></li>
                                    </ul>
                                </div>
                                <div class="col-sm-6">
                                    <ul class="list-unstyled mb-0">
                                        <li><a href="#!">JavaScript</a></li>
                                        <li><a href="#!">CSS</a></li>
                                        <li><a href="#!">Tutorials</a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- Side widget-->
                    <div class="card mb-4">
                        <div class="card-header">Side Widget</div>
                        <div class="card-body">You can put anything you want inside of these side widgets. They are easy to use, and feature the Bootstrap 5 card component!</div>
                    </div>
                </div>
            </div>
        </div>
        <!-- Footer-->
        <footer class="py-5 bg-dark">
            <div class="container"><p class="m-0 text-center text-white">Copyright &copy; Your Website 2023</p></div>
        </footer>
</body>
</html>