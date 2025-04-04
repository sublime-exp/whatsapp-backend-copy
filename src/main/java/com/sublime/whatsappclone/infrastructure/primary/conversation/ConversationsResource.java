package com.sublime.whatsappclone.infrastructure.primary.conversation;

import com.sublime.whatsappclone.messaging.application.ConversationApplicationService;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Conversation;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.ConversationToCreate;
import com.sublime.whatsappclone.messaging.domain.message.vo.ConversationPublicId;
import com.sublime.whatsappclone.shared.service.State;
import com.sublime.whatsappclone.shared.service.StatusNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
@RequiredArgsConstructor
public class ConversationsResource {

    private final ConversationApplicationService service;


    @GetMapping
    ResponseEntity<List<RestConversation>> getAll(Pageable pageable) {
        List<RestConversation> restConversations = service.getAllByConnectedUser(pageable)
                .stream()
                .map(RestConversation::from)
                .toList();

        return ResponseEntity.ok(restConversations);
    }

    @PostMapping
    ResponseEntity<RestConversation> create(@RequestBody RestConversationToCreate restConversationToCreate) {
        ConversationToCreate newConversation = RestConversationToCreate.toDomain(restConversationToCreate);
        State<Conversation, String> conversationState = service.create(newConversation);

        if (conversationState.getStatus().equals(StatusNotification.OK)) {
            RestConversation restConversation = RestConversation.from(conversationState.getValue());
            return ResponseEntity.ok(restConversation);
        } else {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "Not allowed to create conversation");
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @DeleteMapping
    ResponseEntity<UUID> delete(@RequestParam UUID publicId) {
        State<ConversationPublicId, String> restConversations = service.delete(new ConversationPublicId(publicId));
        if (restConversations.getStatus().equals(StatusNotification.OK)) {
            return ResponseEntity.ok(publicId);
        } else {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "Not allowed to delete conversation");
            return ResponseEntity.of(problemDetail).build();
        }
    }

    @GetMapping("/get-one-by-public-id")
    ResponseEntity<RestConversation> getOneByPublicId(@RequestParam UUID conversationId) {
        Optional<RestConversation> restConversation = service.getOneByConversationId(new ConversationPublicId(conversationId))
                .map(RestConversation::from);
        if (restConversation.isPresent()) {
            return ResponseEntity.ok(restConversation.get());
        } else {
            ProblemDetail problemDetail = ProblemDetail
                    .forStatusAndDetail(HttpStatus.BAD_REQUEST,
                            "Not able to find this conversation");
            return ResponseEntity.of(problemDetail).build();
        }
    }
}