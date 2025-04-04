package com.sublime.whatsappclone.infrastructure.primary.conversation;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import lombok.Builder;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Builder
public record RestUserForConversation(String lastName,
                                      String firstName,
                                      UUID publicId,
                                      String imageUrl,
                                      Instant lastSeen) {

    public static RestUserForConversation from(User user) {
        RestUserForConversationBuilder builder = RestUserForConversation.builder();

        if (user.getImageUrl() != null) {
            builder.imageUrl(user.getImageUrl().value());
        }

        return builder
                .firstName(user.getFirstname().value())
                .lastName(user.getLastName().value())
                .publicId(user.getUserPublicId().value())
                .lastSeen(user.getLastSeen())
                .build();
    }

    public static List<RestUserForConversation> from(Set<User> users) {
        return users.stream().map(RestUserForConversation::from).toList();
    }
}
