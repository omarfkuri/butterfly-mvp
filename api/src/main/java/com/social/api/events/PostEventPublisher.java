package com.social.api.events;

import com.social.api.entity.Post;

import jakarta.annotation.PreDestroy;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Component
public class PostEventPublisher
{
    private final List<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    public SseEmitter subscribe()
    {
        SseEmitter emitter = new SseEmitter(0L);
        emitters.add(emitter);

        emitter.onCompletion(() -> emitters.remove(emitter));
        emitter.onTimeout(() -> emitters.remove(emitter));

        return emitter;
    }

    @PreDestroy
    public void shutdown()
    {
        for (SseEmitter emitter : emitters)
        {
            emitter.complete();
        }
        emitters.clear();
    }

    public void postCreated(Post post)
    {
        emit("created", post.getId());
    }

    public void postDeleted(Long id)
    {
        emit("deleted", id);
    }

    public void postUpdated(Post saved)
    {
        emit("updated", saved);
    }

    private void emit(String name, Object data)
    {
        for (SseEmitter emitter : emitters)
        {
            try
            {
                emitter.send(
                    SseEmitter.event()
                        .name(name)
                        .data(data)
                );
            }
            catch (Exception e)
            {
                emitters.remove(emitter);
            }
        }
    }
}
