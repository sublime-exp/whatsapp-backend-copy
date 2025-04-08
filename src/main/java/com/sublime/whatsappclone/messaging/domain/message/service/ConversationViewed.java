package com.sublime.whatsappclone.messaging.domain.message.service;

import com.sublime.whatsappclone.infrastructure.secondary.message.ConversationViewedForNotification;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.repository.MessageRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessageSendState;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.service.UserReader;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class ConversationViewed {

    private final MessageRepository messageRepository;
    private final MessageChangeNotifier messageChangeNotifier;
    private final UserReader userReader;


    public State<Integer, String> markAsRead(ConversationPublicId conversationPublicId, UserPublicId connectedUserPublicId) {
        List<Message> messageToUpdateSendState = messageRepository.findMessageToUpdateSendState(conversationPublicId, connectedUserPublicId);
        int nbUpdatedMessages = messageRepository.updateMessageSendState(conversationPublicId, connectedUserPublicId, MessageSendState.READ);
        List<UserPublicId> usersToNotify = userReader.findUsersToNotify(conversationPublicId, connectedUserPublicId)
                .stream()
                .map(User::getUserPublicId)
                .toList();
        ConversationViewedForNotification conversationViewedForNotification = new ConversationViewedForNotification(conversationPublicId.value(),
                messageToUpdateSendState.stream().map(message -> message.getPublicId().value()).toList());
        messageChangeNotifier.view(conversationViewedForNotification, usersToNotify);
        if (nbUpdatedMessages > 0) {
            return State.<Integer, String>builder().forSuccess(nbUpdatedMessages);
        } else {
            return State.<Integer, String>builder().forSuccess();
        }
    }
}