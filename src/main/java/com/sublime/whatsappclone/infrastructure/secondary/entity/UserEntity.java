package com.sublime.whatsappclone.infrastructure.secondary.entity;

import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.*;
import com.sublime.whatsappclone.shared.jpa.AbstractAuditingEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "whatsapp_user")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserEntity extends AbstractAuditingEntity<Long> {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "userSequenceGenerator")
    @SequenceGenerator(
            name = "userSequenceGenerator",
            sequenceName = "user_sequence",
            allocationSize = 1)
    @Column(name = "id")
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "image_url")
    private String imageUrl;

    @UuidGenerator
    @Column(name = "public_id", updatable = false)
    private UUID publicId;

    @Column(name = "last_seen")
    private Instant lastSeen = Instant.now();

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_authority",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "authority_name", referencedColumnName = "name")}
    )
    private Set<AuthorityEntity> authorities = new HashSet<>();

    @ManyToMany(cascade = CascadeType.DETACH)
    @JoinTable(
            name = "user_conversation",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "conversation_id", referencedColumnName = "id")}
    )
    private Set<ConversationEntity> conversations = new HashSet<>();

    public static UserEntity from(User user) {
        UserEntityBuilder builder = UserEntity.builder();

        if (user.getImageUrl() != null) {
            builder.imageUrl(user.getImageUrl().value());
        }

        if (user.getUserPublicId() != null) {
            builder.publicId(user.getUserPublicId().value());
        }

        return builder
                .authorities(AuthorityEntity.from(user.getAuthorities()))
                .email(user.getEmail().value())
                .firstName(user.getFirstname().value())
                .lastName(user.getLastName().value())
                .id(user.getDbId())
                .lastSeen(user.getLastSeen())
                .build();
    }

    public static User toDomain(UserEntity entity) {
        User.UserBuilder builder = User.builder();
        if (entity.getImageUrl() != null) {
            builder.imageUrl(new UserImageUrl(entity.getImageUrl()));
        }
        return builder
                .email(new UserEmail(entity.getEmail()))
                .lastName(new UserLastName(entity.getLastName()))
                .firstname(new UserFirstname(entity.getFirstName()))
                .authorities(AuthorityEntity.toDomain(entity.getAuthorities()))
                .userPublicId(new UserPublicId(entity.getPublicId()))
                .lastModifiedDate(entity.getLastModifiedDate())
                .createdDate(entity.getCreatedDate())
                .dbId(entity.getId())
                .lastSeen(entity.getLastSeen())
                .build();
    }

    public static Set<UserEntity> from(List<User> users) {
        return users.stream()
                .map(UserEntity::from)
                .collect(Collectors.toSet());
    }

    public static Set<User> toDomain(List<UserEntity> entities) {
        return entities.stream()
                .map(UserEntity::toDomain)
                .collect(Collectors.toSet());
    }

    public void updateFromUser(User user) {
        this.email = user.getEmail().value();
        this.firstName = user.getFirstname().value();
        this.lastName = user.getLastName().value();
        this.imageUrl = user.getImageUrl().value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(email, that.email) && Objects.equals(imageUrl, that.imageUrl) && Objects.equals(publicId, that.publicId) && Objects.equals(lastSeen, that.lastSeen);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, email, imageUrl, publicId, lastSeen);
    }

}
