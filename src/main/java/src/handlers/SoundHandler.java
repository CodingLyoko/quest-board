package src.handlers;

import java.io.File;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import src.shared.SoundFiles;

public final class SoundHandler {

    private static final String FILE_PATH = "src/main/resources/sounds/";

    // Default sound; breaks if set to null
    private static Media sound = new Media(
            new File(FILE_PATH + SoundFiles.LEVEL_UP_EFFECT.toString()).toURI().toString());

    // Used to play all audio
    private static MediaPlayer mediaPlayer = new MediaPlayer(sound);

    private SoundHandler() {
    }

    /**
     * Plays a specified audio file. The file must already be in the application.
     * 
     * @param soundFilename - The audio filename, defined in an Enum
     */
    public static void playSound(SoundFiles soundFilename) {

        // Update the Media in the MediaPlayer to the specified audio File
        sound = new Media(new File(FILE_PATH + soundFilename.toString()).toURI().toString());
        mediaPlayer.play();

        // Resets the MediaPlayer so it can play another sound later after it finishes
        // playing the current sound
        mediaPlayer.setOnEndOfMedia(() -> mediaPlayer.stop());
    }
}
