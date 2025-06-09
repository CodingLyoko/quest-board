package src.backend;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import src.shared.Constants;

public class ConnectH2 {

    public static final String JDBC_URL = "jdbc:h2:file:./" + Constants.APP_FILENAME;

    private ConnectH2() {
    }

    public static void executeSqlScripts() throws SQLException, IOException, URISyntaxException {

        // Get the filenames for each SQL script
        File sqlDirectory = new File(ConnectH2.class.getResource("/sql").toURI());
        File[] listOfSqlFiles = sqlDirectory.listFiles();

        // Attempt to connect to the database
        try (Connection connection = DriverManager.getConnection(JDBC_URL);
                Statement statement = connection.createStatement();) {

            // Holds the content of the current SQL script
            StringBuilder sqlScriptContents = new StringBuilder();

            // Begin getting the contents of each SQL script
            for (File sqlScript : listOfSqlFiles) {

                // Gets the filepath for the current SQL script
                String sqlScriptFilepath = "/sql/" + sqlScript.getName();

                // Empties the StringBuilder so it can read a fresh SQL script
                sqlScriptContents.setLength(0);

                // Instantiate variables to read each of the SQL scripts
                InputStream sqlScriptStream = ConnectH2.class.getResourceAsStream(sqlScriptFilepath);
                BufferedReader sqlScriptReader = new BufferedReader(new InputStreamReader(sqlScriptStream));

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

                sqlScriptStream.close();
                sqlScriptReader.close();
            }

            // Execute the content of all the SQL scripts
            statement.executeBatch();
        }
    }
}
