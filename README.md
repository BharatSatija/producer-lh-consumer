# Producer-LoadHandler-Consumer

Producer->LoadHandler->Consumer is a educational simple project to refresh java concurrency and SpringBoot knowledge.

Several key consideration are :

*  LinkedBlockingQueue, 
** It blocks pool() if it is empty or offer() if it is full within a period of time (timeout).
** It has drain() to store / get an item which pass its expiration date.

* SpringBoot
** Easier to create a test for particular class.

* Improvement
** Queue which is backup by file or persistence layer to reduce memory consumption (or change it with Queue from Messaging system).
** Adding log :D

# Build
* `$ mvn package && java -jar target/producer-lh-consumer-0.0.1-SNAPSHOT.jar`
