package com.team16.gdp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;

@Controller
public class AnnotationViewController {

    @RequestMapping("/annotations")
    public String annotations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greetings " + name;
    }

    @RequestMapping("/")
    public String welcome(Map<String, Object> model) {
        model.put("message", "Hello");
        return "welcome";
    }

}