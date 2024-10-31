package org.jrrevuelta.rr.aws;

import java.util.logging.Logger;

import org.jrrevuelta.rr.config.AwsSesClientConfig;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceAsyncClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;
import com.amazonaws.services.simpleemail.model.SendTemplatedEmailRequest;

public class AwsSesSendEmail {
	
	private AwsSesClientConfig sesContext;
	private AmazonSimpleEmailService ses;
	private String toEmailAddress;
	private String message;
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.service.aws");
	
	public static final String fromEmailAddress = "invitaciones@remoyregatas.org";
	public static final String messageSubject = "Invitación para unirte a 'Remo y Regatas .org'";
	public static final String eMailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";  // from W3C for HTML form fields with type="email"
	
	public AwsSesSendEmail(AwsSesClientConfig sesContext) {
		
		log.finest("RR: Instantiating AWS SES SendMail module.");
		
		// Save deployment configuration for AWS SNS Service
		this.sesContext = sesContext;

		// Use credentials of provided IAM userId in deployment file to prepare SNS Client object
		AWSCredentials credentials = new BasicAWSCredentials(this.sesContext.getIamUserAccessKeyId(), 
															 this.sesContext.getIamUserSecretAccessKey());
		this.ses = AmazonSimpleEmailServiceAsyncClientBuilder.standard()
				   .withCredentials(new AWSStaticCredentialsProvider(credentials))
				   .withRegion(Regions.fromName(this.sesContext.getAwsRegion()))
				   .build();
	}
	
	public AwsSesSendEmail(AwsSesClientConfig sesContext, String toEmailAddress, String message) {
		this(sesContext);
		setToEmailAddress(toEmailAddress);
		setMessage(message);
	}
	
	
	public String getToEmailAddress() {
		return this.toEmailAddress;
	}
	public void setToEmailAddress(String toEmailAddress) {
		if (toEmailAddress.matches(eMailRegex)) {
			this.toEmailAddress = toEmailAddress;
		}
	}
	
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public void sendEmail() {
		
		if (this.toEmailAddress != null && this.message != null && this.toEmailAddress.matches(eMailRegex)) {
			sendEmail(this.toEmailAddress, this.message);
		}
	}
	

	public void sendEmail(String toEmailAddress, String message) {
		
		if (toEmailAddress != null && message != null && toEmailAddress.matches(eMailRegex)) {
			SendEmailRequest request = new SendEmailRequest()
					.withSource(fromEmailAddress)
					.withDestination(new Destination().withToAddresses(toEmailAddress))
					.withMessage(new Message().withSubject(new Content(messageSubject)).withBody(new Body(new Content(message))));
			ses.sendEmail(request);
		}
	}
	
/*	
	public void sendTemplateEmail() {
		
		if (this.toEmailAddress != null && this.message != null && this.toEmailAddress.matches(eMailRegex)) {
			sendEmail(this.toEmailAddress, this.message);
		}
	}
*/	

	public void sendTemplateEmail(String toEmailAddress, String templateName, String values) {
		
		if (toEmailAddress != null && toEmailAddress.matches(eMailRegex)) {
			SendTemplatedEmailRequest request = new SendTemplatedEmailRequest()
					.withSource(fromEmailAddress)
					.withDestination(new Destination().withToAddresses(toEmailAddress))
					.withTemplate(templateName)
					.withTemplateData(values);
			ses.sendTemplatedEmail(request);
		}
	}
}
