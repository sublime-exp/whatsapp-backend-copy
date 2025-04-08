package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;

import java.util.List;

public record MessageIdWithUsers(ConversationViewedForNotification conversationViewedForNotification,
                                 List<UserPublicId> usersToNotify) {
}
