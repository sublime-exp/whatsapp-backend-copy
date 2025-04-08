package com.sublime.whatsappclone.infrastructure.primary.message;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.MessageSendNew;
import com.sublime.whatsappclone.messaging.domain.message.vo.*;
import lombok.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
@NoArgsConstructor
@Getter
@Setter
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

    public void setMediaAttachment(byte[] file, String contentType) {
        this.mediaContent = file;
        this.mimeType = contentType;
    }

    public boolean hasMedia() {
        return !type.equals(MessageType.TEXT);
    }

    public static MessageSendNew toDomain(RestMessage restMessage) {
        MessageContent.MessageContentBuilder messageContent = MessageContent.builder()
                .type(restMessage.type)
                .text(restMessage.textContent);

        if (!restMessage.type.equals(MessageType.TEXT)) {
            messageContent.media(new MessageMediaContent(restMessage.mediaContent,
                    restMessage.mimeType));
        }
        return MessageSendNew.builder()
                .messageContent(messageContent.build())
                .conversationPublicId(new ConversationPublicId(restMessage.conversationId))
                .build();
    }

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
