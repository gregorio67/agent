<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>jsp:forward example</title>
<script>
function n_filesystemChehck() {
	window.requestFileSystem = window.requestFileSystem || window.webkitRequestFileSystem;
	if (window.requestFileSystem) {
		alert("support filesystem");
	}
	else {
		alert("not support filesystem");
	}
}	
</script>
</head>
<body onload="fn_filesystemChehck()">
    This is a page<br/>
    <%-- <jsp:forward page="/oid/cmn/OAuth.do" /> --%>
    <jsp:forward page="/deploy/mainpage.do" />
</body>
</html>