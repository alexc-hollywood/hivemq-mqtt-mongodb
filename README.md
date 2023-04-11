# HiveMQ MongoDB Extension

This extension enables HiveMQ to store incoming MQTT messages directly into a MongoDB collection. This extension intercepts incoming MQTT Publish messages and inserts them into a specified MongoDB collection.

## Prerequisites

1.  [HiveMQ](https://www.hivemq.com/downloads/) MQTT broker installed and running.
2.  [MongoDB](https://www.mongodb.com/try/download/community) installed and running or an existing MongoDB Atlas cluster.

## Build

To build the extension, follow these steps:

1.  Clone the repository or download the source code.
2.  Navigate to the project directory and build the project using your favorite build tool (Gradle or Maven). For Gradle, run `./gradlew build`. For Maven, run `mvn package`.
3.  The JAR file will be generated in the `build/libs` directory (Gradle) or `target` directory (Maven).

## Installation

1.  Create a new folder named `hivemq-mongodb-extension` inside the `<HiveMQ-home>/extensions/` directory.
    
2.  Copy the generated JAR file (e.g., `your-extension.jar`) and the `extension.xml` file from the project's `src/main/resources` folder into the newly created `hivemq-mongodb-extension` folder.
    
3.  Edit the `extension.xml` file and update the `connectionString` and `collectionName` with your MongoDB settings.
    
    xmlCopy code
    
    `<config>
        <mongoDbConfig>
            <connectionString>mongodb://username:password@your-mongodb-host:port/database</connectionString>
            <collectionName>mqtt_messages</collectionName>
        </mongoDbConfig>
    </config>` 
    
4.  Restart HiveMQ to load the extension.
    

## Testing

Use an MQTT client to publish messages to a topic on your HiveMQ broker. The messages will be stored directly into the specified MongoDB collection. You can use an MQTT client library or a tool like [MQTT.fx](https://mqttfx.jensd.de/) or [MQTT Explorer](https://mqtt-explorer.com/) to publish and subscribe to messages.

## Data Structure

The extension stores incoming MQTT messages in the MongoDB collection as documents with the following structure:

jsonCopy code

`{
    "clientId": "example-client",
    "topic": "test/topic",
    "payload": "Hello, World!",
    "qos": 1,
    "retain": false,
    "timestamp": 1620133200000
}` 

-   `clientId`: The client ID of the MQTT client that published the message.
-   `topic`: The MQTT topic to which the message was published.
-   `payload`: The payload of the MQTT message as a string.
-   `qos`: The Quality of Service level of the message (0, 1, or 2).
-   `retain`: A boolean indicating if the message has the retain flag set.
-   `timestamp`: The timestamp (in milliseconds) when the message was received by the extension.