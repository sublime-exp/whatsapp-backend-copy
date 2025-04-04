package com.sublime.whatsappclone.infrastructure.primary.message;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessageSendState;
import com.sublime.whatsappclone.messaging.domain.message.vo.MessageType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestMessage {

    private String textContent;

    private Instant sendDate;

    private MessageSendState state;

    private UUID publicId;

    private UUID conversationId;

    private MessageType type;

    private byte[] mediaContent;

    private String mimeType;

    private UUID senderId;

    public static RestMessage from(Message message) {
        RestMessageBuilder builder = RestMessage.builder()
                .textContent(message.getContent().text())
                .sendDate(message.getSentTime().date())
                .state(message.getSendState())
                .publicId(message.getPublicId().value())
                .conversationId(message.getConversationId().value())
                .type(message.getContent().type())
                .senderId(message.getSender().value());

        if (message.getContent().type() != MessageType.TEXT) {
            builder
                    .mediaContent(message.getContent().media().file())
                    .mimeType(message.getContent().media().mimeType());
        }
        return builder.build();
    }

    public static List<RestMessage> from(Set<Message> messages) {
        return messages.stream().map(RestMessage::from).toList();
    }

}
