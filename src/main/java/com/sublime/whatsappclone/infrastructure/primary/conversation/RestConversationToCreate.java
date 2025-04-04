package com.sublime.whatsappclone.infrastructure.primary.conversation;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationName;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Builder
public record RestConversationToCreate(Set<UUID> members, String name) {

    public static ConversationToCreate toDomain(RestConversationToCreate conversation) {
        Set<UserPublicId> userUuids = conversation.members.stream()
                .map(UserPublicId::new)
                .collect(Collectors.toSet());
        return ConversationToCreate
                .builder()
                .name(new ConversationName(conversation.name()))
                .members(userUuids)
                .build();
    }
}
