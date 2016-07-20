package com.app.msg;

import java.io.IOException;
import java.util.List;
import java.util.Properties;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import com.app.dao.DBAccess;
import com.app.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;

public class KafkaConsumerLoop implements Runnable  {
	//NOTE: KAFKA consumer is NOT thread-safe
	
	private final Consumer<String, String> consumer;
	private final List<String> topics;
	private final DBAccess dbAccess;
	private final ObjectMapper mapper = new ObjectMapper();
	
	private static Properties initConsumer(){
    	Properties props = new Properties();
    	props.put("bootstrap.servers", "localhost:9092");
    	props.put("group.id", "test");
    	props.put("enable.auto.commit","true");
    	props.put("key.deserializer",StringDeserializer.class.getName());
    	props.put("value.deserializer",StringDeserializer.class.getName());
    	props.put("session.timeout.ms","10000");
    	props.put("fetch.min.bytes","50000");
    	props.put("receive.buffer.bytes","262144");
    	props.put("max.partition.fetch.bytes","2097152");
    	return props;
    	
    }

	public KafkaConsumerLoop(DBAccess dbAccess,List<String> topics) {
		this.topics=topics;
		this.dbAccess=dbAccess;
		this.consumer=new KafkaConsumer<>(initConsumer());
	}
	
	@Override
	public void run() {
		try {
			consumer.subscribe(topics);
			while(true){  //Poll Loop
				ConsumerRecords<String,String> records = consumer.poll(200);
				for(ConsumerRecord<String,String> record:records){
					try {
						User userInfo= mapper.readValue(record.value(), User.class);
						dbAccess.insertRecord(userInfo);						
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}finally{
			consumer.close();
		}
		
	}	
	
	public void shutdown() {
	    consumer.wakeup();
	  }
	
}
