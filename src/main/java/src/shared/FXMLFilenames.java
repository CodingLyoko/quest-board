package src.shared;

public enum FXMLFilenames {
    QUEST_VIEW("quest_view"),
    ADD_QUEST("add_quest"),
    SETTINGS_VIEW("settings_view"),
    LEVEL_UP_POPUP("level_up_popup");
    

    private final String fxmlFilename;

    private FXMLFilenames(String fxmlFilename) {
        this.fxmlFilename = fxmlFilename;
    }

    @Override
    public String toString() {
        return fxmlFilename;
    }
}
