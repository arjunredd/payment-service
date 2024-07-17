package com.avanse.consumer;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class HttpHeaderUtility {
	
	@Autowired
	Environment env;	
	
  public HttpHeaders setAllHeader() {
	  
	  HttpHeaders headers = new HttpHeaders();  
	  headers.set("Content-Type",env.getProperty("Content-Type"));
      headers.set("MessageId",String.valueOf(Math.random()));
      headers.set("LANGUAGE",env.getProperty("LANGUAGE"));
      //2019-08-28T12:00:00
      //headers.set("REQUESTTIME",env.getProperty("REQUESTTIME"));
      headers.set("REQUESTTIME", new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss").format(new Date()));
      headers.set("Authorization",env.getProperty("Authorization"));
      headers.set("entityid",env.getProperty("entityid"));
      headers.set("ServiceVersion",env.getProperty("ServiceVersion"));
	  return headers;
  }

}