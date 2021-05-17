package com.aws.lambda.demo.s3sns;

import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.aws.lambda.demo.s3sns.dto.PatientCheckoutEvent;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class BillManagementLambda {

	private final ObjectMapper objectMapper = new ObjectMapper();

	public void handler(SNSEvent event) {
		event.getRecords().forEach(snsRecord->{
			try {
				var patientCheckoutEvent = objectMapper.readValue(snsRecord.getSNS().getMessage(), PatientCheckoutEvent.class);
				System.out.println(patientCheckoutEvent);
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		});
	}





}
