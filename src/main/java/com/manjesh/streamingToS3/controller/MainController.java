package com.manjesh.streamingToS3.controller;

import java.util.Map;

import com.manjesh.streamingToS3.service.StreamingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class MainController {

  @Autowired
  StreamingService streamingService;

  @PostMapping(value = "upload")
  private Map<String, String> uploadToS3(@RequestPart(value = "file") MultipartFile multipartFile) {
    return streamingService.uploadToS3(multipartFile);
  }
}
