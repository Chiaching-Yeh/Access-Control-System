package org.example.support;

import java.io.UnsupportedEncodingException;

//Referenced classes of package java.net:
//         URLEncoder

public class SVURLDecoderSupport
{

 public SVURLDecoderSupport()
 {
 }

 /**
  * @deprecated Method decode is deprecated
  */

 public static String decode(String s, String s1)
     throws UnsupportedEncodingException
 {
     boolean flag = false;
     StringBuffer stringbuffer = new StringBuffer();
     int i = s.length();
     int j = 0;
     if(s1.length() == 0)
         throw new UnsupportedEncodingException("URLDecoder: empty string enc parameter");
     while(j < i) 
     {
         char c = s.charAt(j);
         switch(c)
         {
         case 43: // '+'
             stringbuffer.append(' ');
             j++;
             flag = true;
             break;

         case 37: // '%'
             try
             {
                 byte abyte0[] = new byte[(i - j) / 3];
                 int k = 0;
                 while(j + 2 < i && c == '%') 
                 {
                     abyte0[k++] = (byte)Integer.parseInt(s.substring(j + 1, j + 3), 16);
                     if((j += 3) < i)
                         c = s.charAt(j);
                 }
                 if(j < i && c == '%')
                     throw new IllegalArgumentException("URLDecoder: Incomplete trailing escape (%) pattern");
                 stringbuffer.append(new String(abyte0, 0, k, s1));
             }
             catch(NumberFormatException numberformatexception)
             {
                 throw new IllegalArgumentException("URLDecoder: Illegal hex characters in escape (%) pattern - " + numberformatexception.getMessage());
             }
             flag = true;
             break;

         default:
             stringbuffer.append(c);
             j++;
             break;
         }
     }
     return flag ? stringbuffer.toString() : s;
 }

}