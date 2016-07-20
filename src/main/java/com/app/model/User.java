package com.app.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User {

	private int id;
	private String name;
	private String NATS_Status;
	private String Kafka_Status;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNATS_Status() {
		return NATS_Status;
	}
	public void setNATS_Status(String natsStatus) {
		this.NATS_Status = natsStatus;
	}
	public String getKafka_Status() {
		return Kafka_Status;
	}
	public void setKafka_Status(String kafka_Status) {
		Kafka_Status = kafka_Status;
	}
	
}
