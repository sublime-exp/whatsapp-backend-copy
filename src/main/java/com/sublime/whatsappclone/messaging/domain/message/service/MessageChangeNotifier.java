package com.sublime.whatsappclone.messaging.domain.message.service;

import com.sublime.whatsappclone.infrastructure.secondary.message.ConversationViewedForNotification;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;

import java.util.List;

public interface MessageChangeNotifier {

    State<Void, String> send(Message message,
                             List<UserPublicId> usersToNotify);

    State<Void, String> delete(ConversationPublicId conversationPublicId,
                               List<UserPublicId> usersToNotify);

    State<Void, String> view(ConversationViewedForNotification conversationViewedForNotification,
                             List<UserPublicId> usersToNotify);
}
