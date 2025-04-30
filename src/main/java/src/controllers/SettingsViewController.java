package src.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Slider;
import src.handlers.SoundHandler;

public class SettingsViewController extends FXMLControllerTemplate {

    @FXML
    private Slider soundEffectSlider;

    @FXML
    public void initialize() {
        soundEffectSlider.valueProperty().addListener((_, _, newValue) -> SoundHandler.setSoundEffectVolume(newValue.doubleValue()));
    }
}
