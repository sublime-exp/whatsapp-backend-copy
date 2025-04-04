package com.sublime.whatsappclone.infrastructure.primary.user;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import lombok.Builder;

import java.util.UUID;

@Builder
public record RestSearchUser(String lastName,
                             String firstName,
                             String email,
                             UUID publicId,
                             String imageUrl) {

    public static RestSearchUser from(User user) {
        RestSearchUserBuilder builder = RestSearchUser.builder();

        if (user.getLastName() != null){
            builder.lastName(user.getLastName().value());
        }

        if (user.getFirstname() != null){
            builder.firstName(user.getFirstname().value());
        }

        if (user.getImageUrl() != null){
            builder.imageUrl(user.getImageUrl().value());
        }

        return builder
                .email(user.getEmail().value())
                .publicId(user.getUserPublicId().value())
                .build();
    }
}
