# VW Group Connector

This project provides a convenient solution for obtaining access tokens and id tokens for various applications within the Volkswagen Group ecosystem. 
Whether you're developing software for Volkswagen, Audi, Skoda. 
This connector simplifies the process of authentication and authorization.

Please note that this connector is `unofficial` and not endorsed by the Volkswagen Group. 
It is developed independently to assist developers in integrating their applications with VW Group services.

## Adding Maven Repository and Dependency

To utilize the library in your Maven project, follow these steps to add the repository and dependency to your `pom.xml`

1. Add the Maven Repository to your `pom.xml`
```
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

2. Add the Maven Dependency to your `pom.xml`

```
<dependency>
    <groupId>com.github.nicholasM95</groupId>
    <artifactId>vwgroup-connector</artifactId>
    <version>1.0.2</version>
</dependency>
```

## Get Tokens

This code snippet demonstrates how to use the ConnectorService to obtain authentication tokens using an email and
password.
Replace `email` and `password` with your actual credentials.
The `Client.CONNECT` parameter specifies the application.
Make sure to handle the obtained tokens securely for further usage within your application.

```
ConnectorService connectorService = new ConnectorService();
Tokens tokens = connectorService.getTokens(Client.CONNECT, "email", "password");
```

## Available applications

The following applications are available, choose the correct client for your implementation.

```
Client.CONNECT
Client.SKODA
Client.SMARTLINK
Client.VWG
```
