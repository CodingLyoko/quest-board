package src.backend;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import src.shared.Constants;

public class ConnectH2 {

    public static final String JDBC_URL = "jdbc:h2:file:./" + Constants.APP_FILENAME;

    private ConnectH2() {
    }

    public static void executeSqlScripts() throws SQLException, IOException {

        // Get a file containing the names of the SQL scripts
        InputStream sqlScriptsStream = ConnectH2.class.getResourceAsStream("/sql/sql_scripts.txt");
        BufferedReader sqlScriptsReader = new BufferedReader(new InputStreamReader(sqlScriptsStream));

        // Attempt to connect to the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
                Statement statement = connection.createStatement();) {

            // Instantiate variables to read each of the SQL scripts
            String sqlScriptFilepath = sqlScriptsReader.readLine();
            InputStream sqlScriptStream;
            BufferedReader sqlScriptReader;
            StringBuilder sqlScriptContents = new StringBuilder();

            // Loop through each SQL script file
            while (sqlScriptFilepath != null) {

                // Empties the StringBuilder so it can read a fresh SQL script
                sqlScriptContents.setLength(0);

                // Instantiate variables to read the individual SQL script contents
                sqlScriptStream = ConnectH2.class.getResourceAsStream(sqlScriptFilepath);
                sqlScriptReader = new BufferedReader(new InputStreamReader(sqlScriptStream));

                String lineToRead;
                do {

                    // Get the next unread line of text in the SQL script file
                    lineToRead = sqlScriptReader.readLine();

                    // If there is text on the read line, store it
                    if (lineToRead != null) {
                        sqlScriptContents.append(lineToRead);
                        sqlScriptContents.append(System.getProperty("line.separator"));
                    }
                } while (lineToRead != null);

                // Add the contents of the SQL file to the SQL statement
                statement.addBatch(sqlScriptContents.toString());

                // Get the filepath to the next SQL script
                sqlScriptFilepath = sqlScriptsReader.readLine();

                sqlScriptStream.close();
                sqlScriptReader.close();
            }

            // Execute the content of all the SQL scripts
            statement.executeBatch();

            sqlScriptsStream.close();
            sqlScriptsReader.close();
        }
    }
}
