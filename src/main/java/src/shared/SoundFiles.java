package src.shared;

public enum SoundFiles {
    LEVEL_UP_EFFECT("level_up_effect.mp3");

    private final String soundFile;

    private SoundFiles(String soundFile) {
        this.soundFile = soundFile;
    }

    @Override
    public String toString() {
        return soundFile;
    }
}
