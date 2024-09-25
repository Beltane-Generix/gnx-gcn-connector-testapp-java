package com.gnx.controller;

import com.gnx.connector.Payload;
import com.gnx.connector.exception.ConnectorException;
import com.gnx.model.MessagePayload;
import com.gnx.service.ConnectorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Collections.singletonMap;

@RestController
@RequestMapping("/connector")
@RequiredArgsConstructor
public class ConnectorController {

    private final ConnectorService connectorService;

    @PostMapping("/send")
    public ResponseEntity<?> send(@RequestBody MessagePayload message) {

        try {
            connectorService.send(message);
            return ResponseEntity.ok(singletonMap(RESULT_KEY, "Message sent successfully"));
        } catch (ConnectorException e) {
            return ResponseEntity.badRequest().body(singletonMap(ERROR_KEY, "Error sending message: " + e.getMessage()));
        }
    }

    @PostMapping("/receive")
    public ResponseEntity<?> receive(@RequestParam String topic) {

        try {
            List<Payload> payloads = connectorService.receive(topic);
            return ResponseEntity.ok(payloads);
        } catch (ConnectorException e) {
            return ResponseEntity.badRequest().body(singletonMap(ERROR_KEY, "Error receiving message: " + e.getMessage()));
        }
    }

    private final String RESULT_KEY = "result";
    private final String ERROR_KEY = "error";
}
