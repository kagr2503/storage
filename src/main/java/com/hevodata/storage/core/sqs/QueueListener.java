package com.hevodata.storage.core.sqs;

import com.hevodata.storage.core.s3.S3FileProcessor;
import com.hevodata.storage.core.sqs.SqsMessageProcessor;
import com.hevodata.storage.model.SqsMessageEvent;
import com.hevodata.storage.util.ExceptionPrint;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class QueueListener {

    private static QueueListener instance;

    private static final int THREAD_COUNT = 5;
    private static final long SLEEP_INTERVAL_MS = 5 * 1000; // 5 sec

    public static QueueListener getInstance(){
        if(instance == null) instance = new QueueListener();
        return instance;
    }
    public void start() {
        ExecutorService threadPool = Executors.newFixedThreadPool(THREAD_COUNT);

        for (int i = 0; i < THREAD_COUNT; i++) {
            threadPool.submit(this::consumeMessages);
        }

        // Shutdown the thread pool gracefully when needed
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            threadPool.shutdown();
        }));
    }

    private void consumeMessages() {
        while (true) {
            try {
                List<SqsMessageEvent> messages = SqsMessageProcessor.getInstance().getMessageForQueue();

                if (messages.isEmpty()) {
                    Thread.sleep(SLEEP_INTERVAL_MS);
                } else {
                    processMessages(messages);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    private void processMessages(List<SqsMessageEvent> messages) {
        for (SqsMessageEvent message : messages) {
            try {
                S3FileProcessor.processS3Files(message);
            } catch (Exception e) {
                ExceptionPrint.perform(e);
            }
        }
    }
}
