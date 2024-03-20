package com.pixeltrice.uploaddownloadspringbootapp;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
public class FileStorageController {

	@Autowired
	private FileStorageService fileStorageService;
	
	@PostMapping("/upload-single-file")
	public UploadFileResponse uploadSingleFile(@RequestParam("file") MultipartFile file) {
		String fileName = fileStorageService.storeFile(file);
		
		String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
				.path("/download-file/")
				.path(fileName)
				.toUriString();
		
		return new UploadFileResponse(fileName, fileDownloadUri,
				file.getContentType(), file.getSize());
 }
	
	@PostMapping("/upload-multiple-files")
	public List<UploadFileResponse> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
		return Arrays.asList(files)
				.stream()
                .map(file -> uploadSingleFile(file))
                .collect(Collectors.toList());
	}
}
