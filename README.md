UNO Java - Distributed Systems
==================


## Requirements

- Java >= 7
- Maven


## Getting Started

### Compile

Move inside project directory and run:
```
mvn install
```

### Configuration

Set hosts in `config.json` and a name for each host.
Choose a leader and set `"leader":"true"`.

Example:

    {
       "UnoConfig":{
          "nodes":[
             {
                "name":"test1",
                "id":1,
                "host":"localhost",
                "leader":"true"
             },
             {
                "name":"test2",
                "id":2,
                "host":"localhost"
             },
             {
                "name":"test3",
                "id":3,
                "host":"localhost"
             },
             {
                "name":"test4",
                "id":4,
                "host":"localhost"
             }
          ]
       }
    }

### Run

Place `java.policy` in the same directory of executable jar.
Now, starting on every non-leader host and finally with leader host, execute:

    java -jar uno-*-jar-with-dependencies.jar name playernumber port

In case of using the same host for every player, commands must be executed changing port:

Example:

    java -jar uno-*-jar-with-dependencies.jar test2 2 1099
    java -jar uno-*-jar-with-dependencies.jar test3 3 1199
    java -jar uno-*-jar-with-dependencies.jar test4 4 1299
    java -jar uno-*-jar-with-dependencies.jar test1 1 1399