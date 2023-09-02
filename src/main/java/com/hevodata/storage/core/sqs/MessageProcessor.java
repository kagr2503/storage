package com.hevodata.storage.core.sqs;

import com.amazonaws.services.sqs.model.Message;
import com.hevodata.storage.model.SqsMessageEvent;

import java.util.List;

public interface MessageProcessor {
    void sendMessage(String message);

    int sizeOfQueue();

    List<SqsMessageEvent> getMessageForQueue();

    void deleteMessage(Message message);
}
