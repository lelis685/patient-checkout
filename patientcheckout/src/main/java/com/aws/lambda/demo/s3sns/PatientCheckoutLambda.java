package com.aws.lambda.demo.s3sns;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.sns.AmazonSNS;
import com.amazonaws.services.sns.AmazonSNSClientBuilder;
import com.aws.lambda.demo.s3sns.dto.PatientCheckoutEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;



public class PatientCheckoutLambda {

	private static final String PATIENT_CHECKOUT_TOPIC = System.getenv("PATIENT_CHECKOUT_TOPIC");
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final AmazonS3 s3 = AmazonS3ClientBuilder.defaultClient();
	private final AmazonSNS sns = AmazonSNSClientBuilder.defaultClient();
	

	public void handler(S3Event event, Context context) throws IOException {
		
		Logger logger = LoggerFactory.getLogger(PatientCheckoutLambda.class);

		event.getRecords().forEach(record -> {
			S3ObjectInputStream s3ObjectInputStream = s3
					.getObject(record.getS3().getBucket().getName(), record.getS3().getObject().getKey())
					.getObjectContent();

			try {
				
				logger.info("Reading data from S3");
				
				var patientCheckoutEvents = Arrays
						.asList(objectMapper.readValue(s3ObjectInputStream, PatientCheckoutEvent[].class));
				
				logger.info(patientCheckoutEvents.toString());
				
				s3ObjectInputStream.close();
				
				logger.info("Message being published to SNS");
				publishMessageToSns(patientCheckoutEvents);

			} catch (IOException e) {
				logger.error("Exception is:",e);
				throw new RuntimeException("Error while processing S3 event", e);
			}

		});

	}

	private void publishMessageToSns(List<PatientCheckoutEvent> patientCheckoutEvents) {
		patientCheckoutEvents.forEach(checkoutEvent -> {
			try {
				sns.publish(PATIENT_CHECKOUT_TOPIC,
						objectMapper.writeValueAsString(checkoutEvent));
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}

}
