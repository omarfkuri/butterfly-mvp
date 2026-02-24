package com.social.api.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;
import java.io.InputStream;

@Configuration
public class FirebaseConfig
{

  @PostConstruct
  public void initialize()
  {
    try
    {
      InputStream serviceAccount = getClass().getClassLoader()
          .getResourceAsStream(
            "firebase-service-account.json");
      
      if (serviceAccount == null) {
        throw new IllegalStateException(
          "firebase-service-account.json not found in classpath");
      }

      FirebaseOptions options = FirebaseOptions.builder()
      .setCredentials(GoogleCredentials.fromStream(serviceAccount))
      .setStorageBucket("fkuri-social.firebasestorage.app")
      .build();

      if (FirebaseApp.getApps().isEmpty())
      {
        FirebaseApp.initializeApp(options);
      }

    }
    catch (Exception e)
    {
      throw new RuntimeException(
          "Firebase initialization failed", e);
    }
  }
}