package com.app.rest;

import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.app.model.User;
import com.app.msg.NATSMessageProcessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
public class RestController {

	
	@Autowired
	private NATSMessageProcessor msgProcesser;
	
	private AtomicInteger atomicInteger=new AtomicInteger(200);
	private ObjectMapper mapper=new ObjectMapper();
	
	@RequestMapping(method=RequestMethod.GET,path="/hello")
	@ResponseBody
	String home(){
		return "Hello";
	}
	
	@RequestMapping(method=RequestMethod.POST,path="/rec",consumes=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	int insert(@RequestBody Map<String,Object> payload){
		int id= atomicInteger.getAndIncrement();
		
		User user=new User();
		user.setId(id);
		user.setName(payload.get("name").toString());
	   
		try {
			msgProcesser.publish(msgProcesser.topicName1, mapper.writeValueAsString(user));
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	    return 0;
	}	
	
}
