package com.sublime.whatsappclone.infrastructure.secondary.repository;

import com.sublime.whatsappclone.infrastructure.secondary.entity.ConversationEntity;
import com.sublime.whatsappclone.infrastructure.secondary.entity.UserEntity;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.repository.ConversationRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class SpringDataConversationRepository implements ConversationRepository {

    private final JpaConversationRepository conversationRepository;

    @Override
    public Conversation save(ConversationToCreate conversation, List<User> members) {
        ConversationEntity newConversationEntity = ConversationEntity.from(conversation);
        newConversationEntity.setUsers(UserEntity.from(members));
        ConversationEntity newConversationSaved = conversationRepository.saveAndFlush(newConversationEntity);
        return ConversationEntity.toDomain(newConversationSaved);
    }

    @Override
    public Optional<Conversation> get(ConversationPublicId id) {
        return conversationRepository
                .findOneByPublicId(id.value())
                .map(ConversationEntity::toDomain);
    }

    @Override
    public Page<Conversation> getConversationByUserPublicId(UserPublicId id, Pageable pageable) {
        return conversationRepository
                .findAllByUsersPublicId(id.value(), pageable)
                .map(ConversationEntity::toDomain);
    }

    @Override
    public int deleteByPublicId(UserPublicId userPublicId, ConversationPublicId conversationPublicId) {
        return conversationRepository.deleteByUsersPublicIdAndPublicId(userPublicId.value(), conversationPublicId.value());
    }

    @Override
    public Optional<Conversation> getConversationByUsersPublicIdAndPublicId(UserPublicId userPublicId, ConversationPublicId conversationPublicId) {
        return conversationRepository
                .findOneByUsersPublicIdAndPublicId(userPublicId.value(), conversationPublicId.value())
                .map(ConversationEntity::toDomain);
    }

    @Override
    public Optional<Conversation> getConversationByUsersPublicIds(List<UserPublicId> publicIds) {
        List<UUID> userUuids = publicIds.stream().map(UserPublicId::value).toList();
        return conversationRepository
                .findOneByUsersPublicIdIn(userUuids, userUuids.size())
                .map(ConversationEntity::toDomain);
    }

    @Override
    public Optional<Conversation> getOneByPublicId(ConversationPublicId publicId) {
        return conversationRepository
                .findOneByPublicId(publicId.value())
                .map(ConversationEntity::toDomain);
    }
}
