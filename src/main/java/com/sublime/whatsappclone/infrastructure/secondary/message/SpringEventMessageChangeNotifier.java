package com.sublime.whatsappclone.infrastructure.secondary.message;

import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.service.MessageChangeNotifier;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.messaging.domain.user.vo.UserPublicId;
import com.sublime.whatsappclone.shared.service.State;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringEventMessageChangeNotifier implements MessageChangeNotifier {

    @Override
    public State<Void, String> send(Message message, List<UserPublicId> usersToNotify) {
        return null;
    }

    @Override
    public State<Void, String> delete(ConversationPublicId conversationPublicId, List<UserPublicId> usersToNotify) {
        return null;
    }
}
