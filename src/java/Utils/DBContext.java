package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DBContext: Lớp quản lý kết nối tới SQL Server
 */
public class DBContext {
    private static final String HOST = "localhost";
    private static final String PORT = "1433";
    private static final String DATABASE = "SoundStreamDB";
    private static final String USER = "sa";
    private static final String PASSWORD = "Password123@";

    public static Connection getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = String.format("jdbc:sqlserver://%s:%s;databaseName=%s;encrypt=true;trustServerCertificate=true",
                HOST, PORT, DATABASE);
        return DriverManager.getConnection(url, USER, PASSWORD);
    }
}
