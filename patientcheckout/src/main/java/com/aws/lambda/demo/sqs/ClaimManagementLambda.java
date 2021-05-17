package com.aws.lambda.demo.sqs;

import com.amazonaws.services.lambda.runtime.events.SQSEvent;

public class ClaimManagementLambda {
	
	public void handler(SQSEvent event) {
		event.getRecords().forEach(msg->{
			System.out.println(msg.getBody());
		});
	}

}
