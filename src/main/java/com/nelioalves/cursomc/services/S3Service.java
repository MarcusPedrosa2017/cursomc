package com.nelioalves.cursomc.services;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.PutObjectRequest;

@Service
public class S3Service {
		
	private Logger LOG = LoggerFactory.getLogger(S3Service.class);
	
	@Autowired
	private AmazonS3 s3Client;
	
	@Value("${s3.bucket}")
	private String bucketName;
	
	
	public void uploadFile(String localFilePath) throws FileNotFoundException {
		try {
			File file = new File(localFilePath);			
			LOG.info("Iniciando Upload");
			s3Client.putObject(new PutObjectRequest(bucketName, file.getName(), file));			
			LOG.info("Finalizado Upload");
		} catch (AmazonServiceException e) {
			LOG.info("AmazonServiceException: " + e.getErrorMessage());
			LOG.info("Status Code: " + e.getErrorCode());
		} catch(AmazonClientException e ) {
			LOG.info("AmazonClientException: " + e.getMessage());			
		}
		
	}
}
