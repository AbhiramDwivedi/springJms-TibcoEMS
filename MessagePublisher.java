package com.abd.jms;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

@Service
public class MessagePublisher {
	
	@Autowired
	JmsTemplate jmsTemplate;
	
	@Value("${messageFileLocation}")
	private String messageFileLocation;
	
	@Value("${queueName}")
	private String queueName;
	
	private static final Logger LOGGER = LoggerFactory.getLogger(MessagePublisher.class);
	
	public void sendAMessage(){
	  final String messageText = getFileContents();
		jmsTemplate.send(queueName, new MessageCreator() {
			@Override
			public Message createMessage(Session session) throws JMSException {
				TextMessage message = session.createTextMessage(messageText);
	            return message;
			}
		});
	}
	
	public String getFileContents (){
    	final StringBuilder builder = new StringBuilder();
		try {
			final BufferedReader reader = new BufferedReader(new FileReader(new File(messageFileLocation)));
			String line = null;
			while((line = reader.readLine()) != null) {
				builder.append(line);
			}
			reader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return builder.toString();
	}

}
