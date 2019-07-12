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

#### On Mac output
# To have launchd start zookeeper now and restart at login:
#  brew services start zookeeper
# Or, if you don't want/need a background service you can just run:
#  zkServer start
# ==> kafka
# To have launchd start kafka now and restart at login:
#  brew services start kafka
# Or, if you don't want/need a background service you can just run:
#  zookeeper-server-start /usr/local/etc/kafka/zookeeper.properties & kafka-server-start /usr/local/etc/kafka/server.properties
#
# The above allows you to run Kafka in the background
#

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

## On macOS if you are installing through brew
export KAFKA_HOME=/usr/local/bin
export KAFKA_CONFIG=/usr/local/etc/kafka
```

- if you see a message similar to the one below, you are good to go
- the default port is `2181`
- if this is already bound, you can change it
```bash
vi $KAFKA_HOME/config/zookeeper.properties

# brew
vi $KAFKA_CONFIG/zookeeper.properties
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

# brew
zookeeper-server-start $KAFKA_CONFIG/zookeeper.properties
```
- while you are in `zookeeper.properties`, change the data directory which is bound to the `tmp` directory
```properties
# the directory where the snapshot is stored. Change it to a better home
dataDir=/tmp/zookeeper

# on brew installs
dataDir=/usr/local/var/lib/zookeeper
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

# brew
kafka-server-start $KAFKA_CONFIG/server.properties
```
- what success looks like -> look for `[KafkaServer id=0]` in the console log
```
[2019-06-02 20:50:33,289] INFO [KafkaServer id=0] started (kafka.server.KafkaServer)
```
- if you list `data/kafka`, you will notice new files created -> **it's working**
- on `brew` installs check `log.dirs` on vi $KAFKA_CONFIG/server.properties

### Convenience variables
```bash
export ZOO=127.0.0.1:2181
export KAFKA=127.0.0.1:9092
```
