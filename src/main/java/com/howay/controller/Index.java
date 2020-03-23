package com.howay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Controller
@RequestMapping(value = "/")
public class Index {
	@RequestMapping(value = {"/sunway"})
    public String upload() {
		
		System.out.println("-----");
		 return "/";
    }
}
