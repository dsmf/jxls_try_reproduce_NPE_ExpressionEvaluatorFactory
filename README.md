# JXLS + Spring Boot: Trial to reproduce NullPointerException on ExpressionEvaluatorFactoryHolder in simple demo project

for https://github.com/jxlsteam/jxls/discussions/276

## Context

Issue occurs in real project when we package our app with spring-boot-maven-plugin. 
All is well if application class is started directly in IntelliJ. 
However, if we start the packaged app from command line we get the error.

```
Caused by: java.lang.reflect.InvocationTargetException: null
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:116)
	at java.base/java.lang.reflect.Method.invoke(Method.java:577)
	at org.jxls.util.TransformerFactory.createTransformer(TransformerFactory.java:43)
	... 17 common frames omitted
Caused by: java.lang.NullPointerException: Cannot invoke "org.jxls.expression.ExpressionEvaluatorFactory.createExpressionEvaluator(String)" because the return value of "org.jxls.util.JxlsHelper$ExpressionEvaluatorFactoryHolder.access$100()" is null
	at org.jxls.util.JxlsHelper.createExpressionEvaluator(JxlsHelper.java:316)
	at org.jxls.transform.TransformationConfig.<init>(TransformationConfig.java:17)
	at org.jxls.transform.AbstractTransformer.<init>(AbstractTransformer.java:28)
	at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:95)
	at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:85)
	at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:77)
	at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:151)
	at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:142)
	at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:123)
	at java.base/jdk.internal.reflect.DirectMethodHandleAccessor.invoke(DirectMethodHandleAccessor.java:104)
	... 19 common frames omitted
```

Our current workaround in the real project is to package via maven-assembly-plugin instead.
How this can be achieved is shown in the maven profile `build-server-jar--via-maven-assembly-plugin` in this demo.

However, we would like to understand why the error occurs. 
Therefore, we try to reproduce the situation in this simple demo project here.


## Current status

Not reproducible in this simple demo. 


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

