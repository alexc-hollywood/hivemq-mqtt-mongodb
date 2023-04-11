package com.alexc.hivemqmongodbextension;

import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.interceptor.publish.PublishInboundInterceptor;
import com.hivemq.extension.sdk.api.packets.publish.ModifiablePublishPacket;
import com.hivemq.extension.sdk.api.parameter.ClientBasedInput;
import com.mongodb.client.MongoCollection;
import org.bson.Document;

import java.nio.charset.StandardCharsets;

public class MongoDbPublishInterceptor implements PublishInboundInterceptor 
{
    private final MongoCollection<Document> collection;

    public MongoDbPublishInterceptor(MongoCollection<Document> collection) 
    {
        this.collection = collection;
    }

    @Override
    public void onInboundPublish(@NotNull final ClientBasedInput input,
                                  @NotNull final ModifiablePublishPacket publishPacket) 
    {
        // Create a document to store in MongoDB
        Document document = new Document()
            .append("clientId", input.getClientInformation().getClientId())
            .append("topic", publishPacket
            .getTopic())
            .append("payload", new String(publishPacket.getPayloadAsBytes(), StandardCharsets.UTF_8))
            .append("qos", publishPacket.getQos().getQosNumber())
            .append("timestamp", System.currentTimeMillis());

        // If the message has a retain flag, store it in the document
        if (publishPacket.isRetain()) 
        {
            document.append("retain", true);
        }

        // Store the document in the MongoDB collection
        collection.insertOne(document);
    }
}
