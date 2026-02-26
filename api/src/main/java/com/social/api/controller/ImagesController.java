package com.social.api.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.social.api.dto.ImageDto;
import com.social.api.service.ImageService;

@RestController
@RequestMapping("/image")
public class ImagesController
{
  private final ImageService imageService;

  public ImagesController(
    ImageService imageService)
  {
    this.imageService = imageService;
  }

  @GetMapping("/own")
  public Page<ImageDto> getUserImages(
      Authentication auth,
      @PageableDefault(size = 10, sort = "id") Pageable pageable)
  {
      return imageService.getUserImages(auth.getName(), pageable);
  }
}