package com.social.api.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.social.api.entity.User;
import com.social.api.entity.UserFollow;
import com.social.api.repository.UserFollowRepository;
import com.social.api.repository.UserRepository;

@Service
public class UserFollowService
{
  private final UserRepository userRepository;
  private final UserFollowRepository userFollowRepository;

  public UserFollowService(
      UserRepository userRepository,
      UserFollowRepository userFollowRepository)
  {
    this.userRepository = userRepository;
    this.userFollowRepository = userFollowRepository;
  }

  public UserFollow followUser(User follower, User followed)
  {
    var follow = new UserFollow();
    follow.setFollower(follower);
    follow.setFollowed(followed);

    userFollowRepository.save(follow);
    return follow;
  }

  public void unfollowUser(User followed)
  {
    var follow = userFollowRepository.findByFollowed(followed);
    userFollowRepository.delete(follow);
  }

  public long getFollowerCount(User follower)
  {
    return userFollowRepository.countByFollowed(follower);
  }

  public long getFollowingCount(User follower)
  {
    return userFollowRepository.countByFollower(follower);
  }

  public boolean follows(User follower, User followed)
  {
    return userFollowRepository.existsByFollowerAndFollowed(follower, followed);
  }

  public List<User> getAllByFollower(User follower)
  {
    var l = new ArrayList<User>();

    for (var x : userFollowRepository.findAllByFollower(follower))
    {
      var op = userRepository.findById(x.getFollowed().getId());
      l.add(op.get());
    }

    return l;
  }
}