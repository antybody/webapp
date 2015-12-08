package servlet;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bs.entity.ReceiveXmlEntity;
import bs.entity.ReceiveXmlProcess;

import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;

import org.dom4j.Document;  
import org.dom4j.DocumentHelper;  
import org.dom4j.Element;  
import org.dom4j.io.OutputFormat;  
import org.dom4j.io.XMLWriter; 
 
/**
 * @author haibing.xiao
 * @since jdk1.6
 * @version 1.0
 */
public class wxservlet extends HttpServlet{

	private Logger log =Logger.getLogger(this.getClass().getName());
	private static final long serialVersionUID = 1L;
	private   String Token;
	private   String echostr;
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 connect(request,response);
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		message(request,response);
	}

	 
	
	/**
	 *@author haibing.xiao
	 *@return  
	 *@exception 
	 *@param
	 * 
	 * <p>微信接入认证</p>
	 */
	private void connect(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		log.info("RemoteAddr: "+ request.getRemoteAddr());
		log.info("QueryString: "+ request.getQueryString());
		 if(!accessing(request, response)){
			 log.info("服务接入失败.......");
			 return ;
		 }
		String echostr=getEchostr();
		if(echostr!=null && !"".equals(echostr)){
				log.info("服务接入成果..........");
				response.getWriter().print(echostr);//
		}
	}
	/**
	 * @author haibing.xiao
	 * Date 2013-05-29
	 * @return boolean
	 * @exception ServletException, IOException
	 * @param
	 *
	 *<p>认证反馈比对</p> 
	 */
	private boolean accessing(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException {
		String signature = request.getParameter("signature");
		String timestamp = request.getParameter("timestamp");
		String nonce = request.getParameter("nonce");
		String echostr = request.getParameter("echostr");
		if( isEmpty(signature)){
			return false;
		}
		if(isEmpty(timestamp)){
			return false;
		}
		if(isEmpty(nonce)){
			return false;
		}
		if(isEmpty(echostr)){
			return false;
		}
		// 第一次连接的时候认证
		String[] ArrTmp = { Token, timestamp, nonce };
		Arrays.sort(ArrTmp);
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < ArrTmp.length; i++) {
			sb.append(ArrTmp[i]);
		}
		String pwd = Encrypt(sb.toString());
		 
	    log.info("signature:"+signature+",timestamp:"+timestamp+",nonce:"+nonce+",pwd:"+pwd+",echostr:"+echostr);
	    log.info("JAVA加密后"+pwd+",排序后"+ArrTmp[0]+","+ArrTmp[1]+","+ArrTmp[2]);
	    if(trim(pwd).equals(trim(signature))){
	    	this.echostr =echostr;
	    	return true;
	    }else{
	    	return false;
	    }
	}
	private String Encrypt(String strSrc) {
		MessageDigest md = null;
		String strDes = null;

		byte[] bt = strSrc.getBytes();
		try {
			md = MessageDigest.getInstance("SHA-1");
			md.update(bt);
			strDes = bytes2Hex(md.digest()); //to HexString
		} catch (NoSuchAlgorithmException e) {
			System.out.println("Invalid algorithm.");
			return null;
		}
		return strDes;
	}

	public String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	 
	public String getEchostr(){
		return echostr;
	}
	/**
	 *@author antybody
	 *@return  
	 *@exception ServletException, IOException
	 *@param
	 * 
	 * <p>XML获取</p>
	 */
	 private void message(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException{
		 
		    response.setCharacterEncoding("UTF-8");
			request.setCharacterEncoding("UTF-8");
			
			/* xml*/
			StringBuffer sb = new StringBuffer();
			InputStream is = request.getInputStream();
			InputStreamReader isr = new InputStreamReader(is,"UTF-8");
			BufferedReader br = new BufferedReader(isr);
			String s = "";
			while((s = br.readLine()) !=null){
				sb.append(s);
			}
			String requestStr = sb.toString();
			//String requestStr = new String(xml, "UTF-8");
			try{
				manageMessage(requestStr,request,response);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
 
	 
	/**
	 * @author antybody
	 * @return 
	 * @exception ServletException, IOException
	 * @param
	 * 
	 * <p></p>
	 * 
	 */
	  private void  manageMessage(String requestStr,HttpServletRequest request,HttpServletResponse response)throws ServletException,IOException{
		    
		    String responseStr;
		   
			try {
				 log.info("requestStr:"+requestStr);
				 String str = "";
				 String[] requestStrs = new String[]{};
				 if(requestStr.indexOf("=") != -1){
					 requestStrs = requestStr.split("=");
					 str = requestStrs[1];
				 }else
					 str = requestStr;

				 XMLSerializer xmlSerializer=new XMLSerializer();
				 String xml = java.net.URLDecoder.decode(str,"UTF-8");
				 JSONObject jsonObject =(JSONObject) xmlSerializer.read(xml);
				
				 String msgtype =jsonObject.getString("MsgType");
				 String event = "";
				 String jscontent = "您在输入什么呢？";
				 //jsonObject.put("Content", "您在输入什么呢？");
				 if("text".equals(msgtype)){ //
				     jsonObject.put("Content", "您在输入："+jsonObject.getString("Content"));
				 }else{
					 jsonObject.put("Content", "您在输入什么呢？");
				 }
				 
				 if("event".equals(msgtype)){
					 event =jsonObject.getString("Event");
					 if("subscribe".equals(event)) 
						 jscontent = "欢迎加入小小栈微信号";
					 if("unsubscribe".equals(event))
						 jscontent = "感谢加入小小栈";
					 if("LOCATION".equals(event)){
						
					 }
					 jsonObject.put("Content", jscontent);					 
				 }
				 if("location".equals(msgtype)){
					 jscontent = "您在经度：" +jsonObject.getString("Location_X")+
						     "纬度："+jsonObject.getString("Location_Y")+
						     "中文："+jsonObject.getString("Label");
					 jsonObject.put("Content", jscontent);	
				 }
				 responseStr =creatRevertText(jsonObject);//XML
				 log.info("responseStr:"+responseStr);
				 response.setCharacterEncoding("UTF-8");
				 OutputStream os =response.getOutputStream();
				 os.write(responseStr.getBytes("UTF-8"));
			}   catch (Exception e) { 
				e.printStackTrace();
			}
			
	}
	private String creatRevertText(JSONObject jsonObject){
	    	StringBuffer revert =new StringBuffer();
	    	revert.append("<xml>");
	    	revert.append("<ToUserName><![CDATA["+jsonObject.get("FromUserName")+"]]></ToUserName>");
	    	revert.append("<FromUserName><![CDATA["+jsonObject.get("ToUserName")+"]]></FromUserName>");
	    	revert.append("<CreateTime>"+jsonObject.get("CreateTime")+"</CreateTime>");
	    	revert.append("<MsgType><![CDATA[text]]></MsgType>");
	    	revert.append("<Content><![CDATA["+jsonObject.get("Content")+"]]></Content>");
	    	//revert.append("<FuncFlag>0</FuncFlag>");
	    	revert.append("</xml>");
	    	return revert.toString();
	    }
	@Override
	public void init() throws ServletException {
		Token="antybody28";
	}
	 
	private boolean isEmpty(String str){
		return null ==str || "".equals(str) ? true :false;
	}
	private String trim(String str){
		return null !=str  ? str.trim() : str;
	}
	
	  /** 
     * @param String sourceTime 待转化的时间 
     * @param String dataFormat 日期的组织形式  "yyyy-MM-dd HH:mm:ss"
     * @return long 当前时间的长整型格式,如 1247771400000 
     */  
    private static long string2long(String sourceTime,String dataFormat){  
        long longTime = 0L;  
        DateFormat f = new SimpleDateFormat(dataFormat);  
        Date d = null;  
        try {  
            d = f.parse(sourceTime);  
        } catch (ParseException e) {  
            e.printStackTrace();  
        }  
        longTime = d.getTime();  
        return longTime;  
    } 
	
}
