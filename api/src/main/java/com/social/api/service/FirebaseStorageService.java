package com.social.api.service;

import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.firebase.cloud.StorageClient;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
public class FirebaseStorageService
{
  public String uploadFile(MultipartFile file)
  {
    try
    {
      String fileName = UUID.randomUUID() + "_"
          + file.getOriginalFilename();

      Bucket bucket = StorageClient.getInstance().bucket();

      Blob blob = bucket.create(
        fileName,
        file.getBytes(),
        file.getContentType());

      // Make file publicly readable
      blob.createAcl(com.google.cloud.storage.Acl.of(
        com.google.cloud.storage.Acl.User.ofAllUsers(),
        com.google.cloud.storage.Acl.Role.READER));

      return fileName;

    }
    catch (Exception e)
    {
      e.printStackTrace();
      throw new RuntimeException("File upload failed", e);
    }
  }

  public String getPublicUrl(String fileName)
  {
    return "https://firebasestorage.googleapis.com/v0/b/"
        + StorageClient.getInstance().bucket().getName()
        + "/o/"
        + URLEncoder.encode(fileName, StandardCharsets.UTF_8)
        + "?alt=media";
  }
}