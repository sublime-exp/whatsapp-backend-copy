package com.sublime.whatsappclone.infrastructure.secondary.entity;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationName;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "conversation")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "conversationSequenceGenerator")
    @SequenceGenerator(
            name = "conversationSequenceGenerator",
            sequenceName = "conversation_sequence",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @UuidGenerator
    @Column(name = "public_id", nullable = false, updatable = false)
    private UUID publicId;

    @Column(name = "name")
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "conversation")
    private Set<MessageEntity> messages = new HashSet<>();

    @ManyToMany
    @JoinTable(
            name = "user_conversation",
            joinColumns = {@JoinColumn(name = "conversation_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")}
    )
    private Set<UserEntity> users = new HashSet<>();

    public static ConversationEntity from(Conversation conversation) {
        ConversationEntityBuilder builder = ConversationEntity.builder();

        if (conversation.getDbId() != null) {
            builder.id(conversation.getDbId());
        }

        if (conversation.getConversationName() != null) {
            builder.name(conversation.getConversationName().name());
        }

        if (conversation.getConversationPublicId() != null) {
            builder.publicId(conversation.getConversationPublicId().value());
        }

        if (conversation.getMessages() != null) {
            Set<MessageEntity> messages = conversation.getMessages()
                    .stream()
                    .map(MessageEntity::from)
                    .collect(Collectors.toSet());
            builder.messages(messages);
        }


        return builder
                .users(UserEntity.from(conversation.getMembers().stream().toList()))
                .build();
    }

    public static Conversation toDomain(ConversationEntity entity) {
        Conversation.ConversationBuilder builder = Conversation.builder()
                .conversationPublicId(new ConversationPublicId(entity.getPublicId()))
                .conversationName(new ConversationName(entity.getName()))
                .members(UserEntity.toDomain(entity.getUsers().stream().toList()))
                .dbId(entity.getId());

        if (entity.getMessages() != null) {
            builder.messages(MessageEntity.toDomain(entity.getMessages()));
        }

        return builder.build();
    }

    public static ConversationEntity from(ConversationToCreate conversation) {
        ConversationEntityBuilder builder = ConversationEntity.builder();

        if (conversation.getName() != null) {
            builder.name(conversation.getName().name());
        }

        return builder
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationEntity that = (ConversationEntity) o;
        return Objects.equals(publicId, that.publicId) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(publicId, name);
    }
}
