package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.application.UserApplicationService;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class NotificationService {

    private final UserApplicationService userApplicationService;

    private Map<String, SseEmitter> emitters = new HashMap<>();

    @Scheduled(fixedRate = 5000)
    public void heartBeat() throws IOException {
        for (Map.Entry<String, SseEmitter> ssEmitter : emitters.entrySet()) {
            try {
                ssEmitter.getValue()
                        .send(SseEmitter.event()
                                .name("heartbeat")
                                .id(ssEmitter.getKey())
                                .data("Check heartbeat..."));

            } catch (IllegalStateException e) {
                log.info("remove this one from the map {}", ssEmitter.getKey());
                emitters.remove(ssEmitter.getKey());
            }
        }
    }

    public SseEmitter addEmitter(UserEmail userEmail) {
        Optional<User> userByEmail = userApplicationService.getUserByEmail(userEmail);
        if (userByEmail.isPresent()) {
            log.info("new Emitter added to the list {}", userEmail.value());
            SseEmitter newEmitter = new SseEmitter(60000L);
            try {
                UUID userUUID = userByEmail.get().getUserPublicId().value();
                newEmitter.send(SseEmitter.event()
                        .id(userUUID.toString())
                        .data("Starting connection..."));
                this.emitters.put(userUUID.toString(), newEmitter);
                return newEmitter;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public void sendMessage(Object message,
                            List<UserPublicId> usersToNotify,
                            NotificationEventName notificationEventName) {

        for (UserPublicId userId : usersToNotify) {
            String userUUIDRaw = userId.value().toString();
            if (emitters.containsKey(userUUIDRaw)) {
                log.info("located user public id, let send him message: {}", userUUIDRaw);
                SseEmitter sseEmitter = emitters.get(userUUIDRaw);
                try {
                    sseEmitter.send(SseEmitter.event()
                            .name(notificationEventName.value)
                            .id(userUUIDRaw)
                            .data(message));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
}
