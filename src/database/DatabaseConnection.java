package database;

import java.sql.*;
import java.io.InputStream;
import java.util.Properties;

/**
 * DatabaseConnection - Koneksi ke MySQL
 * Aplikasi CRUD Penjualan Unit Motor
 * @author Arjun Septian Amarta
 */
public class DatabaseConnection {

    private static final String DRIVER  = "com.mysql.cj.jdbc.Driver";
    private static final String URL     = "jdbc:mysql://localhost:3306/db_dealermotor_231011402962?useSSL=false&serverTimezone=Asia/Jakarta&allowPublicKeyRetrieval=true";
    private static final String USER    = "root";
    private static final String PASSWORD = "";

    private DatabaseConnection() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName(DRIVER);
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver tidak ditemukan. Pastikan mysql-connector-java.jar ada di folder lib.\n" + e.getMessage());
        }
    }

    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        try { if (rs   != null) rs.close();   } catch (SQLException ignored) {}
        try { if (stmt != null) stmt.close();  } catch (SQLException ignored) {}
        try { if (conn != null) conn.close();  } catch (SQLException ignored) {}
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

    public static void close(Connection conn) {
        close(conn, null, null);
    }
}
