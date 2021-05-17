# Quarkus Hibernate ORM Oracle

## Steps to reproduce the issue:

1. Setup Oracle 12.2.0.1 database executing ```init.sql``` SQL script in the preferred Schema, that we will call from now ```<SCHEMA>``` (remember to replace ```<SCHEMA>``` with the proper name in ```init.sql``` file)

1. Execute from project root the following comands:

```
mvn clean package -Pnative -Dquarkus.native.container-build=true

docker build -f src/main/docker/Dockerfile.native -t quarkus-hibernate-orm-oracle .
```

1. In ```docker-compose.yml``` file replace the name of the schema, the connection URL, username and password with the proper values in order to connect to database

1. Execute from project root the following command:

```
docker-compose up
```

1. Try to invoke ```http://localhost:8080/test/TEST``` REST service

The expected raised exception is:

```
ERROR [io.qua.ver.htt.run.QuarkusErrorHandler] (executor-thread-1) HTTP Request to /test/TE failed, error id: 07346b28-ead0-4ed7-a3cd-c49976472a42-1: org.jboss.resteasy.spi.UnhandledException: java.lang.NullPointerException
    at org.jboss.resteasy.core.ExceptionHandler.handleApplicationException(ExceptionHandler.java:106)
    at org.jboss.resteasy.core.ExceptionHandler.handleException(ExceptionHandler.java:372)
    at org.jboss.resteasy.core.SynchronousDispatcher.writeException(SynchronousDispatcher.java:218)
    at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:519)
    at org.jboss.resteasy.core.SynchronousDispatcher.lambda$invoke$4(SynchronousDispatcher.java:261)
    at org.jboss.resteasy.core.SynchronousDispatcher.lambda$preprocess$0(SynchronousDispatcher.java:161)
    at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:364)
    at org.jboss.resteasy.core.SynchronousDispatcher.preprocess(SynchronousDispatcher.java:164)
    at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:247)
    at io.quarkus.resteasy.runtime.standalone.RequestDispatcher.service(RequestDispatcher.java:73)
    at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.dispatch(VertxRequestHandler.java:138)
    at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler.access$000(VertxRequestHandler.java:41)
    at io.quarkus.resteasy.runtime.standalone.VertxRequestHandler$1.run(VertxRequestHandler.java:93)
    at org.jboss.threads.EnhancedQueueExecutor$Task.run(EnhancedQueueExecutor.java:2415)
    at org.jboss.threads.EnhancedQueueExecutor$ThreadBody.run(EnhancedQueueExecutor.java:1452)
    at org.jboss.threads.DelegatingRunnable.run(DelegatingRunnable.java:29)
    at org.jboss.threads.ThreadLocalResettingRunnable.run(ThreadLocalResettingRunnable.java:29)
    at java.lang.Thread.run(Thread.java:834)
    at org.jboss.threads.JBossThread.run(JBossThread.java:501)
    at com.oracle.svm.core.thread.JavaThreads.threadStartRoutine(JavaThreads.java:519)
    at com.oracle.svm.core.posix.thread.PosixJavaThreads.pthreadStartRoutine(PosixJavaThreads.java:192)
Caused by: java.lang.NullPointerException
    at org.hibernate.engine.jdbc.cursor.internal.StandardRefCursorSupport.registerRefCursorParameter(StandardRefCursorSupport.java:67)
    at org.hibernate.query.procedure.internal.ProcedureParameterImpl.prepare(ProcedureParameterImpl.java:283)
    at org.hibernate.procedure.internal.ProcedureCallImpl$3.accept(ProcedureCallImpl.java:397)
    at org.hibernate.procedure.internal.ProcedureCallImpl$3.accept(ProcedureCallImpl.java:390)
    at org.hibernate.query.procedure.internal.ProcedureParameterMetadata.visitRegistrations(ProcedureParameterMetadata.java:186)
    at org.hibernate.procedure.internal.ProcedureCallImpl.buildOutputs(ProcedureCallImpl.java:389)
    at org.hibernate.procedure.internal.ProcedureCallImpl.getOutputs(ProcedureCallImpl.java:352)
    at org.hibernate.procedure.internal.ProcedureCallImpl.outputs(ProcedureCallImpl.java:632)
    at org.hibernate.procedure.internal.ProcedureCallImpl.getOutputParameterValue(ProcedureCallImpl.java:670)
    at org.acme.hibernate.orm.TestResource.get(TestResource.java:37)
    at java.lang.reflect.Method.invoke(Method.java:566)
    at org.jboss.resteasy.core.MethodInjectorImpl.invoke(MethodInjectorImpl.java:170)
    at org.jboss.resteasy.core.MethodInjectorImpl.invoke(MethodInjectorImpl.java:130)
    at org.jboss.resteasy.core.ResourceMethodInvoker.internalInvokeOnTarget(ResourceMethodInvoker.java:643)
    at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTargetAfterFilter(ResourceMethodInvoker.java:507)
    at org.jboss.resteasy.core.ResourceMethodInvoker.lambda$invokeOnTarget$2(ResourceMethodInvoker.java:457)
    at org.jboss.resteasy.core.interception.jaxrs.PreMatchContainerRequestContext.filter(PreMatchContainerRequestContext.java:364)
    at org.jboss.resteasy.core.ResourceMethodInvoker.invokeOnTarget(ResourceMethodInvoker.java:459)
    at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:419)
    at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:393)
    at org.jboss.resteasy.core.ResourceMethodInvoker.invoke(ResourceMethodInvoker.java:68)
    at org.jboss.resteasy.core.SynchronousDispatcher.invoke(SynchronousDispatcher.java:492)
... 17 more
```

To test the "no native" application execute the following commands:

```
mvn clean package

docker build -f src/main/docker/Dockerfile.jvm -t quarkus-hibernate-orm-oracle .

docker-compose up
```

Invoking the ```http://localhost:8080/test/TEST``` REST service, the expected result is:

```
[
    {
        "id": null,
        "test": "TEST TEST TEST"
    }
]
```
