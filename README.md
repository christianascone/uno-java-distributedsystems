uno Sistemi Distribuiti
==================


## Requirements

- Java 7
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
                "host":"dorina.cs.unibo.it",
                "leader":"true"
             },
             {
                "name":"test2",
                "id":2,
                "host":"abigaille.cs.unibo.it"
             },
             {
                "name":"test3",
                "id":3,
                "host":"marullo.cs.unibo.it"
             },
             {
                "name":"test4",
                "id":4,
                "host":"frank.cs.unibo.it"
             }
          ]
       }
    }

### Run

Starting from last host, start the following command:

    java -jar uno-*-jar-with-dependencies.jar name number port

Example:

    java -jar uno-*-jar-with-dependencies.jar test4 4 1299

Attention: in case of multiple instances on the same host, it is necessary to change port for every launch.