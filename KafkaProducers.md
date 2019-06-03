## CLI Producers

- console producers are created via `kafka-console-producer.sh`
- The first thing we need to specify is the `--broker-list` and the `--topic`
- since we will be referencing local host Kafka a lot, `export KAFKA=127.0.0.1:9092`

### Sending our first messages
- `kafka-console-producer.sh --broker-list $KAFKA --topic first_topic`

```
>Hello world
>lo
>Another hello
>one more message :)
>^C
```
- we sent 4 messages
- we did not get any errors
- `ctrl+c` quits the console producer

### Sending messages with non default properties

- `kafka-console-producer.sh --broker-list $KAFKA --topic first_topic --producer-property acks=all`
- recall from [Kafka Basics](KafkaBasics.md)
 - `acks=all`: leader + replicas acknowledgement (no data loss)

```
>message is acked
>another acked message
>^C
```

- something you should know about the console producer
- if you launch `kafka-console-producer.sh --broker-list $KAFKA --topic new_topic`

```
>what happens if I send a message to a topic not yet created?
[2019-06-02 23:00:04,593] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 3 : {new_topic=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
>send a new message
>^C
```
- producers are able to recover from errors; and so the producer tried an waited until the leader became available
- `kafka-topics.sh --zookeeper $ZOO --list`

```
first_topic
new_topic
```
- we can see the `new_topic` was created
- let's describe as we learned in [Kafka Topics](KafkaTopics.md)
- `kafka-topics.sh --zookeeper $ZOO --topic new_topic --describe`

```
Topic:new_topic	PartitionCount:1	ReplicationFactor:1	Configs:
	Topic: new_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
```
- what we see is that the topic is created using default properties such as 1 partition and 1 replication factor
- you want higher `PartitionCount` and a higher `ReplicationFactor`
- recommended: always create a topic beforehand

### Changing global defaults

- access `vi config/server.properties`
- look for

```properties
# The default number of log partitions per topic. More partitions allow greater
# parallelism for consumption, but this will also result in more files across
# the brokers.
num.partitions=1
```

- change it to `num.partitions=3` if that is the default you'd like to apply
- now as you create a topic without specifying `partitions` 3 partitions will be assigned by default
- let's try it out
- `kafka-console-producer.sh --broker-list $KAFKA --topic new_topic2`

```
>hello world
[2019-06-02 23:14:58,168] WARN [Producer clientId=console-producer] Error while fetching metadata with correlation id 3 : {new_topic2=LEADER_NOT_AVAILABLE} (org.apache.kafka.clients.NetworkClient)
>hello again
>^C
```
- `kafka-topics.sh --zookeeper $ZOO --list`

```
first_topic
new_topic
new_topic2
```

- `kafka-topics.sh --zookeeper $ZOO --topic new_topic2 --describe`

```
Topic:new_topic2	PartitionCount:3	ReplicationFactor:1	Configs:
	Topic: new_topic2	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: new_topic2	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: new_topic2	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
```

- we can see 3 partitions were assigned by default  
