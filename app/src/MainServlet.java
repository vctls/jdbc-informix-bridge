
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.DriverManager;

import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

import java.sql.*;
import com.informix.jdbc.*;

@WebServlet("/query")
public class MainServlet extends HttpServlet {

   @Override
   public void doGet(HttpServletRequest request, HttpServletResponse response) 
   throws IOException, ServletException {

      response.setContentType("text/plain");
      response.setCharacterEncoding("UTF-8");
      PrintWriter out = response.getWriter();

      String sql = request.getParameter("sql");

      if (sql == null || sql.equals("")) {
         sql = "SELECT CURRENT FROM systables WHERE tabid=1;";
      }

      Connection cnx = null;

      String host = System.getenv("INFORMIX_HOST");
      String port = System.getenv("INFORMIX_PORT");
      String db = System.getenv("INFORMIX_DB");
      String srv = System.getenv("INFORMIX_SERVER");

      String user = System.getenv("INFORMIX_USER");
      String pwd = System.getenv("INFORMIX_PASSWORD");

      String url = String.format("jdbc:informix-sqli://%s:%s/%s:informixserver=%s;", host, port, db, srv);

      try {
         // Register Informix driver.
         // https://www.ibm.com/docs/en/informix-servers/12.1.0?topic=database-load-informix-jdbc-driver
         Class.forName("com.informix.jdbc.IfxDriver");

         cnx = DriverManager.getConnection(url, user, pwd);
         // Enforce read-only connection.
         cnx.setReadOnly(true);

         Statement stm = cnx.createStatement();
         stm.execute(sql);

         ResultSet rs = stm.getResultSet();
         ResultSetMetaData meta = rs.getMetaData();
         int colCount = meta.getColumnCount();

         // Print headers.
         for (var i = 1; i <= colCount; i++) {
            out.print("\"");
            out.print(meta.getColumnName(i));
            out.print("\"");
            if (i != colCount) {
               out.append(";");
            }
         }
         out.println();

         String strValue;

         // Print values.
         while (rs.next()) {
            for (int col = 1; col <= colCount; col++) {
               Object value = rs.getObject(col);
               // Only print separators if column is not null.
               // This makes it possible to separate nulls from empty strings.
               if (value != null) {
                  out.print("\"");
                  strValue = value.toString();
                  if (strValue.contains("\"")) {
                     strValue.replace("\"", "\"\"");
                  }
                  out.print(strValue);
                  out.print("\"");
               }
               if (col != colCount) {
                  out.append(";");
               }
            }
            out.println();
         }

      } catch (Exception e) {
         out.println(e.getMessage());
      } finally {
         try {
            if (cnx != null) {
               cnx.close();
            }
         } catch (SQLException ex) {
            out.println(ex.getMessage());
         }
      }

      out.close();
   }
}