package servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import bs.WechatProcess;
import bs.entity.*;

public class wechart extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public wechart() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setCharacterEncoding("UTF-8");
		request.setCharacterEncoding("UTF-8");
		
		/* 读取接收到的xml 消息*/
		StringBuffer sb = new StringBuffer();
		InputStream is = request.getInputStream();
		InputStreamReader isr = new InputStreamReader(is,"UTF-8");
		BufferedReader br = new BufferedReader(isr);
		String s = "";
		while((s = br.readLine()) !=null){
			sb.append(s);
		}
		String xml = sb.toString();
		String result = "";
		/** 判断是否是微信接入激活验证，只有首次接入验证时才会收到echostr参数，此时需要把他直接返回**/
		String echostr = request.getParameter("echostr");
		if(echostr != null && echostr.length() >1){
			result = echostr;
		}else{
			 result = new WechatProcess().processWechatMag(xml);
		}
		try{
			OutputStream os = response.getOutputStream();
			os.write(result.getBytes("UTF-8"));
			os.flush();
			os.close();
		} catch(Exception e){
			e.printStackTrace();
		}
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}
