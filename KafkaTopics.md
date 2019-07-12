## Kafka Topics

**Note** If you've installed Kafka via a package manager, remove the `.sh` extension from all commands

### Convenience variables
```bash
export ZOO=127.0.0.1:2181
export KAFKA=127.0.0.1:9092
```

### Intro
- `kafka-topics.sh` - typing this command will display the CLI `help`
- since `kafka-topics` requires you to type `127.0.0.1:2181` as an argument make it an environment variable
- `export ZOO=127.0.0.1:2181` - now you can invoke `kafka-topics.sh $ZOO`

#### First topic

- `create` your first topic

```bash
# This will generate an error
kafka-topics.sh --zookeeper $ZOO --topic first_topic --create

# binary
kafka-topics --zookeeper $ZOO --topic first_topic --create
```
- what went wrong?
- `Missing required argument "[partitions]"`
- we must specify how many partitions we need for this topic

```bash
# This will generate an error
kafka-topics.sh --zookeeper $ZOO --topic first_topic --create --partitions 3

#binary
kafka-topics --zookeeper $ZOO --topic first_topic --create --partitions 3
```
- what went wrong?
- `Missing required argument "[replication-factor]"`
- we need to specify a replication factor

```bash
# This will generate an error
kafka-topics.sh --zookeeper $ZOO --topic first_topic --create --partitions 3 --replication-factor 2

# binary
kafka-topics --zookeeper $ZOO --topic first_topic --create --partitions 3 --replication-factor 2
```
- what went wrong?
- 'Error while executing topic command : Replication factor: 2 larger than available brokers: 1.'
- we only have one server in our `cluster` - so we cannot exceed that number - we need to use `1` as our replication factor

```bash
# # success
kafka-topics.sh --zookeeper $ZOO --topic first_topic --create --partitions 3 --replication-factor 1

# binary
kafka-topics --zookeeper $ZOO --topic first_topic --create --partitions 3 --replication-factor 1

# create this for our Java twitter code
kafka-topics.sh --zookeeper $ZOO --topic twitter_tweets --create --partitions 6 --replication-factor 1

# binary
kafka-topics --zookeeper $ZOO --topic twitter_tweets --create --partitions 6 --replication-factor 1
```
- `Created topic first_topic.`

#### List topics
- `kafka-topics.sh --zookeeper $ZOO --list`

```
first_topic
```

- describe the topic
- `kafka-topics.sh --zookeeper $ZOO --topic first_topic --describe`
- `kafka-topics --zookeeper $ZOO --topic first_topic --describe`

```
Topic:first_topic	PartitionCount:3	ReplicationFactor:1	Configs:
	Topic: first_topic	Partition: 0	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 1	Leader: 0	Replicas: 0	Isr: 0
	Topic: first_topic	Partition: 2	Leader: 0	Replicas: 0	Isr: 0
```

- a higher replication factor will produce a different result when running `--describe` on a topic

#### Delete topics
- first create a new topic to be deleted in this example
- `kafka-topics.sh --zookeeper $ZOO --topic second_topic --create --partitions 6 --replication-factor 1`
- `kafka-topics.sh --zookeeper $ZOO --list`
- binary commands
- `kafka-topics --zookeeper $ZOO --topic second_topic --create --partitions 6 --replication-factor 1`
- `kafka-topics --zookeeper $ZOO --list`

```
first_topic
second_topic
```

- `kafka-topics.sh --zookeeper $ZOO --topic second_topic --delete`
- `kafka-topics --zookeeper $ZOO --topic second_topic --delete`

```
Topic second_topic is marked for deletion.
Note: This will have no impact if delete.topic.enable is not set to true.
```

- `delete.topic.enable` is set to true by default
