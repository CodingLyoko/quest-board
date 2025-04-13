package src;

import java.io.IOException;

import org.tinylog.Logger;

import javafx.concurrent.Task;
import lombok.NoArgsConstructor;
import src.handlers.FXMLHandler;
import src.handlers.FXMLHandler.FXMLInstance;
import src.handlers.PlayerHandler;
import src.shared.FXMLFilenames;

@NoArgsConstructor
public class InitTask extends Task<Void> {

    /*
     * TASK:
     * - Retrieve Player Data
     * - Instantiate FXML files
     */
    private static final int TOTAL_NUM_TASKS = 1 + FXMLFilenames.values().length;
    private int currentTasksComplete = 0;

    @Override
    protected Void call() throws Exception {
        Logger.info("\n#################### STARTING APPLICATION ####################\n");

        PlayerHandler.getPlayerInstance();
        updateProgress(1, TOTAL_NUM_TASKS);

        initFXML();

        // Allows the User to see the progress bar at 100%
        Thread.sleep(500);

        return null; // success
    }

    /**
     * Initializes the FXML files/controllers for the app. Each FXML is tracked in
     * the FXMLFilenames enum.
     * 
     * @throws IOException
     */
    public void initFXML() throws IOException {
        for (FXMLFilenames fxmlFilename : FXMLHandler.getFXML_FILENAMES()) {
            addFXMLInstance(fxmlFilename);

            // Updates the progress bar for the loading screen
            currentTasksComplete++;
            updateProgress(currentTasksComplete, TOTAL_NUM_TASKS);
        }
    }

    /**
     * Creates an FXMLInstance for the specified FXML file, then stores in in a Map
     * (that relates FXML filenames to FXMLInstances).
     * 
     * @param fxmlFilename - the name of the FXML file used to create the
     *                     FXMLInstance
     * @throws IOException
     */
    public void addFXMLInstance(FXMLFilenames fxmlFilename) throws IOException {
        // Only create an FXMLInstance if it does not already exist
        if (!FXMLHandler.getFxmlInstances().containsKey(fxmlFilename)) {
            FXMLHandler.getFxmlInstances().put(fxmlFilename, new FXMLInstance(fxmlFilename.toString()));
        }
    }
}
