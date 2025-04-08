package com.sublime.whatsappclone.messaging.domain.message.service;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.MessageSendNew;
import com.sublime.whatsappclone.messaging.domain.message.repository.MessageRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessagePublicId;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessageSendState;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessageSentTime;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class MessageCreator {

    private final MessageRepository messageRepository;
    private final MessageChangeNotifier messageChangeNotifier;
    private final ConversationReader conversationReader;

    public MessageCreator(MessageRepository messageRepository,
                          MessageChangeNotifier messageChangeNotifier,
                          ConversationReader conversationReader) {
        this.messageRepository = messageRepository;
        this.messageChangeNotifier = messageChangeNotifier;
        this.conversationReader = conversationReader;
    }


    public State<Message, String> create(MessageSendNew messageSendNew, User sender) {
        Message newMessage = Message.builder()
                .content(messageSendNew.messageContent())
                .publicId(new MessagePublicId(UUID.randomUUID()))
                .sendState(MessageSendState.RECEIVED)
                .sentTime(new MessageSentTime(Instant.now()))
                .conversationId(messageSendNew.conversationPublicId())
                .sender(sender.getUserPublicId())
                .build();

        State<Message, String> creationState;
        Optional<Conversation> conversationToLink = conversationReader.getOneByPublicId(messageSendNew.conversationPublicId());
        if (conversationToLink.isPresent()) {
            Message messageSaved = messageRepository.save(newMessage, sender, conversationToLink.get());
            List<UserPublicId> usersToNotify = conversationToLink.get()
                    .getMembers()
                    .stream()
                    .map(User::getUserPublicId)
                    .toList();
            messageChangeNotifier.send(newMessage, usersToNotify);
            creationState = State.<Message, String>builder().forSuccess(messageSaved);
        } else {
            creationState = State.<Message, String>builder().forError(
                    String.format("Unable to find the conversation to link the message with the " +
                            "following publicId %s", messageSendNew.conversationPublicId().value())
            );
        }
        return creationState;
    }
}
