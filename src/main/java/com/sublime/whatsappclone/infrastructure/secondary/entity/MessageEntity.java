package com.sublime.whatsappclone.infrastructure.secondary.entity;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.vo.*;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "message")
public class MessageEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "messageSequenceGenerator")
    @SequenceGenerator(
            name = "messageSequenceGenerator",
            sequenceName = "message_sequence",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "public_id", nullable = false, updatable = false)
    private UUID publicId;

    @Column(name = "send_time")
    private Instant sendTime;

    @Column(name = "text", nullable = false)
    private String text;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private MessageType type;

    @Enumerated(EnumType.STRING)
    @Column(name = "send_state")
    private MessageSendState sendState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_fk_sender", nullable = false)
    private UserEntity sender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conversation_fk", nullable = false)
    private ConversationEntity conversation;

    @OneToOne
    @JoinColumn(name = "message_binary_content_fk", referencedColumnName = "id")
    private MessageContentBinaryEntity contentBinary;

    public static MessageEntity from(Message message) {
        MessageEntityBuilder builder = MessageEntity.builder();

        if (message.getContent().type().equals(MessageType.TEXT)) {
            builder.text(message.getContent().text());
        } else {
            builder.contentBinary(MessageContentBinaryEntity.from(message.getContent()));
            if (message.getContent().text() != null) {
                builder.text(message.getContent().text());

            }
        }

        UserEntity user = UserEntity.builder()
                .publicId(message.getSender().value())
                .build();

        return builder
                .type(message.getContent().type())
                .publicId(message.getPublicId().value())
                .sendTime(message.getSentTime().date())
                .sendState(message.getSendState())
                .sender(user)
                .build();
    }

    public static Message toDomain(MessageEntity entity) {
        Message.MessageBuilder builder = Message.builder()
                .conversationId(new ConversationPublicId(entity.getPublicId()))
                .sendState(entity.getSendState())
                .sentTime(new MessageSentTime(entity.getSendTime()))
                .sender(new UserPublicId(entity.getSender().getPublicId()))
                .publicId(new MessagePublicId(entity.getPublicId()));

        if (entity.getType().equals(MessageType.TEXT)) {
            MessageContent content = new MessageContent(entity.getText(), MessageType.TEXT, null);
            builder.content(content);
        } else {
            MessageMediaContent mediaContent = new MessageMediaContent(
                    entity.getContentBinary().getFile(),
                    entity.getContentBinary().getFileContentType());

            MessageContent content = new MessageContent(
                    entity.getText(),
                    entity.getType(),
                    mediaContent);
            builder.content(content);
        }

        return builder.build();
    }

    public static Set<Message> toDomain(Set<MessageEntity> entities) {
        return entities.stream()
                .map(MessageEntity::toDomain)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageEntity that = (MessageEntity) o;
        return Objects.equals(publicId, that.publicId) && Objects.equals(sendTime, that.sendTime) && Objects.equals(text, that.text) && type == that.type && sendState == that.sendState;
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicId, sendTime, text, type, sendState);
    }

}
