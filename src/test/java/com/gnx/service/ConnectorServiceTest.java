package com.gnx.service;

import com.gnx.connector.Connector;
import com.gnx.connector.Payload;
import com.gnx.connector.exception.ConnectorException;
import com.gnx.model.MessagePayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.Mock;
import org.mockito.InjectMocks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.gnx.connector.exception.ConnectorException.Reason.RECEIVE_ERROR;
import static com.gnx.connector.exception.ConnectorException.Reason.SEND_ERROR;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class ConnectorServiceTest {

    @Mock
    private Connector connector;

    @InjectMocks
    private ConnectorService connectorService;

    private MessagePayload testMessagePayload;
    private final String TEST_TOPIC = "Q_ADDRESSING";

    @BeforeEach
    void setUp() {

        Map<String, String> message = new HashMap<>();
        message.put("MPRP.MQ.MSGTYPE", "PARAMDATA");
        message.put("MPRP.BASEID", "BASEID");
        message.put("MPRP.DATA.ORIGINALNAME", "DATA_ORIGINAL_NAME");

        testMessagePayload = new MessagePayload(message, TEST_TOPIC);
    }

    @Test
    void testSendMessageSentSuccessfully() throws ConnectorException {
        // when
        connectorService.send(testMessagePayload);

        // then
        verify(connector, times(1)).send(any(String.class), any(Payload.class));
    }

    @Test
    void testSendThrowsConnectorException() throws ConnectorException {

        // given
        doThrow(new ConnectorException(SEND_ERROR)).when(connector).send(any(String.class), any(Payload.class));

        // when & then
        assertThrows(ConnectorException.class, () -> connectorService.send(testMessagePayload));
    }

    @Test
    void testReceiveMessagesReceivedSuccessfully() throws ConnectorException {

        // given
        List<Payload> payloads = List.of(mock(Payload.class));
        when(connector.receive(TEST_TOPIC)).thenReturn(payloads);

        // when
        List<Payload> result = connectorService.receive(TEST_TOPIC);

        // then
        assertEquals(payloads, result);
        verify(connector, times(1)).receive(TEST_TOPIC);
    }

    @Test
    void testReceiveThrowsConnectorException() throws ConnectorException {
        // given
        when(connector.receive(TEST_TOPIC)).thenThrow(new ConnectorException(RECEIVE_ERROR));

        // when & then
        assertThrows(ConnectorException.class, () -> connectorService.receive(TEST_TOPIC));
    }

    @Test
    void testSendFileIsGeneratedWhenMessageHasFile() throws ConnectorException {
        // when
        connectorService.send(testMessagePayload);

        // then
        verify(connector, times(1)).send(eq(TEST_TOPIC), any(Payload.class));
    }

    @Test
    void testSendNoFileIsGeneratedWhenMessageHasNoFile() throws ConnectorException {
        // given
        Map<String, String> messageWithoutFile = new HashMap<>();
        messageWithoutFile.put("MPRP.MQ.MSGTYPE", "OTHER_TYPE");
        MessagePayload payloadWithoutFile = new MessagePayload(messageWithoutFile, TEST_TOPIC);

        // when
        connectorService.send(payloadWithoutFile);

        // then
        verify(connector, times(1)).send(eq(TEST_TOPIC), any(Payload.class));
    }
}