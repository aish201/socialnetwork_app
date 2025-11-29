package com.SocialNetwork.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
@Service
public class ImageUploadService {
	
	public String saveImage(MultipartFile file, String folder) throws IOException {
		  String uploadDir = "uploads/"+folder;
		  Files.createDirectories(Paths.get(uploadDir));
		  String originalFileName = StringUtils.cleanPath(file.getOriginalFilename());
		  String fileName = System.currentTimeMillis()+"_"+ originalFileName;
		  Path filePath = Paths.get(uploadDir).resolve(fileName);
		  
		  Files.copy(file.getInputStream(),filePath,StandardCopyOption.REPLACE_EXISTING);
		  return "http://localhost:8080/uploads/"+folder+"/"+fileName;
	  }
}
