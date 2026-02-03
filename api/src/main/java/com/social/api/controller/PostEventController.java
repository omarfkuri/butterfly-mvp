package com.social.api.controller;

import com.social.api.events.PostEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class PostEventController
{
  private final PostEventPublisher publisher;

  public PostEventController(PostEventPublisher publisher)
  {
    this.publisher = publisher;
  }

  @GetMapping("/events/posts/{topic}")
  public SseEmitter subscribe(@PathVariable String topic)
  {
    return publisher.subscribe(topic);
  }
}
