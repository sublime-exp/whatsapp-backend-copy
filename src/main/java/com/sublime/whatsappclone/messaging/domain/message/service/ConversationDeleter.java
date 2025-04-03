package com.sublime.whatsappclone.messaging.domain.message.service;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.repository.ConversationRepository;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.aggregate.User;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;

import java.util.List;
import java.util.Optional;

public class ConversationDeleter {

    private final ConversationRepository repository;

    private final MessageChangeNotifier messageChangeNotifier;

    public ConversationDeleter(ConversationRepository repository, MessageChangeNotifier messageChangeNotifier) {
        this.repository = repository;
        this.messageChangeNotifier = messageChangeNotifier;
    }

    public State<ConversationPublicId, String> deleteById(ConversationPublicId conversationId,
                                                          User connectedUser) {
        State<ConversationPublicId, String> stateResult;
        Optional<Conversation> conversationToDeleteOpt = this.repository
                .getConversationByUsersPublicIdAndPublicId(connectedUser.getUserPublicId(), conversationId);
        if (conversationToDeleteOpt.isPresent()) {
            this.repository.deleteByPublicId(connectedUser.getUserPublicId(), conversationId);
            List<UserPublicId> publicIds = conversationToDeleteOpt.get()
                    .getMembers().stream()
                    .map(User::getUserPublicId)
                    .toList();
            messageChangeNotifier.delete(conversationId, publicIds);
            stateResult = State.<ConversationPublicId, String>builder()
                    .forSuccess(conversationId);
        } else {
            stateResult = State.<ConversationPublicId, String>builder()
                    .forError("This conversation doesn't belong to this user or doesn't exists");
        }

        return stateResult;
    }
}
