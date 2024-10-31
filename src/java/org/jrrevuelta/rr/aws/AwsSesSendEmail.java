package org.jrrevuelta.rr.aws;

import java.util.logging.Logger;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ses.SesClient;
import software.amazon.awssdk.services.ses.model.Body;
import software.amazon.awssdk.services.ses.model.Content;
import software.amazon.awssdk.services.ses.model.Destination;
import software.amazon.awssdk.services.ses.model.Message;
import software.amazon.awssdk.services.ses.model.SendEmailRequest;
import software.amazon.awssdk.services.ses.model.SendTemplatedEmailRequest;

public class AwsSesSendEmail {
	
	private AwsClientConfig sesContext;
	private SesClient ses;
	private String toEmailAddress;
	private String message;
	
	public static final String fromEmailAddress = "invitaciones@remoyregatas.org";
	public static final String messageSubject = "Invitación para unirte a 'Remo y Regatas .org'";
	public static final String eMailRegex = "^[a-zA-Z0-9.!#$%&’*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$";  // from W3C for HTML form fields with type="email"
	
	private static Logger log = Logger.getLogger("org.jrrevuelta.rr.aws");
	
	
	public AwsSesSendEmail(AwsClientConfig sesContext) {
		log.finest("RR: Instantiating AWS SES SendMail module.");
		
		// Save deployment configuration for AWS SNS Service
		this.sesContext = sesContext;

		// Use credentials of provided IAM userId in deployment file to prepare SNS Client object
		AwsCredentials credentials = AwsBasicCredentials.create(this.sesContext.getIamUserAccessKeyId(), 
															    this.sesContext.getIamUserSecretAccessKey());
		AwsCredentialsProvider credentialsProvider = StaticCredentialsProvider.create(credentials);

		this.ses = SesClient.builder()
				.credentialsProvider(credentialsProvider)
				.region(Region.of(this.sesContext.getAwsRegion()))
				.build();
	}
	
	public AwsSesSendEmail(AwsClientConfig sesContext, String toEmailAddress, String message) {
		this(sesContext);
		setToEmailAddress(toEmailAddress);
		setMessage(message);
	}
	
	
	public String getToEmailAddress() {
		return this.toEmailAddress;
	}
	public void setToEmailAddress(String toEmailAddress) {
		this.toEmailAddress = toEmailAddress.matches(eMailRegex) ? toEmailAddress : null;
	}
	
	public String getMessage() {
		return this.message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public void sendEmail() {
		
		if (this.toEmailAddress != null && this.message != null) {
			sendEmail(this.toEmailAddress, this.message);
		}
	}
	

	public void sendEmail(String toEmailAddress, String message) {
		
		if (toEmailAddress != null && message != null && toEmailAddress.matches(eMailRegex)) {
			SendEmailRequest request = SendEmailRequest.builder()
					.source(fromEmailAddress)
					.destination(Destination.builder().toAddresses(toEmailAddress).build())
					.message(Message.builder()
							.subject(Content.builder().data(messageSubject).build())
							.body(Body.builder().html(Content.builder().data(message).build()).build())  // TODO review how to build content, in this case it shouldn't be html
							.build())
					.build();
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
			SendTemplatedEmailRequest request = SendTemplatedEmailRequest.builder()
					.source(fromEmailAddress)
					.destination(Destination.builder().toAddresses(toEmailAddress).build())
					.template(templateName)
					.templateData(values)
					.build();
			ses.sendTemplatedEmail(request);
		}
	}
}
