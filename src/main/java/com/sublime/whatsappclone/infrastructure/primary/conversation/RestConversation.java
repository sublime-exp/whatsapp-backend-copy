package com.sublime.whatsappclone.infrastructure.primary.conversation;

import com.sublime.whatsappclone.infrastructure.primary.message.RestMessage;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record RestConversation(UUID publicId, String name,
                               List<RestUserForConversation> members,
                               List<RestMessage> messages) {

    public static RestConversation from(Conversation conversation){
        RestConversationBuilder builder = RestConversation.builder()
                .name(conversation.getConversationName().name())
                .publicId(conversation.getConversationPublicId().value())
                .members(RestUserForConversation.from(conversation.getMembers()));

        if (conversation.getMessages() != null) {
            builder.messages(RestMessage.from(conversation.getMessages()));
        }

        return builder.build();
    }
}
