package com.social.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.social.api.entity.User;
import com.social.api.entity.UserFollow;
import com.social.api.ex.ResourceNotFoundException;
import com.social.api.repository.UserFollowRepository;
import com.social.api.repository.UserRepository;

@Service
public class UserFollowService
{
  private final UserService userService;
  private final UserFollowRepository userFollowRepository;

  public UserFollowService(
      UserService userService,
      UserFollowRepository userFollowRepository)
  {
    this.userService = userService;
    this.userFollowRepository = userFollowRepository;
  }

  public UserFollow followUser(String followerName, String followedName)
  {
    var follower = userService.findByUsername(followerName);
    var followed = userService.findByUsername(followerName);

    if (follower.getId() == followed.getId())
      throw new IllegalArgumentException(
        "Follower is same as followed");

    var follow = new UserFollow();
    follow.setFollower(follower);
    follow.setFollowed(followed);

    userFollowRepository.save(follow);
    return follow;
  }

  public void unfollowUser(String followedName)
  {
    var followed = userService.findByUsername(followedName);
    var follow = userFollowRepository.findByFollowed(followed);

    userFollowRepository.delete(follow);
  }

  public long getFollowerCount(String followerName)
  {
    var follower = userService.findByUsername(followerName);

    return userFollowRepository.countByFollowed(follower);
  }

  public long getFollowingCount(String followerName)
  {
    var follower = userService.findByUsername(followerName);

    return userFollowRepository.countByFollower(follower);
  }

  public boolean follows(String followerName, String followedName)
  {
    var follower = userService.findByUsername(followerName);

    var followed = userService.findByUsername(followerName);

    if (follower.getId() == followed.getId())
      throw new IllegalArgumentException(
        "Follower is same as followed");

    return userFollowRepository.existsByFollowerAndFollowed(follower, followed);
  }

  public List<User> getAllByFollower(String followerName)
  {
    var follower = userService.findByUsername(followerName);

    var l = new ArrayList<User>();

    for (var x : userFollowRepository.findAllByFollower(follower))
    {
      var op = userService.findById(x.getFollowed().getId());
      l.add(op.get());
    }

    return l;
  }
}