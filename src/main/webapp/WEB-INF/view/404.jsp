<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html>
<head>  
    <title>404 Not Found</title>
    <meta name="viewport" content="width=device-width, initial-scale=1">
</head>  
<body>
<style>
    body {
        padding: 0;
        margin: 0;
    }
    ul li {
        padding-top: 5px;
        padding-bottom: 5px;
    }
</style>

<div style="width: 100%;background-color: #F7F7F7">

    <div class="header-small header-middle header-large" style="margin: 0 auto;background-color: #ffffff;min-height: 700px;">
        <div style="width: 100%;text-align: center">
            <div style="width: 50%;margin: 0 auto">
                <img style="width: 200px;margin: 20px auto;" src="<%=request.getContextPath()%>/resources/images/ic_error.png"/>
            </div>
            <div style="color: #2A343D;	font-size: 25px;font-weight: bold">Sorry, Page not found.</div>
            <span style="color: #555;font-size: 14px;">We encounter problem while loading the page you are looking for. You may still:</span>
            <ul style="width: 45%;margin: 0 auto;text-align: left;font-size: 14px;color: #555;">
                <li>You can return <a href="${pageContext.request.contextPath}/home">Home</a></li>
                <li>or contact us at <a href="">184821077**</a></li>
                <li>or e-mail us at <a href="">yangnzhi@163.com</a></li>
            </ul>
        </div>
    </div>

</div>

</body>
</html>
