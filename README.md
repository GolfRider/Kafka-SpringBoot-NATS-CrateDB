### Simple tutorial on integrating Kafka, SpringBoot, NATS & Crate DB

#### This example illustrates a simple integration across these four technologies:
         1. Kafka  Messaging Server - Distributed (Durable) Messaging Server [http://kafka.apache.org/]
         2. Spring Boot - Java Microframework [http://projects.spring.io/spring-boot/]
         3. NATS Messaging Server  - High Performance Messaging Serverr [http://nats.io/]
         4. Crate DB  - Scalable DB based on ElasticSearch [https://crate.io/]
         
#### Application Flow:
         [Rest Request (Browser/Postman/Rest Client)] 
                 => [Rest Controller (Spring Boot)]
                 => [NATS Publisher sends pay-load to NATS messaging server (Spring Bean)]
                 => [NATS Subscriber processes the message (Spring Bean) & invokes Kafka Publisher (Spring Bean) ]
                 => [Kafka Publisher publishes the message to a Kafka Topic (Spring Bean)
                 => [Kafka Consumer then persists the message to CRATE database (Spring Bean)]

#### Some thougts : 
         1. Kafka serves as a durable-cum-distributed messaging server
         2. Spring Boot serves as Java -microframework  for Restful APIs.
         3. NATS serves as a high-performance in-memory/distributed messaging server
                  - Can be used for loadbalancing
                  - Can be used for Service Discovery
                  - Auto-prunes unresponsive/slow clients
                  - Very,very fast - IO 
                  - Tiny footprint, suitable for microservices-style artchitectures
                  - And lots more, please check their website
                  
         4. Crate DB - Scalable database based on elastic search
                     - Brings SQL style interface to NoSQL storage engine
                     - Scalable & Distributed
                     - Can be used in places where Cassandra & Elastic search were used
                     - Very new, stability needs to be proven yet
                     - Looks promising 
