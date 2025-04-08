package com.sublime.whatsappclone.infrastructure.primary.message;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sublime.whatsappclone.messaging.application.MessageApplicationService;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.Message;
import com.sublime.whatsappclone.messaging.domain.message.aggregate.MessageSendNew;
import com.sublime.whatsappclone.shared.service.State;
import com.sublime.whatsappclone.shared.service.StatusNotification;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/messages")
@RequiredArgsConstructor
public class MessageResource {

    private final MessageApplicationService messageApplicationService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @PostMapping(value = "/send", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<RestMessage> send(@RequestPart(value = "file", required = false) MultipartFile file,
                                            @RequestPart("dto") String messageRaw) throws IOException {
        RestMessage restMessage = objectMapper.readValue(messageRaw, RestMessage.class);
        if (restMessage.hasMedia()) {
            restMessage.setMediaAttachment(file.getBytes(), file.getContentType());
        }

        MessageSendNew messageSendNew = RestMessage.toDomain(restMessage);

        State<Message, String> sendState = messageApplicationService.send(messageSendNew);
        if (sendState.getStatus().equals(StatusNotification.OK)) {
            return ResponseEntity.ok(RestMessage.from(sendState.getValue()));
        } else {
            ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, sendState.getError());
            return ResponseEntity.of(problemDetail).build();
        }
    }
}