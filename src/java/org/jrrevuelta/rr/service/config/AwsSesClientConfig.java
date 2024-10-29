package org.jrrevuelta.rr.service.config;

public class AwsSesClientConfig {
	
	// IAM userid credentials
	private String iamUserName;
	private String iamUserAccessKeyId;
	private String iamUserSecretAccessKey;
	
	// AWS SNS 
	private String awsRegion;
	
	
	public AwsSesClientConfig() {
		super();
	}
	
	
	public String getIamUserName() {
		return iamUserName;
	}
	public void setIamUserName(String iamUserName) {
		this.iamUserName = iamUserName;
	}
	
	public String getIamUserAccessKeyId() {
		return iamUserAccessKeyId;
	}
	public void setIamUserAccessKeyId(String iamUserAccessKeyId) {
		this.iamUserAccessKeyId = iamUserAccessKeyId;
	}
	
	public String getIamUserSecretAccessKey() {
		return iamUserSecretAccessKey;
	}
	public void setIamUserSecretAccessKey(String iamUserSecretAccessKey) {
		this.iamUserSecretAccessKey = iamUserSecretAccessKey;
	}
	
	public String getAwsRegion() {
		return awsRegion;
	}
	public void setAwsRegion(String awsRegion) {
		this.awsRegion = awsRegion;
	}
	
}
