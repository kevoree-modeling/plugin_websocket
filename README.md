# KMF Storage Plugin: Websocket Client & Server

This project contains two KMF Plugins:
- **WebSocketGateway**: acts as a server to another KMF Storage plugin such as LevelDB or RocksDB.
In a nutshell, this plugin allows to open a remote access on any storage through a bi-bidirectional WebSocket persistent connection.
- **WebSocketClientPlugin**: allows to configure a model to load its elements through a WebSocket connection, and therefore through a gateway (client/server architecture).
The assembly of both plugins allows to seamlessly work on a remote storage as a local one.
Additionally the WebSocket Client plugin is delivered for Java and JavaScript platform, both embedded in the same Jar dependency.

## Last versions:

- 4.27.0 compatible with KMF framework 4.27.x

## Changelog

- 4.27.0 use Undertow in version 1.3.6

## Dependency

Simply add the following dependency to your maven project:

```java
<dependency>
    <groupId>org.kevoree.modeling.plugin</groupId>
    <artifactId>websocket</artifactId>
    <version>REPLACE_BY_LAST_VERSION</version>
</dependency>
```

## Gateway usage (Java only)

First, man has to instantiate another storage using a plugin such as levelDB.
Considering the name of this storage **localStorage***, the following code snippet expose this storage on a WebSocket port 8080.
Please change the port according to your need.

```java
import org.kevoree.modeling.plugin.WebSocketGateway;

WebSocketGateway.expose(localStorage, 8080).start();
```

## WebSocketClientPlugin usage (Java)


## WebSocketClientPlugin usage (JS)



## More info

To have more information about KMF distributed model usage, please visit the following tutorial step:

https://github.com/kevoree-modeling/tutorial/tree/master/step6_distribution