package com.rfchina.wallet.server.bank;

import   java.util.*;
import   java.net.*;   
import   java.io.*; 
import   java.lang.*;

public class yqzl_query{

       public String  do_sign(String url_str,String send_str)throws Exception{
              String line = "";
       	URL urll  = new URL(url_str);
       	String   contentType="INFOSEC_SIGN/1.0";
       	HttpURLConnection con1 = (HttpURLConnection) urll.openConnection();
       	con1.setDoInput(true);
       	con1.setDoOutput(true);
       	con1.setRequestMethod("POST");
       	con1.setRequestProperty("Content-Type", contentType);
       	con1.setRequestProperty("Content-Length",String.valueOf(send_str.length()));
       	System.out.println("do_sign send["+send_str+"]len="+String.valueOf(send_str.length()));
       	con1.connect();
      		
       	OutputStreamWriter wr = new OutputStreamWriter(con1.getOutputStream()); 
       	wr.write(send_str); 
       	wr.flush(); 
      
      		System.out.println("ResponesCode="+con1.getResponseCode());
      		System.out.println("ResponseMessage="+con1.getResponseMessage());

      		BufferedReader rd = new BufferedReader(new InputStreamReader(con1.getInputStream())); 
      		 
      		String rs; 
      		while ((rs = rd.readLine()) != null) { 
            		line=line+rs; 
      		} 
      		//System.out.println(rs);
      		wr.close(); 
      		rd.close(); 
              return line;
    }

    public String  do_un_sign(String url_str,String send_str)throws Exception{
              String line = "";
       	URL urll  = new URL(url_str);
       	String   contentType="INFOSEC_VERIFY_SIGN/1.0";
       	HttpURLConnection con1 = (HttpURLConnection) urll.openConnection();
       	con1.setDoInput(true);
       	con1.setDoOutput(true);
       	con1.setRequestMethod("POST");
       	con1.setRequestProperty("Content-Type", contentType);
       	con1.setRequestProperty("Content-Length",String.valueOf(send_str.length()));
       	System.out.println("do_un_sign send["+send_str+"]len="+String.valueOf(send_str.length()));
       	con1.connect();
      		
       	OutputStreamWriter wr = new OutputStreamWriter(con1.getOutputStream()); 
       	wr.write(send_str); 
       	wr.flush(); 
      
      		System.out.println("ResponesCode="+con1.getResponseCode());
      		System.out.println("ResponseMessage="+con1.getResponseMessage());

      		BufferedReader rd = new BufferedReader(new InputStreamReader(con1.getInputStream())); 
      		 
      		String rs; 
      		while ((rs = rd.readLine()) != null) { 
            		line=line+rs; 
      		} 
      		//System.out.println(rs);
      		wr.close(); 
      		rd.close(); 
              return line;
    }
    

   

	public String do_query_for_http(String url_str,String send_str)throws Exception{
              
           String line = "";
           URL urll  = new URL(url_str);
           String   contentType="text/plain";
           HttpURLConnection con1 = (HttpURLConnection) urll.openConnection();
           con1.setDoInput(true);
           con1.setDoOutput(true);
           con1.setRequestMethod("POST");
           con1.setRequestProperty("Content-Type", contentType);
           con1.setRequestProperty("Content-Length",String.valueOf(send_str.length()));
           con1.connect();

           int len=send_str.getBytes().length;
           len=len+6;
           System.out.println("len="+len+"===");
                   
           String len_str="";
           if(len<100){
                 len_str="0000"+len;
	    }
           else if(len<1000){
                 len_str="000"+len;
           }
           else if(len<10000){
                 len_str="00"+len;
           }
           String str=len_str+send_str;
           System.out.println("http��ʽ��ѯ���͵��ַ���["+str+"]");

      		
           OutputStreamWriter wr = new OutputStreamWriter(con1.getOutputStream()); 
           wr.write(str); 
           wr.flush(); 
      
      	    System.out.println("ResponesCode="+con1.getResponseCode());
      	    System.out.println("ResponseMessage="+con1.getResponseMessage());

      	    BufferedReader rd = new BufferedReader(new InputStreamReader(con1.getInputStream())); 
      		 
      	    String rs; 
      	    while ((rs = rd.readLine()) != null) { 
            		line=line+rs; 
      	    } 
      	    //System.out.println(rs);
      	    wr.close(); 
      	    rd.close(); 
           
           return line;

}
public static String get_date_format(String type_format)
{
	  String  str;
	  java.text.SimpleDateFormat formatter;
         Date currentTime;
	  formatter = new java.text.SimpleDateFormat(type_format); 
         currentTime = new Date();//�õ���ǰϵͳʱ��
         str= formatter.format(currentTime); //������ʱ���ʽ��
         return str;
}

         
    public static void main(String[] args)throws Exception{
        String  sign_result_str="";
        String  url_str="";
        String  send_sign_str="";
        String  send_query_str="";
        String  ip_str="";
        int	   port;
	 			String  query_result_str="";
        String  sign_obj_str="";
        String  toSign="<sign>";
        String  fromSign="</sign>";
        String  packet_id="";
        

        packet_id=get_date_format("yyyyMMddHHmmss");

        String    host_ip_str="192.168.197.217";

        /****ǩ��****/
	url_str="http://"+host_ip_str+":5666";
        send_sign_str="<body><lists name=\"acctList\"><list><acctNo>95200078801300000003</acctNo></list></lists></body>";
        yqzl_query  query  = new yqzl_query();
        sign_result_str=query.do_sign(url_str,send_sign_str);
        System.out.println("ǩ�����"+sign_result_str);
        
	        
        /**** ��http��ʽ���в�ѯ*/
        url_str="http://"+host_ip_str+":5777";
        //��ȡ<sign></sign>�ֶ��е�����
        System.out.println("λ��="+sign_result_str.indexOf(toSign)+"end="+sign_result_str.indexOf(fromSign));
        int begin_ind=sign_result_str.indexOf(toSign)+6;
	 			int end_ind=sign_result_str.indexOf(fromSign);
        sign_obj_str=sign_result_str.substring(begin_ind,end_ind);
        
        //System.out.println("sign_obj_str="+sign_obj_str);        
        send_query_str="<?xml version='1.0' encoding='Gb2312'?><packet><head><transCode>4402</transCode><signFlag>1</signFlag><packetID>"+packet_id+"</packetID><masterID>2000040752</masterID><timeStamp>2010-10-26 15:37:27</timeStamp></head><body><signature>"+sign_obj_str+"</signature></body></packet>";
	//send_query_str="<?xml version='1.0' encoding='Gb2312'?><packet><head><transCode>4402</transCode><signFlag>1</signFlag><packetID>"+packet_id+"</packetID><masterID>2003809821</masterID><timeStamp>2009-11-27 15:37:27</timeStamp></head><body><signature>"+sign_obj_str+"</signature></body></packet>";
                
	System.out.println("��������=["+send_query_str+"]");
        query_result_str=query.do_query_for_http(url_str,send_query_str);
        System.out.println("��ѯ���["+query_result_str+"]"); 
       
        


        /*��ǩ*/
        url_str="http://"+host_ip_str+":5666";
        toSign="<signature>";
        fromSign="</signature>";
        begin_ind=query_result_str.indexOf(toSign)+11;
	end_ind=query_result_str.indexOf(fromSign);
        sign_obj_str=query_result_str.substring(begin_ind,end_ind);
        
        send_sign_str=sign_obj_str;
        sign_result_str=query.do_un_sign(url_str,send_sign_str);
        System.out.println("��ǩ���"+sign_result_str);
        
  
        
      
      
    }
}
