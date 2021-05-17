package com.aws.lambda.demo.s3sns.dto;

public class PatientCheckoutEvent {

	public String firstName;
	public String lastName;
	public String ssn;

	public PatientCheckoutEvent() {

	}

	public PatientCheckoutEvent(String firstName, String lastName, String ssn) {
		this.firstName = firstName;
		this.lastName = lastName;
		this.ssn = ssn;
	}

	@Override
	public String toString() {
		return "PatientCheckoutEvent [firstName=" + firstName + ", lastName=" + lastName + ", ssn=" + ssn + "]";
	}




}
