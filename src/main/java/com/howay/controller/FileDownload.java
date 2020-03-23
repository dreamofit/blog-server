/**
 * 
 */
package com.howay.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.RandomAccess;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

/**
 * @author pkc00
 *
 */
@RestController
@RequestMapping(value = "/howay")
public class FileDownload {
    
    public static final String BASE_DIR = "/home/howay/download/";
    
    @CrossOrigin 
    @RequestMapping(value = "/download",method = {RequestMethod.POST,RequestMethod.GET})
    public String downloadFile(HttpServletRequest request, 
    		HttpServletResponse response,
    		@RequestParam("filename") String fileName) {
    	System.out.println(request);
    	
    	String userAgent = request.getHeader("User-Agent");  
    	System.out.println(userAgent);
        String formFileName = fileName;  
            
        // 针对IE或者以IE为内核的浏览器：  
        if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {  
            try {
				formFileName = java.net.URLEncoder.encode(formFileName, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        } else {  
            // 非IE浏览器的处理：  
            try {
				formFileName = new String(formFileName.getBytes("UTF-8"), "ISO-8859-1");
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  
        } 
    String filePath = BASE_DIR+fileName;
    System.out.println(fileName);
        if (filePath != null) {
            //设置文件路径
            File file = new File(filePath);
            long lenth = file.length();
            int fSize = Integer.parseInt(String.valueOf(file.length()));
            long pos = 0;
            if (file.exists()) {
                response.setContentType("application/force-download");// 设置强制下载不打开
                response.setHeader("Accept-Ranges", "bytes");//服务器通知客户端支持断点续传
                response.addHeader("Content-Disposition", "attachment;fileName=" + fileName);// 设置文件名
                response.setHeader("Content-Length", lenth + "");
                response.setHeader("Content-disposition",String.format("attachment; filename=\"%s\"", formFileName));  
                response.setContentType("multipart/form-data");   
                response.setCharacterEncoding("UTF-8");
//                response.setHeader("Access-Control-Allow-Headers", "*");
//                response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
//                response.setHeader("Access-Control-Allow-Expose-Headers", "*");
//                response.setHeader("Access-Control-Allow-Origin", "*");
//                response.setHeader("Access-Control-Allow-Credentials", "true");
                if (request.getHeader("Range") != null) {
                	// 若客户端传来Range，说明之前下载了一部分，设置206状态(SC_PARTIAL_CONTENT)
                	response.setStatus(HttpServletResponse.SC_PARTIAL_CONTENT);
                	pos = Long.parseLong(request.getHeader("Range").replaceAll("bytes=", "").replaceAll("-", ""));
                	
                }
                
                byte[] buffer = new byte[1];
                FileInputStream fis = null;
                BufferedInputStream bis = null;
                try {
                    fis = new FileInputStream(file);
                    bis = new BufferedInputStream(fis);
                    OutputStream os = response.getOutputStream();
                    String contentRange = new StringBuffer("bytes ").append(pos+"").append("-").append((fSize - 1)+"").append("/").append(fSize+"").toString(); 
                    response.setHeader("Content-Range", contentRange);  
                    bis.skip(pos);
                    int len = 0;
                    while ((len=(bis.read(buffer)))!=-1) {
                    	os.write(buffer,0,len);
                    }
                    os.flush();
                    os.close();
                    return "success";
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (fis != null) {
                        try {
                            fis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return "no found this file,please check file name";
    }
    
    

}
