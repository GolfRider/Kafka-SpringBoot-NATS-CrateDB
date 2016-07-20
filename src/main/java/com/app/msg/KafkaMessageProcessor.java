package com.app.msg;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.app.dao.DBAccess;
import com.app.model.User;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


@Component
public class KafkaMessageProcessor {

	private final String KAFKA_TOPIC = "topic1";
	private final ObjectMapper mapper = new ObjectMapper();
	private Producer<String,String> kafkaProducer = new KafkaProducer<>(initProducer());
	
	@Autowired
	private DBAccess dbAcccess;
	
	@PostConstruct
	private void setupKafkaConsumer(){
		ExecutorService executor = Executors.newSingleThreadExecutor();
		KafkaConsumerLoop kafkaConsumerLoop = new KafkaConsumerLoop(dbAcccess,Arrays.asList(KAFKA_TOPIC));
		executor.submit(kafkaConsumerLoop);
		cleanupKafkaConsumer(kafkaConsumerLoop, executor);
	}
	
	@PreDestroy
	private void cleanUp(){
		kafkaProducer.close();
	}
	
	private void cleanupKafkaConsumer(final KafkaConsumerLoop kafkaConsumerLoop,final ExecutorService executor){
		Runtime.getRuntime().addShutdownHook(new Thread(){
			   @Override
			   public void run(){
				   kafkaConsumerLoop.shutdown();
				   executor.shutdown();
				    try {
				        executor.awaitTermination(5000, TimeUnit.MILLISECONDS);
				    }
				    catch (InterruptedException e) {
				            e.printStackTrace();
				    }
			   }
		});
	}
	
	private static Properties initProducer(){
		Properties props= new Properties();
		props.put("bootstrap.servers", "localhost:9092"); //list of kafka servers/brokers
		props.put("acks", "all");
		props.put("retries", "0");
		props.put("batch.size", "16384");
		props.put("auto.commit.interval.ms", "1000");
		props.put("linger.ms", "0");
		props.put("key.serializer", StringSerializer.class.getName());
		props.put("value.serializer", StringSerializer.class.getName());
		props.put("block.on.buffer.full", "true");
		
		return props;	
	}
	
	public void sendToKafka(User user){
		try{
			  user.setKafka_Status("KAFKA_PROCESSED");
			  kafkaProducer.send(new ProducerRecord<String, String>(KAFKA_TOPIC, mapper.writeValueAsString(user)));
				
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
	}
	
}
