package org.silver9.s9_java_exchange_adapter;

import org.apache.kafka.streams.processor.api.Processor;
import org.apache.kafka.streams.processor.api.ProcessorContext;
import org.apache.kafka.streams.processor.api.Record;

public class ReadProcessor implements Processor<String, String, String, String> {

    @Override
    public void init(ProcessorContext<String, String> context) {
        Processor.super.init(context);
        System.out.println("ReadProcessor initialized!");
    }

    @Override
    public void process(Record<String, String> record) {
        System.out.println("Read from Kafka: Key: " + record.key() + " Value: " + record.value());
    }

    @Override
    public void close() {
        Processor.super.close();
    }
}
