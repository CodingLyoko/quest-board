package src.backend.controller;

import java.sql.SQLException;

import org.tinylog.Logger;

import lombok.NoArgsConstructor;
import src.backend.model.Player;
import src.backend.service.PlayerService;

@NoArgsConstructor
public class PlayerController extends ControllerTemplate {

    private PlayerService playerService = new PlayerService();

    public Player getPlayer() {

        Player result = null;

        try {
            result = playerService.getPlayer();

            Logger.info("Successfully retrieved Player data!");
        } catch (SQLException sqle) {
            Logger.error(SQL_ERROR_TEXT, sqle.getMessage());
        } catch (Throwable e) {
            Logger.error(GENERAL_ERROR_TEXT, e.getMessage());
        }

        return result;
    }
}
