package src.controllers.quests;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import src.backend.controller.QuestController;
import src.backend.model.Quest;
import src.controllers.FXMLControllerTemplate;
import src.shared.DueDateTimeframe;
import src.shared.OccurrenceType;

public class AddQuestController extends FXMLControllerTemplate {

    private static final int MAX_LENGTH_QUEST_NAME = 64; // Based on DB column
    private static final int MAX_LENGTH_QUEST_DESC = 1024; // Based on DB column
    private static final int MAX_LENGTH_EXP_POINTS = 7;

    private QuestController questController = new QuestController();

    private ObservableList<OccurrenceType> occurrenceTypeValues = FXCollections.observableArrayList();

    private Quest questToSave = new Quest();

    // Tracks if the Cancel button was used t oclose the Window
    private static Boolean cancelClicked = false;

    @FXML
    private TextField questNameInput;
    @FXML
    private TextArea questDescriptionInput;
    @FXML
    private TextField experiencePointsInput;
    @FXML
    private ComboBox<OccurrenceType> occurrenceTypeInput;
    @FXML
    private DatePicker dueDateInput;

    @FXML
    public void initialize() {

        restrictInputFields();

        // Sets the values for the Occurrence Type input
        for (OccurrenceType occurrenceType : OccurrenceType.class.getEnumConstants()) {
            occurrenceTypeValues.add(occurrenceType);
        }
        occurrenceTypeInput.setItems(occurrenceTypeValues);
    }

    /**
     * Limits the type of input/max length of input for the relevant input fields.
     */
    private void restrictInputFields() {
        limitMaxCharacterLength(questNameInput, MAX_LENGTH_QUEST_NAME);
        limitMaxCharacterLength(questDescriptionInput, MAX_LENGTH_QUEST_DESC);

        limitMaxCharacterLength(experiencePointsInput, MAX_LENGTH_EXP_POINTS);
        allowOnlyIntegersInTextField(experiencePointsInput);
    }

    @FXML
    private void cancelOnClick(Event e) {
        clearInputFields();

        cancelClicked = true;

        super.closeWindowOnClick(e);
    }

    @FXML
    private void occurrenceTypeOnChange() {
        if (occurrenceTypeInput.getValue() != null && occurrenceTypeInput.getValue().equals(OccurrenceType.RECURRING)) {
            dueDateInput.setValue(null);
            dueDateInput.setDisable(true);
        } else {
            dueDateInput.setDisable(false);
        }
    }

    @FXML
    private void createQuest(Event e) {
        setQuestValues(questToSave);

        // Save the newly-created Quest to the database
        if (questToSave.getId() == null) {
            questController.saveEntry(questToSave);
        } else {
            questController.updateEntry(questToSave);
        }

        clearInputFields();

        // If this field was disabled, enable it
        dueDateInput.setDisable(false);

        // Resets the Quest used for Creation/Editing
        questToSave = new Quest();

        // Resets object to the default value
        cancelClicked = false;

        super.closeWindowOnClick(e);
    }

    /**
     * Sets the values of the created/edited Quest with the values from the input
     * fields. This function also determines which ListView the Quest will go into.
     * 
     * @param quest - the Quest being created/edited
     */
    private void setQuestValues(Quest quest) {

        // If any input fields are blank (other than Due Date), set a default value
        // Quest Name
        if (questNameInput.getText().isBlank()) {
            quest.setQuestName("Default Quest Name");
        } else {
            quest.setQuestName(questNameInput.getText());
        }

        // Quest Description
        if (questDescriptionInput.getText().isBlank()) {
            quest.setQuestDescription("Default Quest Description.");
        } else {
            quest.setQuestDescription(questDescriptionInput.getText());
        }

        // Experience Points
        if (experiencePointsInput.getText().isBlank()) {
            quest.setExperiencePoints(0);
        } else {
            quest.setExperiencePoints(Integer.parseInt(experiencePointsInput.getText()));
        }

        // Occurrence Type
        if (occurrenceTypeInput.getValue() == null) {
            quest.setOccurrenceType(OccurrenceType.ONCE);
        } else {
            quest.setOccurrenceType(occurrenceTypeInput.getValue());
        }

        // If a Due Date was selected, set that value for the Quest
        if (dueDateInput.getValue() != null) {
            // Converts the DatePicker value to a Timestamp
            quest.setDueDate(Timestamp.valueOf(dueDateInput.getValue().atStartOfDay()));

            addQuestToListView(quest);
        } else if (occurrenceTypeInput.getValue() != null
                && occurrenceTypeInput.getValue() != OccurrenceType.RECURRING) {
            quest.setDueDate(Timestamp.from(Instant.now()));
            QuestViewController.getQuestLists().get(DueDateTimeframe.DAY.toString()).getItems().add(quest);
        } else {
            quest.setDueDate(null);
            QuestViewController.getQuestLists().get(DueDateTimeframe.RECURRING.toString()).getItems().add(quest);
        }
    }

    /**
     * Determines which ListView a Quest should go in based on Due Date and
     * Occurrence Type
     * 
     * @param quest - the Quest being added to the ListView
     */
    private void addQuestToListView(Quest quest) {
        // Don't want to do Due Date calculations for Quests with the RECURRING
        // Occurrence
        // Type (since they do not have Due Dates)
        List<DueDateTimeframe> dueDateTimeframes = new ArrayList<>(Arrays.asList(DueDateTimeframe.values()));
        dueDateTimeframes.remove(DueDateTimeframe.RECURRING);

        if (quest.getDueDate() != null) {
            // Due Date minus the Current Date
            int daysUntilDueDate = Math
                    .toIntExact(ChronoUnit.DAYS.between(Instant.now(), quest.getDueDate().toInstant())) + 1;

            // Adds a Quest to a ListView based on distance of Due Date from the Current
            // Date
            for (DueDateTimeframe dueDateTimeframe : dueDateTimeframes) {
                if (daysUntilDueDate <= dueDateTimeframe.toInt()) {
                    QuestViewController.getQuestLists().get(dueDateTimeframe.toString()).getItems().add(quest);
                    break;
                }
            }
        } else {
            QuestViewController.getQuestLists().get(DueDateTimeframe.RECURRING.toString()).getItems().add(quest);
        }
    }

    private void clearInputFields() {
        questNameInput.setText("");
        questDescriptionInput.setText("");
        experiencePointsInput.setText("");
        occurrenceTypeInput.valueProperty().set(null);
        dueDateInput.valueProperty().set(null);
    }

    /**
     * If a Quest is being edited, this function transfers the attributes of that
     * Quest to this FXML Controller.
     * 
     * @param quest - the Quest to be edited
     */
    public void setQuestToSave(Quest quest) {
        questToSave = quest;

        questNameInput.setText(quest.getQuestName());
        questDescriptionInput.setText(quest.getQuestDescription());
        experiencePointsInput.setText(String.valueOf(quest.getExperiencePoints()));
        occurrenceTypeInput.valueProperty().set(quest.getOccurrenceType());

        if (quest.getDueDate() == null) {
            dueDateInput.valueProperty().set(null);
        } else {
            dueDateInput.valueProperty().set(LocalDate.from(quest.getDueDate().toLocalDateTime()));
        }
    }

    public static Boolean cancelWasClicked() {
        return cancelClicked;
    }
}
