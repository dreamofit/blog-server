package com.howay;

import org.dom4j.DocumentException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App   
{
	
    public static void main( String[] args ) throws DocumentException
    {
    	SpringApplication.run(App.class, args);
    }
}
