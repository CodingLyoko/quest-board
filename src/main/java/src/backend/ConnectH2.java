package src.backend;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import src.shared.Constants;

public class ConnectH2 {

    public static final String JDBC_URL = "jdbc:h2:file:./" + Constants.APP_FILENAME;

    private ConnectH2() {
    }

    public static void initDatabase() throws SQLException {

        String sql = "RUNSCRIPT FROM 'init.sql'";

        try (Connection connection = DriverManager.getConnection(JDBC_URL);
                Statement statement = connection.createStatement();) {
            statement.execute(sql);
        }
    }
}
