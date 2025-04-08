package com.sublime.whatsappclone.messaging.application;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.repository.ConversationRepository;
import com.sublime.whatsappclone.messaging.domain.message.repository.MessageRepository;
import com.sublime.whatsappclone.messaging.domain.message.service.*;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.repository.UserRepository;
import com.sublime.whatsappclone.messaging.domain.user.service.UserReader;
import com.sublime.whatsappclone.shared.service.State;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ConversationApplicationService {

    private final ConversationCreator conversationCreator;
    private final ConversationReader conversationReader;
    private final ConversationDeleter conversationDeleter;
    private final UserApplicationService userApplicationService;
    private final ConversationViewed conversationViewed;


    public ConversationApplicationService(
            ConversationRepository conversationRepository,
            UserRepository userRepository,
            MessageChangeNotifier messageChangeNotifier,
            MessageRepository messageRepository,
            UserApplicationService userApplicationService) {

        UserReader userReader = new UserReader(userRepository);
        this.conversationCreator = new ConversationCreator(conversationRepository, userReader);
        this.conversationReader = new ConversationReader(conversationRepository);
        this.conversationDeleter = new ConversationDeleter(conversationRepository, messageChangeNotifier);
        this.userApplicationService = userApplicationService;
        this.conversationViewed = new ConversationViewed(messageRepository, messageChangeNotifier, userReader);
    }

    @Transactional
    public State<Conversation, String> create(ConversationToCreate conversation) {
        User authenticatedUser = userApplicationService.getAuthenticatedUser();
        return conversationCreator.create(conversation, authenticatedUser);
    }

    @Transactional(readOnly = true)
    public List<Conversation> getAllByConnectedUser(Pageable pageable) {
        User authenticatedUser = userApplicationService.getAuthenticatedUser();
        return this.conversationReader
                .getAllByUserPublicID(authenticatedUser.getUserPublicId(), pageable)
                .stream()
                .toList();
    }

    @Transactional
    public State<ConversationPublicId, String> delete(ConversationPublicId conversationPublicId) {
        User authenticatedUser = userApplicationService.getAuthenticatedUser();
        return this.conversationDeleter.deleteById(conversationPublicId, authenticatedUser);
    }

    @Transactional(readOnly = true)
    public Optional<Conversation> getOneByConversationId(ConversationPublicId conversationPublicId) {
        User authenticatedUser = userApplicationService.getAuthenticatedUser();
        return this.conversationReader.getOneByPublicIdAndUserId(conversationPublicId, authenticatedUser.getUserPublicId());
    }

    @Transactional
    public State<Integer, String> markConversationAsRead(ConversationPublicId conversationPublicId) {
        User authenticatedUser = userApplicationService.getAuthenticatedUser();
        return conversationViewed.markAsRead(conversationPublicId, authenticatedUser.getUserPublicId());
    }
}
