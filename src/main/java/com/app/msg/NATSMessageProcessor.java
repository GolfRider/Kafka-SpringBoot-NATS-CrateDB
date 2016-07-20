package com.app.msg;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.nats.client.Connection;
import io.nats.client.ConnectionFactory;
import io.nats.client.Constants;

@Component
public class NATSMessageProcessor {
	
	private ConnectionFactory natsConnectionFactory=new ConnectionFactory(Constants.DEFAULT_URL);
	public static final String topicName1="msg.type1";
	private ObjectMapper mapper = new ObjectMapper();
	
	@Autowired
	private KafkaMessageProcessor kafkaProcessor;
	
	@PostConstruct
	private void init(){
		subscribe(topicName1);
	}
	
	private void subscribe(String topicName){ //Subscribe,Process,Persist to DB
		try {
			   Connection natsClient=natsConnectionFactory.createConnection();
	           natsClient.subscribe(topicName, msg -> {
	        	      try {
						  User user=mapper.readValue(msg.getData(), User.class);
						  user.setNATS_Status("NATS_Processed");
						  kafkaProcessor.sendToKafka(user);
						  													  
					  } catch (Exception e) {		
						  e.printStackTrace();
					  }	        	      
	           });
	        
	   } catch (IOException e) {
			e.printStackTrace();
		} catch (TimeoutException e) {
			e.printStackTrace();
		}				
	}
	
	public void publish(final String topicName,final String message){
		Connection natsClient;
		try {
			natsClient = natsConnectionFactory.createConnection();
			natsClient.publish(topicName,message.getBytes());
		} catch (IOException | TimeoutException e1) {
			e1.printStackTrace();
		}
			
	}
	
	
}
