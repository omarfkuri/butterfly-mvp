package com.social.api.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.social.api.dto.UserProfileDto;
import com.social.api.entity.Image;
import com.social.api.entity.User;
import com.social.api.ex.ResourceNotFoundException;
import com.social.api.repository.ImageRepository;

@Service
public class UserProfileService
{
    private final UserService userService;
    private final ImageRepository imageRepository;
    private final FirebaseStorageService storageService;
    
    public UserProfileService(
        ImageRepository imageRepository,
        FirebaseStorageService storageService,
        UserService userService
    )
    {  
        this.imageRepository = imageRepository;
        this.storageService = storageService;
        this.userService = userService;
    }
    
    public void setProfilePicture(String username, MultipartFile file)
    {
        var user = userService.findByUsername(username);

        var img = uploadImage(user, file);
        user.getProfile().setProfilePicture(img);

        userService.update(user);
    }
    
    public void setCoverPicture(String username, MultipartFile file)
    {
        var user = userService.findByUsername(username);

        var img = uploadImage(user, file);
        user.getProfile().setCoverPicture(img);

        userService.update(user);
    }

    public UserProfileDto getUserProfile(String username)
    {
        var user = userService.findByUsername(username);

        var prof = user.getProfile();

        var profPic = prof.getProfilePicture();
        var covrPic = prof.getCoverPicture();

        System.out.println("Profile: pic -> " + 
            (profPic == null ? null : profPic.getKey()) + 
            " cover -> " +
            (covrPic == null ? null : covrPic.getKey()));

        return new UserProfileDto(
            profPic != null ? storageService.getPublicUrl(profPic.getKey()) : null,
            covrPic != null ? storageService.getPublicUrl(covrPic.getKey()) : null
        );
    }

    private Image uploadImage(User user, MultipartFile file)
    {
        var key = storageService.uploadFile(file);

        var img = new Image();
        img.setKey(key);
        img.setUser(user);

        imageRepository.save(img);

        return img;
    }
}