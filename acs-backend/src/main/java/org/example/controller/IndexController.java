package org.example.controller;


import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.example.support.FunctionLogSupport;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Controller
public class IndexController {

    @GetMapping("/index")
    public String index(final HttpServletRequest request, final ModelMap model) {

        String ipAddress = Optional.ofNullable(request.getHeader("X-FORWARDED-FOR")).orElse(request.getRemoteAddr());

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        System.out.println("Login Time: " + formatter.format(date));
        System.out.println("Login IP: " + ipAddress);


        model.addAttribute("localTime",formatter.format(date));
        model.addAttribute("ip",ipAddress);

        return "index";
    }


    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String root(final HttpServletRequest request, final ModelMap model) {
        FunctionLogSupport.start("indexController.root");
        FunctionLogSupport.end("indexController.root");

        return "redirect:/index";
    }

}
