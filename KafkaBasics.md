## Kafka basics

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
