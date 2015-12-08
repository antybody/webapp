/*
 * 测试信息的JS 
 */
var wxconfig = {APPID:"wx832e7ddb8839e528",APPSECRET:"541373eca80fdde596442c274a4c6a2d"};

// 设置全局变量
var wx_ACCESS_TOKEN ="";
// 微信接口
//var wxrzurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=APPID&secret=APPSECRET";
$(document).ready(function(){
	setTimeout(getToken(),7200000);
});

// 获取token 的方法
function getToken(){
	var wxrzurl = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+
    + wxconfig.APPID +"&secret="+ wxconfig.APPSECRET;
	$.getJSON(wxrzurl,function(data){
		    console.log("ACCESS_TOKEN:"+data.access_token);
			$("#revwx").html(data.access_token);
			wx_ACCESS_TOKEN = data.access_token;	
	});
}

// 创建接收组