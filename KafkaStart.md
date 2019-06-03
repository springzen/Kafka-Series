## Install Kafka

- via brew

```bash
# Java 11 is supported as of Kafka 2.1.0
brew top caskroom/versions

# Java Install
brew cask install java
# if in doubt -> use Java 8
brew cask install java8


# Kafka
brew install kafka

# Test Kafka
bin/kafka-topics.sh
```
- or just download from https://kafka.apache.org/downloads and follow the instructions

## Starting Kafka

### Start Zookeeper

- first add Kafka to the system PATH
- [optional] - crate `KAFKA_HOME` to make other commands easier
```bash
## Your path may differ
export PATH="$PATH:$HOME/work/tools/kafka/kafka_2.12-2.2.0/bin"
export KAFKA_HOME="$HOME/work/tools/kafka/kafka_2.12-2.2.0"
```

- if you see a message similar to the one below, you are good to go
- the default port is `2181`
- if this is already bound, you can change it
```bash
vi $KAFKA_HOME/config/zookeeper.properties
```
- change the default to whatever works for you
```properties
# the port at which the clients will connect
clientPort=2181
```
```
[2019-06-02 20:18:24,060] INFO binding to port 0.0.0.0/0.0.0.0:2181 (org.apache.zookeeper.server.NIOServerCnxnFactory)
```
- now start Zookeeper
```bash
zookeeper-server-start.sh $KAFKA_HOME/config/zookeeper.properties
```
- while you are in `zookeeper.properties`, change the data directory which is bound to the `tmp` directory
```properties
# the directory where the snapshot is stored. Change it to a better home
dataDir=/tmp/zookeeper
```
- to ensure the new home is used, if you list the directory, directory named `version-2` should be listed
- do the same for Kafka, change the `data` directory
```bash
vi config/server.properties
```
```properties
# A comma separated list of directories under which to store log files
log.dirs=/tmp/kafka-logs
```
- a good home for data (at least in dev) is  `$KAFKA_HOME/data` ->  `$KAFKA_HOME/data/kafka` -> `$KAFKA_HOME/data/zookeeper`

### Start Kafka
- Kafka need to reference `server.properties` - this is located at `$KAFKA_HOME/config`
```bash
kafka-server-start.sh config/server.properties
```
- what success looks like -> look for `[KafkaServer id=0]` in the console log
```
[2019-06-02 20:50:33,289] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)
```
- if you list `data/kafka`, you will notice new files created -> **it's working**
