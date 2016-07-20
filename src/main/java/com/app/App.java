package com.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication 
public class App {// Main Application 

	  public static void main(String args[]){
		  
		  SpringApplication.run(App.class, args);
	  }
		
}

/**
             Application Flow: 
             =================
             
             [Rest Request (Browser/Rest Client)] 
                 => [Rest Controller (Spring Boot)]
                 => [Publishes pay-load to NATS messaging server (Spring Bean)]
                 => [Subscriber processes the message (Spring Bean)]
                 => [Kafka [Publisher + Subscriber] (SPring Bean)
                 => [Kafka consumer then persists the message to CRATE database (Spring Bean)]
                 
                 kafka_spring_boot_nats_crate
 
 
 */


