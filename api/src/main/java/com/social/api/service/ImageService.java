package com.social.api.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;

import com.social.api.dto.ImageDto;
import com.social.api.entity.User;
import com.social.api.ex.ResourceNotFoundException;
import com.social.api.repository.ImageRepository;
import com.social.api.repository.UserRepository;

import java.util.List;

@Service
public class ImageService
{
  private final UserRepository userRepository;
  private final ImageRepository imageRepository;
  private final FirebaseStorageService storageService;

  public ImageService(
      UserRepository userRepository,
      ImageRepository imageRepository,
      FirebaseStorageService storageService)
  {
    this.userRepository = userRepository;
    this.storageService = storageService;
    this.imageRepository = imageRepository;
  }

  public Page<ImageDto> getUserImages(String username, Pageable pageable)
  {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new ResourceNotFoundException("User not found"));

    return resolveImagePage(imageRepository.findAllByUser(user, pageable));
  }

  private Page<ImageDto> resolveImagePage(Page<ImageDto> page)
  {
    List<ImageDto> resolved = page.getContent()
    .stream()
    .map(this::resolveImage)
    .toList();
    
    return new PageImpl<>(resolved, page.getPageable(), page.getTotalElements());
  }

  private ImageDto resolveImage(ImageDto image)
  {
    return new ImageDto(
      storageService.getPublicUrl(image.url())
    );
  }
}