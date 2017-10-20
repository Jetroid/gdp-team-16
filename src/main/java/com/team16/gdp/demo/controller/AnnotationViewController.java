package com.team16.gdp.demo.controller;

import data.Annotation;
import data.Person;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;

@Controller
public class AnnotationViewController {

    @RequestMapping("/annotations")
    public String annotations(@RequestParam(value="name", required=false, defaultValue="World") String name, Model model) {
        model.addAttribute("name", name);
        return "greetings " + name;
    }

    @RequestMapping("/")
    public String welcome(Model model) {
        ArrayList<Annotation> annotations = new ArrayList<>();
        annotations.add(new Annotation(1, "Text", "Quote", 1,2 ));
        annotations.add(new Annotation(1, "Text2", "Quote2", 1,2 ));
        model.addAttribute("annotations", annotations);

        HashMap<Integer, Person> people = new HashMap<>();
        people.put(1, new Person(1, "John", "Lemon", "jl@jl.net"));
        model.addAttribute("people", people);

        return "welcome";
    }

}