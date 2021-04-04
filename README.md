# JfxBridge

This Tomcat-based micro-service takes an SQL query as a GET parameter, runs it on an Informix database
through the Informix JDBC driver, and returns the results as a CSV formatted plain text response.

Compiling the main class:
```sh
javac -cp /usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/webapps/jfxbridge/WEB-INF/lib/ifxjdbc-6.0.0.jar -d /usr/local/tomcat/webapps/jfxbridge/WEB-INF/classes webapps/jfxbridge/src/MainServlet.java
```