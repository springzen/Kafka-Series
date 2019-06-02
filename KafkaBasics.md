## Kafka basics
- based on Stephane Maarek: Apache Kafka Series - Learn Apache Kafka for Beginners

### Topics, partitions and offsets

#### Topics
- similar to a table in a database (without al the constraints)
- you can have as many topics as you'd like
- a topic is identified by its name
- topics are split into partitions

#### Partitions
- each partition is ordered
- each message within a partition gets an incremental id, called offset

#### Offsets
- an offset is bound to a specific partition; in other words an offset is bound to a specific partition
- oder is guaranteed only within the partition but not across partitions

#### Data
- is kept for a limited time (default is one week)
- once the data is written to a partition, it cannot be changed (immutability)
- data is assigned randomly to a partition unless a key is provided


### Brokers

- a Kafka cluster is composed of multiple brokers (servers)
- each broker is identified by its ID (integer)
- each broker contains certain topic partitions - but not all the data
- after connecting to any broker (called a bootstrap broker), you will be connected to the entire cluster
- a good number cluster number to start with is 3; some clusters can grow in the hundreds
- to make it easy for growth, name clusters starting at 100 (example, 101, 102, 103)

#### Brokers and Topics

- example of Topic-A with 3 partitions
- example of Topic-B with 2 partitions

| Broker 101          |      Broker 102     |          Broker 103 |
|---------------------|:-------------------:|--------------------:|
| Topic-A Partition-0 | Topic-A Partition 2 | Topic-A Partition 1 |
| Topic-B Partition 1 | Topic-B Partition 0 |                     |

- **Note**: Data is distributed and Broker 103 does not have any Topic B data

##### Topic replication factor

- topics should have a replication factor > 1 (usually between 2 and 3)
- replication factor gold standard is 3 while two is a bit risky
- this way if a broker is down, another broker can serve the data
- example: Topic-A with two partitions and replication factor of two

| Broker 101          |         Broker 102        |                Broker 103 |
|---------------------|:-------------------------:|--------------------------:|
| Partition-0 Topic-A | Partition 1 Topic-A       | Repl: Partition 1 Topic-A |
|                     | Repl: Partition 0 Topic-A |                           |


- if we lose Broker 102
- broker 101 and 103 can still serve the data

##### Concept of Leader for a Partition

- only one broker can be a leader for a given partition
- only that leader can receive and serve data for a partition
- the other brokers will synchronize the data
- therefore, each partition has one leader and multiple ISR (in-sync replica)

| Broker 101                     |            Broker 102           |                      Broker 103 |
|--------------------------------|:-------------------------------:|--------------------------------:|
| * Partition-0 Topic-A (leader) | * Partition 1 Topic-A (Leader)  | Repl: Partition 1 Topic-A (ISR) |
|                                | Repl: Partition 0 Topic-A (ISR) |                                 |

### Producers

- how do we get data into Kafka - enter the producers
- producers write data to topics (which are made of topics)
- producers automatically know to which broker and partition to write to
- in case of broker failures, Producers will automatically recover

Inline-style: ![producers]: https://github.com/springzen/Kafka-Series/blob/master/Kafka%20Producers.png "Producers in action"
