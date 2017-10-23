package com.team16.gdp.demo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Handles all the URI links and various other URL parameters.
 * Use it to create links with other parts of the application
 **/


@Controller
public class DemoController {

    @GetMapping("/")
    public String index() {
        return "index";
    }



}
