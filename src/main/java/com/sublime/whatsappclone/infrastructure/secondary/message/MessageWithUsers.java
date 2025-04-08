package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;

import java.util.List;

public record MessageWithUsers(Message message,
                               List<UserPublicId> userToNotify) {
}