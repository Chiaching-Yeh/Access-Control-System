package org.example.support;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public final class Base64Support {

    public static String encode(String raw, Charset charset) {
        return Base64.getEncoder().encodeToString(raw.getBytes(charset));
    }

    public static String encode(String raw) {
    	if(raw == null || raw.isEmpty()) return "";
        return encode(raw, StandardCharsets.UTF_8);
    }

    public static String decode(String raw, Charset charset) {
    	try {
    		return new String(Base64.getDecoder().decode(raw), charset);
    	}catch(Exception ex) {
    		return raw;
    	}
	}

    public static String decode(String raw) {
    	if(raw == null || raw.isEmpty()) return "";
        return decode(raw, StandardCharsets.UTF_8);
    }

    public static boolean check(String base64) {
    	try {
    		Base64.getDecoder().decode(base64);
    		return true;
    	}catch(Exception ex) {
    		return false;
    	}
    	
    }

    private Base64Support() {
        super();
    }

}
