## Steps to build your first client

1. Create a simple Maven project - this can be done in Eclipse or IntelliJ

```bash
mvn archetype:generate -DgroupId=com.springzen-DartifactId=kafka-beginners-course -DarchetypeArtifactId=maven-archetype-quickstart -DarchetypeVersion=1.4 -DinteractiveMode=false
```

2. Ensure support for at least Java 8

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>3.8.1</version>
            <configuration>
                <source>1.8</source>
                <target>1.8</target>
            </configuration>
        </plugin>
    </plugins>
</build>
```

3. Add dependencies (minimum)
```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.slf4j/slf4j-simple -->
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-simple</artifactId>
        <version>1.7.26</version>
        <scope>test</scope>
    </dependency>
    
    <!-- https://mvnrepository.com/artifact/org.apache.kafka/kafka-clients -->
    <dependency>
        <groupId>org.apache.kafka</groupId>
        <artifactId>kafka-clients</artifactId>
        <version>2.2.0</version>
    </dependency>
</dependencies>

```

4. Define a simple Producer

```java
public class ProducerDemo {

    public static void main(String[] args) {

        //
        // create producer properties
        // https://kafka.apache.org/documentation/#producerconfigs
        //
        Properties properties = new Properties();
        properties.setProperty(BOOTSTRAP_SERVERS_CONFIG, KafkaConstants.LOCAL_BOOTSTRAP_SERVERS);
        properties.setProperty(KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());

        /*
            - moved to closeable to send the message using Closeable
         */
        try (
                // create the producer
                KafkaProducer<String, String> producer = new KafkaProducer<>(properties)
        ) {

            // create a producer record
            ProducerRecord<String, String> record = new ProducerRecord<>("first_topic", "hello world");

            // send data - this is asynchronous
            producer.send(record);

            /*
                if try with resources is not used
                producer.flush();
                producer.close();
             */
        }

        /*
         * test on CLI
         * kafka-console-consumer.sh --bootstrap-server $KAFKA -topic first_topic --group my-third-app-group
         */
    }
}
```

#### Welcome to Kafka via Java

## Notes on the project

1. It uses [Project Lombok](https://projectlombok.org/)
2. Second example `ProducerDemoWithCallback` uses [Lambda syntax](https://docs.oracle.com/javase/tutorial/java/javaOO/lambdaexpressions.html)
