package com.gnx.model;

import lombok.*;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessagePayload {

    private Map<String, String> message;
    private String topic;
}
