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
        
        if (playerInstance.getExperiencePoints() >= playerInstance.getExpToNextLevel()) {
            playerInstance.setExperiencePoints(playerInstance.getExperiencePoints() % playerInstance.getExpToNextLevel());
        }

        Logger.info("Successfully granted " + expToGain + " Experience Points to the Player.");

        updatePlayer();
    }

    public static double getProgressToNextLevel() {
        return ((double) playerInstance.getExperiencePoints() / playerInstance.getExpToNextLevel());
    }

    /**
     * Call this method whenever a change made to the Player should persist after
     * closing the application
     */
    private static void updatePlayer() {
        playerController.updateEntry(playerInstance);
    }
}
