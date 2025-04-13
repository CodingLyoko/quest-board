package src.handlers;

import org.tinylog.Logger;

import src.backend.controller.PlayerController;
import src.backend.model.Player;

public final class PlayerHandler {

    private PlayerHandler() {
    }

    private static final PlayerController playerController = new PlayerController();
    private static Player playerInstance;

    public static Player getPlayerInstance() {
        if (playerInstance == null) {
            playerInstance = playerController.getPlayer();
        }

        return playerInstance;
    }

    public static void gainExp(int expToGain) {
        playerInstance.setExperiencePoints(playerInstance.getExperiencePoints() + expToGain);
        Logger.info("Successfully granted " + expToGain + " Experience Points to the Player.");

        updatePlayer();
    }

    /**
     * Subtract from the current Action Points available
     * 
     * @param actionPointsToLose - the amount of Action Points to lose
     * @return true if there are enough Action Points to perform the action;
     *         false otherwise
     */
    /*public static Boolean expendActionPoints(int actionPointsToLose) {

        // Checks if there are enough Action Points to perform the action
        if (playerInstance.getCurrentActionPoints() >= actionPointsToLose) {
            playerInstance.setCurrentActionPoints(playerInstance.getCurrentActionPoints() - actionPointsToLose);
            Logger.info("Successfully subtracted " + actionPointsToLose + " Action Points from the Player.");

            updatePlayer();
        } else {
            Logger.warn("Insufficient Action Points to perform action.");
            WindowHandler.showAlertPopup(FXMLFilenames.INSUFFICIENT_ACTION_POINTS);

            return false;
        }

        return true;
    }*/

    /**
     * Call this method whenever a change made to the Player should persist after
     * closing the application
     */
    private static void updatePlayer() {
        playerController.updateEntry(playerInstance);
    }
}
