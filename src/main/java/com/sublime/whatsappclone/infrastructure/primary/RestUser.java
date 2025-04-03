package com.sublime.whatsappclone.infrastructure.primary;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import lombok.Builder;

import java.util.Set;
import java.util.UUID;

@Builder
public record RestUser(UUID publicId,
                       String firstName,
                       String lastName,
                       String email,
                       String imageUrl,
                       Set<RestAuthority> authorities) {

    static RestUser from(User user) {
        RestUserBuilder builder = RestUser.builder();

        if (user.getImageUrl() != null) {
            builder.imageUrl(user.getImageUrl().value());
        }

        return builder
                .email(user.getEmail().value())
                .firstName(user.getFirstname().value())
                .lastName(user.getLastName().value())
                .publicId(user.getUserPublicId().value())
                .authorities(RestAuthority.fromSet(user.getAuthorities()))
                .build();
    }
}
