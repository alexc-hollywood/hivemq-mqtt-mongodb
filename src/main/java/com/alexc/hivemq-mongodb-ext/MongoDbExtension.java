package com.alexc.hivemqmongodbextension;

import com.hivemq.extension.sdk.api.ExtensionMain;
import com.hivemq.extension.sdk.api.annotations.NotNull;
import com.hivemq.extension.sdk.api.parameter.ExtensionInformation;
import com.hivemq.extension.sdk.api.parameter.ExtensionStartInput;
import com.hivemq.extension.sdk.api.parameter.ExtensionStopInput;
import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import org.bson.Document;

public class MongoDbExtension implements ExtensionMain 
{
    private MongoClient mongoClient;

    @Override
    public void extensionStart(@NotNull final ExtensionStartInput extensionStartInput,
                               @NotNull final ExtensionInformation extensionInformation) 
    {
        // Read configuration from extension.xml
        final String connectionString = extensionInformation.getConfigurationValue("connectionString");
        final String collectionName = extensionInformation.getConfigurationValue("collectionName");
        
        // Connect to MongoDB
        MongoClientSettings settings = MongoClientSettings.builder()
            .applyConnectionString(new ConnectionString(connectionString))
            .build();
        mongoClient = MongoClients.create(settings);
        MongoDatabase database = mongoClient.getDatabase("your_database_name");
        MongoCollection<Document> collection = database.getCollection(collectionName);

        // Register the publish interceptor
        extensionStartInput.getServices().getInterceptorRegistry()
            .setPublishInboundInterceptorProvider(input -> new MongoDbPublishInterceptor(collection));
    }

    @Override
    public void extensionStop(@NotNull final ExtensionStopInput extensionStopInput) 
    {
        // Close MongoDB connection
        if (mongoClient != null) 
        {
            mongoClient.close();
        }
    }
}
