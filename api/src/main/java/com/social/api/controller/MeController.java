package com.social.api.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
public class MeController
{
  @GetMapping("/me")
  public Map<String, String> me(Authentication auth)
  {
    return Map.of("username", auth.getName());
  }
}
