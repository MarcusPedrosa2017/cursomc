package com.nelioalves.cursomc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.nelioalves.cursomc.services.exception.FileException;

@Service
public class S3Service {

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	@Autowired
	private AmazonS3 s3Client;

	@Value("${s3.bucket}")
	private String bucketName;

	public URI uploadFile(MultipartFile multipartFile) {

		try {
			String fileName = multipartFile.getOriginalFilename();
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();
			return uploadFile(is, fileName, contentType);
		} catch (IOException e) {			
			throw new FileException("Erro de IO: " + e.getMessage());
		}

	}
	
	public URI uploadFilePatternName(MultipartFile multipartFile, String fileName) {

		try {
			InputStream is = multipartFile.getInputStream();
			String contentType = multipartFile.getContentType();
			Long lengthFile = multipartFile.getSize();
			fileName = validExtensionName(fileName, multipartFile, contentType);
			return uploadFile(is, fileName, contentType, lengthFile);
		} catch (IOException e) {			
			throw new FileException("Erro de IO: " + e.getMessage());
		}

	}

	private String validExtensionName(String fileName, MultipartFile multipartFile, String contentType) {
		
		String extName = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
		String extSufixContentType = contentType.substring(contentType.indexOf("/") + 1, contentType.length());
		String extPrefixContentType = contentType.substring(0, contentType.indexOf("/"));
		
		if(!extName.equals(extPrefixContentType) && !extName.equals(extSufixContentType)) {
			return fileName = fileName + "." + extName;
		}else if(extName.equals(extPrefixContentType)){
			return fileName = fileName + "." + extPrefixContentType;			
		}else if(extName.equals(extSufixContentType)){
			return fileName = fileName + "." + extSufixContentType;			
		}else {
			throw new FileException("Formato de Arquivo Invalido!");
		}
		
	}

	public URI uploadFile(InputStream is, String fileName, String contentType) {

		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			LOG.info("Iniciando Upload");
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, is, meta));
			LOG.info("Finalizado Upload");
			return s3Client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}

	}
	public URI uploadFile(InputStream is, String fileName, String contentType, Long lengthFile) {
		
		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);
			meta.setContentLength(lengthFile);
			LOG.info("Iniciando Upload");
			s3Client.putObject(new PutObjectRequest(bucketName, fileName, is, meta));
			LOG.info("Finalizado Upload");
			return s3Client.getUrl(bucketName, fileName).toURI();
		} catch (URISyntaxException e) {
			throw new FileException("Erro ao converter URL para URI");
		}
		
	}
}
