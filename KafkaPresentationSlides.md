
---
## The many faces of Kafka
Ref: [The Kafka API Battle: Producer vs Consumer vs Kafka Connect vs Kafka Streams vs KSQL !](https://medium.com/@stephane.maarek/the-kafka-api-battle-producer-vs-consumer-vs-kafka-connect-vs-kafka-streams-vs-ksql-ef584274c1e)
* **Kafka Producer API**: Applications directly producing data (ex: clickstream, logs, IoT).
* **Kafka Connect Source API**: Applications bridging between a datastore we don’t control and Kafka (ex: CDC, Postgres, MongoDB, Twitter, REST API).
* **Kafka Streams API / KSQL**: Applications wanting to consume from Kafka and produce back into Kafka, also called stream processing. Use KSQL if you think you can write your real-time job as SQL-like, use Kafka Streams API if you think you’re going to need to write complex logic for your job.
* **Kafka Consumer API**: Read a stream and perform real-time actions on it (e.g. send email…)
* **Kafka Connect Sink API**: Read a stream and store it into a target store (ex: Kafka to S3, Kafka to HDFS, Kafka to PostgreSQL, Kafka to MongoDB, etc.)
---

* At least once (default)
  * if a message from a Producer failed or it's not acknowledged, the producer resends the message. One of the safest methods for sending messages via Kafka.
  * the broker will see two messages (or one if there was a failure)
  * consumers will receive as many messages as the broker received. Deduplication must be handled.
* At most once
  * if a message from a Producer has a failure or it's not acknowledged, the producer will not resend the message
  * the broker will see one message at most (or zero if there was a failure)
  * consumers will see the messages the broker receives. The consumer will never see failed messages
  * this semantic is useful when tracking website visitors. I this case a certain level of failed messages is acceptable
* Exactly once—this is what people actually want, each message is delivered once and only once.
   * if a message from a Producer has a failure or it's not acknowledged, the producer will resend the message
   * the broker will only allow one message
   * consumers will only see the message once
   * [Kafka Exactly Once Semantics](https://hevodata.com/blog/kafka-exactly-once/)

---

## Why Kafka

* another benefit, which is common, when using a message broker is the ability to handle consumer failures (i.e. application failures)
  * the producer does not need to wait the consumer to handle the message
  * when consumers restart to process data, they should be able to pickup where they left off and not drop messages

---

## Why the need for Kafka

* replication and fault tolerance by default
* millions of messages per second
* Developers
  * Java developers can integrate with Kafka through [Spring-Kafka](https://spring.io/projects/spring-kafka)
  * Ruby developers can integrate with Kafka through [Karafka](https://github.com/karafka/karafka) or [ruby-kafka
](https://github.com/zendesk/ruby-kafka)
  * decoupling -> eliminating tightly coupled implementations
  * language agnostic messaging
  * not requiring to have all things to be running at the same time
  * the interface to data is done via Kafka and not via APIs and databases
* For the organization
  * deal with large datasets by moving away from batch jobs
  * ability  to run on premise or in the cloud
  * runs on the JVM (platform independence)
  * runs on inexpensive hardware
  * can run on a single node cluster

---

Kafka is not the same as other brokers
