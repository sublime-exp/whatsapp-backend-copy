package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.shared.authentication.application.AuthenticatedUser;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/api/sse")
@RequiredArgsConstructor
public class NotificationResource {

    private final NotificationService notificationService;

    @GetMapping("/subscribe")
    public SseEmitter subscribe() {
        String userEmail = AuthenticatedUser.username().username();
        return notificationService.addEmitter(new UserEmail(userEmail));
    }

}
