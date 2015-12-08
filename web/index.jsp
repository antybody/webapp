<%@ page language="java" import="java.util.*" pageEncoding="utf-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>测试页面</title>
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
	<script type="text/javascript" src="lib/jquery-1.8.2.js"></script>
	<script type="text/javascript" src="bs/index.js"></script>
	<script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	
  </head>
  
  <body>
   <h5> 模拟发送微信消息</h5>
    <form action="/webapp/servlet/wxservlet" name=”form2” method="post">
       <textarea name="wx" cols="50" rows="10"></textarea>
       <input type="submit" value="发送"/>
    </form>   
    
   <h5> 接收微信消息</h5> 
    <div id="revwx"><input type="button" value="获取token" onclick="getToken()"></div>
   <h5> 发送微信消息</h5> 
    <div id="sendwx"></div>
  </body>
</html>
