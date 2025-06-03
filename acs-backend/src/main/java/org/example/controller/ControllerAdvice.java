package org.example.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.example.support.FunctionLogSupport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.unbescape.html.HtmlEscape;
import org.unbescape.html.HtmlEscapeLevel;
import org.unbescape.html.HtmlEscapeType;

import java.beans.PropertyEditorSupport;
import java.time.Duration;
import java.time.Instant;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@org.springframework.web.bind.annotation.ControllerAdvice
public class ControllerAdvice {

    public ControllerAdvice() {
        super();
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        // Form元素绑定到後台的JaveBean对象作為映射。這個映射的List長度預設最多256，以下方法setAutoGrowCollectionLimit(1024)改變預設長度。
    	binder.setAutoGrowCollectionLimit(1024);
    	// String 類別轉換，將所有傳遞進來的String進行HTML編碼，防止XSS攻擊
        binder.registerCustomEditor(String.class, new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {
                setValue(text == null ? null : HtmlEscape.escapeHtml(text.trim(),
                        HtmlEscapeType.HTML5_NAMED_REFERENCES_DEFAULT_TO_DECIMAL,
                        HtmlEscapeLevel.LEVEL_1_ONLY_MARKUP_SIGNIFICANT));
            }

            @Override
            public String getAsText() {
                Object value = getValue();
                return value != null ? value.toString() : "";
            }
        });
        
        String[] abd=new String[]{"class.*","Class.*","*.class.*","*.Class.*"};
        binder.setDisallowedFields(abd);        
    }
    
    @InitBinder
    public void setAllowedFields(WebDataBinder dataBinder){
	    String[ ]abd=new String[]{"class.*","Class.*","*.class.*","*.Class.*"};
	    dataBinder.setDisallowedFields(abd);
    }

    /**
     * 接到 controller 的 request，先塞入共用的 model
     *
     * @param request             http servlet request
     * @param model               要傳給 view 的 model
     */
    @ModelAttribute
    public void handleRequest(
    		final HttpServletRequest request,
    		final HttpServletResponse response,
    		final @Value("${server.servlet.session.timeout}") String timeout,
    		final HttpSession session,
    		final ModelMap model) {

    	
		FunctionLogSupport.start("ControllerAdvice.handleRequest");

		Instant start = Instant.now();
		Instant end = Instant.now();
		Duration timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");

		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		CsrfToken token = repository.generateToken(request);
		repository.saveToken(token, request, response);

		HashMap<String, String> csrfTokenHashMap = new HashMap<String, String>();

		csrfTokenHashMap.put("headerName", token.getHeaderName());
		csrfTokenHashMap.put("parameterName", token.getParameterName());
		csrfTokenHashMap.put("token", token.getToken());

		model.addAttribute("_csrf", csrfTokenHashMap);

		System.out.println("requestURI is : " + request.getRequestURI());


		//讀取session timeout值(秒)
		//測試機用getMaxInactiveInterval()數值怪怪的,先改成讀設定檔的寫法
		//int countDownMs = request.getSession().getMaxInactiveInterval() * 1000; // 乘1000變毫秒
		int countDownMs = 2400000; // 預設2400000毫秒=40分鐘
		String regEx = "[^0-9]";
		Pattern pattern_regEx = Pattern.compile(regEx);
		Matcher timeout_matcher = pattern_regEx.matcher(timeout);
		Integer timeoutInt = Integer.valueOf(timeout_matcher.replaceAll("").trim());
		if (timeout.contains("m")) {
			countDownMs = timeoutInt * 60000; // *60變秒>乘1000變毫秒
		} else if (timeout.contains("s")) {
			countDownMs = timeoutInt * 1000; // 乘1000變毫秒
		}
		model.addAttribute("countDownMs", countDownMs);

		end = Instant.now();
		timeElapsed = Duration.between(start, end);
		System.out.println("Time taken: "+ timeElapsed.toMillis() +" milliseconds");

		FunctionLogSupport.end("ControllerAdvice.handleRequest");


        
    }
}
