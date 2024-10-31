package org.jrrevuelta.rr.aws;

public class AwsClientConfig {
	
	// IAM userid credentials
	private String iamUserName;
	private String iamUserAccessKeyId;
	private String iamUserSecretAccessKey;
	private String awsRegion;
	// SES
	// SNS
	// KMS
	private String kmsKeyId;
	
	
	public AwsClientConfig() {
		super();
	}
	
	
	public String getIamUserName() { return iamUserName; }
	public void setIamUserName(String iamUserName) { this.iamUserName = iamUserName; }
	
	public String getIamUserAccessKeyId() { return iamUserAccessKeyId; }
	public void setIamUserAccessKeyId(String iamUserAccessKeyId) { this.iamUserAccessKeyId = iamUserAccessKeyId; }
	
	public String getIamUserSecretAccessKey() { return iamUserSecretAccessKey; }
	public void setIamUserSecretAccessKey(String iamUserSecretAccessKey) { this.iamUserSecretAccessKey = iamUserSecretAccessKey; }
	
	public String getAwsRegion() { return awsRegion; }
	public void setAwsRegion(String awsRegion) { this.awsRegion = awsRegion; }
	
	public String getKmsKeyId() { return kmsKeyId; }
	public void setKmsKeyId(String kmsKeyId) { this.kmsKeyId = kmsKeyId; }
	
}
