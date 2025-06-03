package org.example.support;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.StringTokenizer;


public class SVList {
	static private String data="";
	static private int num=0;
	static private String delim=",";
   static String rfc822EscapeChars=" <>@,;:./?=\\\"*~～|+%&\r\n\t";
	
	static public int getLen(String data,String delim){
		StringTokenizer tokens=new StringTokenizer(data,delim);
		return(tokens.countTokens());
	}
	static public int getLen(String data){
		delim=",";
		StringTokenizer tokens=new StringTokenizer(data,delim);
		return(tokens.countTokens());
	}
	static public String rest(String data,String delim){
		int ptr=data.indexOf(delim);
		if(data.indexOf(delim)!=-1)
			return(data.substring(ptr+1));
		else
			return("");
	}
	static public String rest(String data){
		delim=",";
		int ptr=data.indexOf(delim);
		if(data.indexOf(delim)!=-1)
			return(data.substring(ptr+1));
		else
			return("");
	}
	static public String getAt(String data1,String num1,String delim1){
		data=data1;
		num=Integer.parseInt(num1);
		delim=delim1;
		return(process());
	}
	static public String getAt(String data1,int num1,String delim1){
		data=data1;
		num=num1;
		delim=delim1;
		return(process());
	}
	static public String getAt(String data1,int num1){
		delim=",";
		data=data1;
		num=num1;
		return(process());
	}
	static public String deleteAt(String data1,String num1,String delim1){
		data=data1;
		num=Integer.parseInt(num1);
		delim=delim1;
		return(delprocess());
	}
	static public String deleteAt(String data1,int num1,String delim1){
		data=data1;
		num=num1;
		delim=delim1;
		return(delprocess());
	}
	static public String deleteAt(String data1,int num1){
		delim=",";
		data=data1;
		num=num1;
		return(delprocess());
	}
	static public String getAt(String data1,String num1){
		delim=",";
		data=data1;
		num=Integer.parseInt(num1);
		return(process());
	}
	static public String getFirst(String data,String delim){
		int ptr=data.indexOf(delim);
		if(data.indexOf(delim)!=-1)
			return(data.substring(0,ptr));
		else
			return("");
	}
	static public String getFirst(String data){
		delim=",";
		int ptr=data.indexOf(delim);
		if(data.indexOf(delim)!=-1)
			return(data.substring(0,ptr));
		else
			return("");
	}
	static private String process(){
		StringTokenizer tokens=new StringTokenizer(data,delim);
		int count=tokens.countTokens();
		if(num>=count)
			return("");
		else{
			int ptr=0;
			String value="";
			for(int i=0;i<count;i++){
				value=tokens.nextToken();
				if(i==num)
					break;
			}
			return(value);
		}
	}
	static private String delprocess(){
		StringTokenizer tokens=new StringTokenizer(data,delim);
		int count=tokens.countTokens();
		if(num>=count)
			return("");
		else{
			int ptr=0;
			String recombine="";
			String value="";
			for(int i=0;i<count;i++){
				value=tokens.nextToken();
				if(i!=num)
					recombine+=value+delim;
			}
			if(recombine.length()!=0)
				recombine=recombine.substring(0,recombine.length()-1);
			return(recombine);
		}
	}
	static public String replace(String s,String old,String replacement){
		if(s.length()>100000) return s;
	 	int i=s.indexOf(old);
	 	StringBuffer r=new StringBuffer();
	 	if(i==-1) return s;
	 	r.append(s.substring(0,i)+replacement);
	 	if(i+old.length()<s.length())
	 		r.append(replace(s.substring(i+old.length(),s.length()),old,replacement));
	 	return r.toString();
	} 
	static public String getWord(String data,int num){
		if(data.getBytes().length>num){
           StringBuffer sb=new StringBuffer();
           boolean keepGoing=true;
           int wordCount=0;
           int ptr=0;
           int len=data.length();
           while(keepGoing){
           		if(wordCount<num && ptr<len){
           			char c=data.charAt(ptr++);
           			if(((int)c)>1000)
           				wordCount+=2;
           			else
           				wordCount++;
           			sb.append(c);
           		}else
           			keepGoing=false;
           }
           
           data=sb.toString();
       }
       return(data);
	}
	static public String getWord(String data,int num,String append){
		if(data.getBytes().length>num){
           StringBuffer sb=new StringBuffer();
           boolean keepGoing=true;
           int wordCount=0;
           int ptr=0;
           int len=data.length();
           while(keepGoing){
           		if(wordCount<num && ptr<len){
           			char c=data.charAt(ptr++);
           			if(((int)c)>1000)
           				wordCount+=2;
           			else
           				wordCount++;
           			sb.append(c);
           		}else
           			keepGoing=false;
           }
           data=sb.toString()+append;
       }
       return(data);
	}
	static public boolean isCharsetBig5(String big5Data) throws UnsupportedEncodingException{
		byte[] bin=big5Data.getBytes("Big5");
		boolean isBig5=false;
		for(int i=0;i<bin.length;i++)
			if(bin[i]<0){
				isBig5=true;
				break;
			}
      return(isBig5);
	}
    static public String escapeFileName(String unescapeStr){
    	 String filename="";
		 String ext="";
		 StringBuffer sbuffer=new StringBuffer();
		 StringBuffer ebuffer=new StringBuffer();
		 int ptr=unescapeStr.lastIndexOf('.');
		 if(ptr!=-1){
		 	  filename=unescapeStr.substring(0,ptr);
		 	  ext=unescapeStr.substring(ptr+1);
		 }else{
		 	 filename=unescapeStr;
		 }
    	 int filenameLen=filename.length();
    	 int escapeLen=rfc822EscapeChars.length();
		 for(int s=0;s<filenameLen;s++){
		 		char letter=filename.charAt(s);
		 		for(int skipnum=0;skipnum<escapeLen;skipnum++){
		 			if(letter==rfc822EscapeChars.charAt(skipnum))
		 				letter='_';
		 		}
				sbuffer.append(letter);
		 }
		 int extLen=ext.length();
		 for(int e=0;e<extLen;e++){
		 		char letter=ext.charAt(e);
		 		for(int skipnum=0;skipnum<escapeLen;skipnum++){
		 			if(letter==rfc822EscapeChars.charAt(skipnum))
		 				letter='_';
		 		}
				ebuffer.append(letter);
		 }
		 if(ptr!=-1)
		 	  return(sbuffer.toString().trim()+"."+ebuffer.toString());
		 else 
		 	  return(sbuffer.toString().trim());
	 }
    static public String escapeString(String unescapeStr){
		 StringBuffer sbuffer=new StringBuffer();
    	 int unescapeStrLen=unescapeStr.length();
    	 int escapeLen=rfc822EscapeChars.length();
		 for(int s=0;s<unescapeStrLen;s++){
		 		char letter=unescapeStr.charAt(s);
		 		for(int skipnum=0;skipnum<escapeLen;skipnum++){
		 			if(letter==rfc822EscapeChars.charAt(skipnum))
		 				letter='_';
		 		}
				sbuffer.append(letter);
		 }
 	    return(sbuffer.toString().trim());
	 }
    static public String escapeXml(String text){
		   StringBuffer xmlBuffer=new StringBuffer();
         for (int i = 0; i < text.length(); i++) {
             char c = text.charAt(i);
             if (c == '&')
                 xmlBuffer.append("&amp;");
             else if (c == '<')
                 xmlBuffer.append("&lt;");
             else if (c == '>')
                 xmlBuffer.append("&gt;");
             else if (c == '"')
                 xmlBuffer.append("&#034;");
             else if (c == '\'')
                 xmlBuffer.append("&#039;");
             else
                 xmlBuffer.append(c);
         }
         return(xmlBuffer.toString());
	 }	
    
    static public String removeSpace(String unSpace){
    	 int len=unSpace.length();
		 StringBuffer sbuffer=new StringBuffer();
		 for(int s=0;s<len;s++){
		 		char letter=unSpace.charAt(s);
		 		if(letter!=' ')
					sbuffer.append(letter);
		 }
		 return(sbuffer.toString().trim());
	 }
   
   static public String encodeURL(String url,String charset) throws UnsupportedEncodingException{
      StringBuffer sb=new StringBuffer();
      int len=url.length();
      for(int i=0;i<len;i++){
     		String str=String.valueOf(url.charAt(i));
     		if(url.charAt(i)<1000){
     			sb.append(str);
     		}else{
  				sb.append(URLEncoder.encode(str,charset));
     		}
       }
       return(sb.toString());
   }
}