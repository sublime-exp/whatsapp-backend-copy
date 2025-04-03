package com.sublime.whatsappclone.infrastructure.primary;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.Authority;
import lombok.Builder;

import java.util.Set;
import java.util.stream.Collectors;

@Builder
public record RestAuthority(String name) {

    public static Set<RestAuthority> fromSet(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> RestAuthority.builder()
                        .name(authority.getName().name())
                        .build())
                .collect(Collectors.toSet());
    }
}
