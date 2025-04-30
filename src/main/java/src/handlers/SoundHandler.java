package src.handlers;

import java.io.File;

import org.tinylog.Logger;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import src.shared.SoundFiles;

public final class SoundHandler {

    private static final String FILE_PATH = "src/main/resources/sounds/";
    private static final double DEFAULT_VOLUME_LEVEL = 0.75;

    // Default sound; breaks if set to null
    private static Media soundEffect = new Media(
            new File(FILE_PATH + SoundFiles.LEVEL_UP_EFFECT.toString()).toURI().toString());

    // Used to play audio for sound effects
    private static MediaPlayer soundEffectPlayer = new MediaPlayer(soundEffect);

    private SoundHandler() {
    }

    public static void initialize() {
        soundEffectPlayer.setVolume(DEFAULT_VOLUME_LEVEL);
    }

    /**
     * Plays a specified audio file. The file must already be in the application.
     * 
     * @param soundFilename - The audio filename, defined in an Enum
     */
    public static void playSound(SoundFiles soundFilename) {

        // Update the Media in the MediaPlayer to the specified audio File
        soundEffect = new Media(new File(FILE_PATH + soundFilename.toString()).toURI().toString());
        soundEffectPlayer.play();

        // Resets the MediaPlayer so it can play another sound later after it finishes
        // playing the current sound
        soundEffectPlayer.setOnEndOfMedia(() -> soundEffectPlayer.stop());
    }

    //TODO: Implement method that interrupts any BGM playing (vs. playing a sound effect on top of it)
    // Will need multiple MediaPlayers, each responsible for a specific type of sound (e.g., effects, music, etc.)
    public static void playSoundBGMInterrupt() {

    }

    public static void setSoundEffectVolume(double newVolumeLevel) {
        Logger.info("Setting sound effect volume to {}", newVolumeLevel);
        soundEffectPlayer.setVolume(newVolumeLevel);
    }
}
