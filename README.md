# JfxBridge

This Tomcat-based micro-service takes an SQL query as a GET parameter, runs it on an Informix database
through the Informix JDBC driver, and returns the results as a CSV formatted plain text response.

Tomcat is configured to serve the app at the http root on port 80.

Use the get parameter `?sql=` to pass SQL queries.

NB: the database connection is configured as read-only.

Compiling the main class from the host:
```sh
docker-compose exec tomcat javac \
-cp /usr/local/tomcat/lib/servlet-api.jar:/usr/local/tomcat/webapps/jfxbridge/WEB-INF/lib/ifxjdbc-6.0.0.jar \
-d /usr/local/tomcat/webapps/jfxbridge/WEB-INF/classes \
webapps/jfxbridge/src/MainServlet.java
```