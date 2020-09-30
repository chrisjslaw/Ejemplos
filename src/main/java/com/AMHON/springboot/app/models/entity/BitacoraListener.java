package com.AMHON.springboot.app.models.entity;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public class BitacoraListener implements RevisionListener {
	
	@Override
	public void newRevision(Object revisionEntity) {
		Bitacora rev = (Bitacora) revisionEntity; 
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		rev.setUserName(auth.getName());
	    rev.setDate(new Date()); 
	    rev.setEntidad( revisionEntity.getClass().getCanonicalName().toString());
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
				.getRequest();
		 String ip = request.getHeader("X-Forwarded-For");
		 System.out.println("x forwarded"+ip);
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("Proxy-Client-IP");
	            System.out.println("Proxy client"+ip);
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("WL-Proxy-Client-IP");
	            System.out.println("WL Proxy client"+ip);
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_CLIENT_IP");
	            System.out.println("HTTP client"+ip);
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
	            System.out.println("HTTP X FORWARDED"+ip); 
	        }
	        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
	            ip = request.getRemoteAddr();
	            System.out.println("remote"+ip);
	            System.out.println("host"+request.getRemoteHost());
	            System.out.println("local"+request.getLocalAddr());
	            System.out.println("header custom de remote ip"+request.getHeader("x-your-remote-ip-header"));
	            System.out.println("header custom de protocol ip"+request.getHeader("x-your-protocol-header"));
	        }
	        
	        final WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
	        System.out.println("con details el addres remote"+details.getRemoteAddress());
        rev.setIp(ip);
        //probar remote filter de tomcat
        
	}
	
	
	
}