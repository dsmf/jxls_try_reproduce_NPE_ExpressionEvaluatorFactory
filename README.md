Trial to reproduce `java.lang.NullPointerException: Cannot invoke "org.jxls.expression.ExpressionEvaluatorFactory.createExpressionEvaluator(String)" because the return value of "org.jxls.util.JxlsHelper$ExpressionEvaluatorFactoryHolder.access$100()" is null`
for https://github.com/jxlsteam/jxls/discussions/276

Current status: Not reproducible


## Requirements

- jdk >= 17
- Maven


## Build

```
mvn clean package -P build-server-jar--via-spring-boot-maven-plugin
```

## Start

```
java -jar server\target\server-1.0.0-SNAPSHOT-spring-boot.jar
```

then open http://localhost:8080/hello in webbrowser
or curl it

```
curl http://localhost:8080/hello
```

