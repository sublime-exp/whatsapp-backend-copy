package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.service.MessageChangeNotifier;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SpringEventMessageChangeNotifier implements MessageChangeNotifier {

    private final ApplicationEventPublisher applicationEventPublisher;
    private final NotificationService notificationService;

    @Override
    public State<Void, String> send(Message message, List<UserPublicId> usersToNotify) {
        return null;
    }

    @Override
    public State<Void, String> delete(ConversationPublicId conversationPublicId,
                                      List<UserPublicId> usersToNotify) {
        ConversationIdWithUsers conversationIdWithUsers = new ConversationIdWithUsers(conversationPublicId, usersToNotify);
        applicationEventPublisher.publishEvent(conversationIdWithUsers);
        return State.<Void, String>builder().forSuccess();
    }

    @EventListener
    public void handleDeleteConversation(ConversationIdWithUsers conversationIdWithUsers) {
        notificationService.sendMessage(
                conversationIdWithUsers.conversationPublicId().value(),
                conversationIdWithUsers.users(),
                NotificationEventName.DELETE_CONVERSATION);
    }
}
