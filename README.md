# JXLS + Spring Boot: Trial to reproduce NullPointerException on ExpressionEvaluatorFactoryHolder in simple demo project

for https://github.com/jxlsteam/jxls/discussions/276

## Context

Issue occurs in real project when we package our app with spring-boot-maven-plugin. 
All is well if application class is started directly in IntelliJ. 
However, if we start the packaged app from command line we get the error.

However, we would like to understand why the error occurs. 
Therefore, we tried to reproduce the situation in this simple demo project.

The error is _not_ reproducible with the other endpoints. 


## Steps to reproduce

### 1) Build

Requirements:
- jdk >= 17
- Maven


Build:
```
mvn clean install -P build-server-jar--via-spring-boot-maven-plugin
```

### 2) Start appplication from command line

```
java -jar server/target/server-*-spring-boot.jar
```


### 3) Call endpoint

Open http://localhost:8080/hello2 in web browser
or curl it

```
curl http://localhost:8080/hello2
```

Then after some sleep the following error appears in the terminal.

```java
http-nio-8080-exec-1 | ERROR | org.apache.catalina.core.ContainerBase.[Tomcat].[localhost].[/].[dispatcherServlet] | Servlet.service() for servlet [dispatcherServlet] in context with path [] threw exception [Request processing failed: java.lang.RuntimeException: java.util.concurrent.ExecutionException: java.lang.RuntimeException: Method createTransformer of org.jxls.transform.poi.PoiTransformer class thrown an exception:
null] with root cause |  |
java.lang.NullPointerException: Cannot invoke "org.jxls.expression.ExpressionEvaluatorFactory.createExpressionEvaluator(String)" because the return value of "org.jxls.util.JxlsHelper$ExpressionEvaluatorFactoryHolder.access$100()" is null
        at org.jxls.util.JxlsHelper.createExpressionEvaluator(JxlsHelper.java:316)
        at org.jxls.transform.TransformationConfig.<init>(TransformationConfig.java:17)
        at org.jxls.transform.AbstractTransformer.<init>(AbstractTransformer.java:28)
        at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:95)
        at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:85)
        at org.jxls.transform.poi.PoiTransformer.<init>(PoiTransformer.java:77)
        at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:151)
        at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:142)
        at org.jxls.transform.poi.PoiTransformer.createTransformer(PoiTransformer.java:123)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
        at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
        at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
        at java.base/java.lang.reflect.Method.invoke(Method.java:568)
        at org.jxls.util.TransformerFactory.createTransformer(TransformerFactory.java:43)
        at de.foo.writer.MyWriter.process(MyWriter.java:53)
        at com.foo.server.ProcessorService.process(ProcessorService.java:46)
        at com.foo.server.ProcessorService.lambda$processSupplyAsync$0(ProcessorService.java:24)
        at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1768)
        at java.base/java.util.concurrent.CompletableFuture$AsyncSupply.exec(CompletableFuture.java:1760)
        at java.base/java.util.concurrent.ForkJoinTask.doExec(ForkJoinTask.java:373)
        at java.base/java.util.concurrent.ForkJoinPool$WorkQueue.topLevelExec(ForkJoinPool.java:1182)
        at java.base/java.util.concurrent.ForkJoinPool.scan(ForkJoinPool.java:1655)
        at java.base/java.util.concurrent.ForkJoinPool.runWorker(ForkJoinPool.java:1622)
        at java.base/java.util.concurrent.ForkJoinWorkerThread.run(ForkJoinWorkerThread.java:165)
```



## Solution / Workarounds 

**A) Use maven-assembly-plugin for packaging** instead of spring-boot-maven-plugin.
How this can be achieved is shown in the maven profile `build-server-jar--via-maven-assembly-plugin` in this demo.

<span style="color:green">_(my current workaround)_</span>


**B) Manually set the classloader** before calling `TransformerFactory.createTransformer(...)`, and set it back afterwards,
see https://github.com/jxlsteam/jxls/discussions/276#discussioncomment-8073204

<span style="color:red">_(in my tests still sporadically failed, see endpoint hello2-other-classloader)_</span>


**C) Build and use Jxls from branch feature-3.0.** Creating the Transformer is different there.
See https://github.com/jxlsteam/jxls/discussions/276#discussioncomment-8066874

<span style="color:red">_(not tested by me yet)_</span>
