package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.infrastructure.secondary.entity.UserEntity;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserEmail;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpringDataUserRepository implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

    @Override
    public void save(User user) {
        if (user.getDbId() != null) {
            Optional<UserEntity> userToUpdateOpt = jpaUserRepository.findById(user.getDbId());
            if (userToUpdateOpt.isPresent()) {
                UserEntity userToUpdate = userToUpdateOpt.get();
                userToUpdate.updateFromUser(user);
                jpaUserRepository.saveAndFlush(userToUpdate);
            }
        } else {
            jpaUserRepository.save(UserEntity.from(user));
        }
    }

    @Override
    public Optional<User> get(UserPublicId userPublicId) {
        return Optional.empty();
    }

    @Override
    public Optional<User> getOneByEmail(UserEmail userEmail) {
        return jpaUserRepository.findByEmail(userEmail.value())
                .map(UserEntity::toDomain);
    }

    @Override
    public List<User> getByPublicIds(List<UserPublicId> publicIds) {
        return List.of();
    }

    @Override
    public Page<User> search(Pageable pageable, String query) {
        return null;
    }

    @Override
    public int updateLastSeenByPublicId(UserPublicId publicId, Instant lastSeen) {
        return 0;
    }

    @Override
    public List<User> getRecipientsByConversationIdExcludingReader(ConversationPublicId conversationId, UserPublicId readerPublicId) {
        return List.of();
    }

//    @Override
//    public List<User> getByPublicIds(Set<UserPublicId> userPublicIds) {
//        List<UUID> rawPublicIds = userPublicIds.stream().map(UserPublicId::value).toList();
//        return jpaUserRepository.findByPublicIdIn(rawPublicIds)
//                .stream()
//                .map(UserEntity::toDomain)
//                .toList();
//    }

//    @Override
//    public Page<User> search(Pageable pageable, String query) {
//        return jpaUserRepository.search(pageable, query).map(UserEntity::toDomain);
//    }

//    @Override
//    public int updateLastSeenByPublicId(UserPublicId userPublicId, Instant lastSeen) {
//        return jpaUserRepository.updateLastSeen(userPublicId.value(), lastSeen);
//    }

//    @Override
//    public List<User> getRecipientByConversationIdExcludingReader(ConversationPublicId conversationPublicId, UserPublicId readerPublicId) {
//        return jpaUserRepository.findByConversationsPublicIdAndPublicIdIsNot(conversationPublicId.value(), readerPublicId.value())
//                .stream()
//                .map(UserEntity::toDomain)
//                .toList();
//    }

//    @Override
//    public Optional<User> getOneByPublicId(UserPublicId userPublicId) {
//        return jpaUserRepository
//                .findOneByPublicId(userPublicId.value())
//                .map(UserEntity::toDomain);
//    }
}
