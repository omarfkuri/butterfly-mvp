package com.social.api.events;

import com.social.api.entity.Post;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PostEventPublisher
{

  private final Map<String, List<SseEmitter>> topics = new ConcurrentHashMap<>();

  public SseEmitter subscribe(String topic)
  {
    SseEmitter emitter = new SseEmitter(0L);

    topics
        .computeIfAbsent(topic,
          t -> new CopyOnWriteArrayList<>())
        .add(emitter);

    emitter.onCompletion(() -> removeEmitter(topic, emitter));
    emitter.onTimeout(() -> removeEmitter(topic, emitter));

    return emitter;
  }

  private void removeEmitter(String topic, SseEmitter emitter)
  {
    List<SseEmitter> emitters = topics.get(topic);
    if (emitters != null)
    {
      emitters.remove(emitter);
      if (emitters.isEmpty())
      {
        topics.remove(topic);
      }
    }
  }

  @PreDestroy
  public void shutdown()
  {
    topics.values()
        .forEach(list -> list.forEach(SseEmitter::complete));
    topics.clear();
  }

  public void postCreated(Post post)
  {
    publish(post, "created");
  }

  public void postUpdated(Post post)
  {
    publish(post, "updated");
  }

  public void postDeleted(Long postId, String username)
  {
    emit("all", "deleted", postId);
    emit("author:" + username, "deleted", postId);
  }

  private void publish(Post post, String event)
  {
    emit("all", event, post);
    emit("author:" + post.getUsername(), event, post);
  }

  private void emit(String topic, String event, Object data)
  {
    List<SseEmitter> emitters = topics.get(topic);
    if (emitters == null)
      return;

    for (SseEmitter emitter : emitters)
    {
      try
      {
        emitter.send(
          SseEmitter.event()
              .name(event)
              .data(data));
      }
      catch (Exception e)
      {
        emitters.remove(emitter);
      }
    }
  }
}
