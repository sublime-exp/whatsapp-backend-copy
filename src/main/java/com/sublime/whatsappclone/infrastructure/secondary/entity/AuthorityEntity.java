package com.sublime.whatsappclone.infrastructure.secondary.entity;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.Authority;
import com.sublime.whatsappclone.messaging.domain.user.vo.AuthorityName;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "authority")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class AuthorityEntity {

    private static final long serialVersionUID = 1L;

    @NotNull
    @Size(max = 50)
    @Id
    @Column(length = 50)
    private String name;

    public static Set<AuthorityEntity> from(Set<Authority> authorities) {
        return authorities
                .stream()
                .map(authority -> AuthorityEntity.builder()
                        .name(authority.getName().name())
                        .build())
                .collect(Collectors.toSet());
    }

    public static Set<Authority> toDomain(Set<AuthorityEntity> authorities) {
        return authorities
                .stream()
                .map(authority -> Authority.builder()
                        .name(new AuthorityName(authority.name))
                        .build())
                .collect(Collectors.toSet());
    }
}
