package com.gnx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gnx.connector.Connector;
import com.gnx.connector.Payload;
import com.gnx.connector.exception.ConnectorException;
import com.gnx.kafka.Message;
import com.gnx.model.MessagePayload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static com.gnx.connector.constants.MessageFields.MPRP_MQ_MSGTYPE;
import static com.gnx.connector.exception.ConnectorException.Reason.SEND_ERROR;

@Service
public class ConnectorService {

    private static final String PARAMDATA = "PARAMDATA";
    private final Connector connector;

    @Autowired
    public ConnectorService(Connector connector) {
        this.connector = connector;
    }

    public void send(MessagePayload messagePayload) throws ConnectorException {

        Map<String, String> message = messagePayload.getMessage();
        String topic = messagePayload.getTopic();

        Payload payload = new Payload.Builder().withMessage(new Message(message)).withFile(getFile(message)).build();
        connector.send(topic, payload);
    }

    public List<Payload> receive(String topic) throws ConnectorException {

        return connector.receive(topic);
    }

    private byte[] getFile(Map<String, String> message) throws ConnectorException {

        try {
            if (hasFile(message)) {

                ObjectMapper objectMapper = new ObjectMapper();
                String jsonString = objectMapper.writeValueAsString(message);
                return jsonString.getBytes();
            } else {
                return null;
            }
        } catch (JsonProcessingException e) {
            throw new ConnectorException(SEND_ERROR, "Error while encoding message", e);
        }
    }

    private boolean hasFile(Map<String, String> message) {
        return PARAMDATA.equals(message.get(MPRP_MQ_MSGTYPE));
    }
}
