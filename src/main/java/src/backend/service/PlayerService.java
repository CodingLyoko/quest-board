package src.backend.service;

import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

import lombok.NoArgsConstructor;
import src.backend.model.Player;

@NoArgsConstructor
public class PlayerService extends ServiceTemplate {

    private static final String TABLE_NAME = "player";

    public Player getPlayer() throws SQLException, IllegalAccessException, InvocationTargetException,
            NoSuchMethodException, SecurityException {

        connectToDatabase();

        ResultSet resultSet = getQueryResults("SELECT * FROM " + TABLE_NAME);

        Player player;

        // Checks if there is a Player entry in the Database
        // If no entry, create one
        if (resultSet.getRow() != 0) {
            player = new Player(resultSet);
        } else {
            player = new Player();
            player.setId(UUID.randomUUID());

            saveEntry(player);
        }

        closeDatabaseConnections();

        return player;
    }
}
