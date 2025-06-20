package src.controllers.quests;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import src.backend.controller.QuestController;
import src.backend.model.Quest;
import src.controllers.FXMLControllerTemplate;
import src.handlers.FXMLHandler;
import src.handlers.PlayerHandler;
import src.handlers.WindowHandler;
import src.shared.DueDateTimeframe;
import src.shared.FXMLFilenames;
import src.shared.OccurrenceType;

public class QuestViewController extends FXMLControllerTemplate {

    private static final int REMOVE_TRAILING_DIGITS_DUE_DATE_STRING = 11;

    private QuestController questController = new QuestController();

    private static Map<String, ListView<Quest>> questLists = new HashMap<>();

    @FXML
    private TabPane questTabPane;

    @FXML
    private CheckBox todoCheckbox;
    @FXML
    private Text questNameText;
    @FXML
    private TextArea questDescriptionTextArea;
    @FXML
    private Text dueDateText;
    @FXML
    private Text occurrenceTypeText;
    @FXML
    private Text expText;
    @FXML
    private Button completeQuestButton;

    @FXML
    private ProgressBar expBar;
    @FXML
    private Text expBarText;

    @FXML
    public void initialize() {
        populateTabPanes();
        populateListViews();

        updateExpBar();
    }

    /**
     * Creates a Tab, ListView, and ObservableList for each DueDateTimefram value.
     */
    private void populateTabPanes() {

        createQuestListTab("TODO");

        // Create Tab, ObservableList and ListView for each Due Date type
        for (DueDateTimeframe dueDateTimeframe : DueDateTimeframe.values()) {
            String timeframeString = dueDateTimeframe.toString();
            createQuestListTab(timeframeString);
        }

        createQuestListTab("ONCE");
    }

    /**
     * Creates a Tab for the different Quest categories
     * 
     * @param tabHeaderText - the initial text to use for the Tab
     */
    private void createQuestListTab(String tabHeaderText) {

        // Creates a ListView for the Tab (to store the Quest objects)
        ListView<Quest> listView = new ListView<>();
        configureListView(listView);

        // Stores the ObservableList in a Map (so Quest objects can be retrieved)
        questLists.put(tabHeaderText, listView);

        Tab tab = new Tab();
        tab.setId(tabHeaderText);

        // Configures the text for the Tab
        Text tabText = new Text();
        tabText.setWrappingWidth(80);
        tabText.setTextAlignment(TextAlignment.CENTER);

        // Clear the Quest display whenever a new Tab is selected
        tab.setOnSelectionChanged(_ -> clearQuestDisplay());

        // Determines what text should be displayed
        if (DueDateTimeframe.contains(tabHeaderText) == Boolean.TRUE
                && !tabHeaderText.equals(DueDateTimeframe.RECURRING.toString())) {
            tabText.setText("Due Within A " + tabHeaderText.toUpperCase());
        } else {
            tabText.setText(tabHeaderText.toUpperCase());
        }

        // Sets the text for the Tab
        tab.setGraphic(tabText);
        tab.setText(tabText.getText()); // Hidden; used for TabPane quick select

        // Associates the Tab with the relevant ListView
        tab.setContent(listView);

        questTabPane.getTabs().add(tab);
    }

    /**
     * Associates the given ListView with an ObservableList<Quest>, and sets
     * relevant event listeners.
     * 
     * @param listView - ListView to be configured
     */
    private void configureListView(ListView<Quest> listView) {

        // Creates a List to hold the Quests for the associated Tab
        ObservableList<Quest> observableList = FXCollections.observableArrayList();
        listView.getItems().setAll(observableList);

        listView.setOnMouseClicked(_ -> questItemOnClick());

        // Update the ListView to highlight overdue Quests in red
        listView.setCellFactory(_ -> new ListCell<Quest>() {

            @Override
            protected void updateItem(Quest quest, boolean empty) {

                // Call the original updateItem() function (to maintain functionality)
                super.updateItem(quest, empty);

                // If no quest is being passed to the ListView, set relevant values to NULL
                if (empty || quest == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle(null);
                } else {
                    // Display the name of the Quest in the ListView
                    setText(quest.getQuestName());

                    // Check if this Quest has a due date
                    if (quest.getDueDate() != null) {
                        int daysUntilDueDate = Math
                                .toIntExact(ChronoUnit.DAYS.between(Instant.now(), quest.getDueDate().toInstant()));

                        // If the Quest is past due, highlight the ListView cell
                        if (daysUntilDueDate < 0) {
                            setStyle("-fx-background-color: red; -fx-text-fill: white");
                        } else {
                            setStyle(null);
                        }
                    }

                }
            }
        });
    }

    /**
     * Quests that are due within the relevant DueDateTimeframes will be added to
     * the appropriate ListViews.
     */
    private void populateListViews() {
        List<Quest> allQuests = questController.getAllQuests();
        for (Quest quest : allQuests) {
            addQuestToListView(quest);
        }

    }

    private void addQuestToListView(Quest quest) {
        if (quest.getTodo() == Boolean.TRUE) {
            questLists.get("TODO").getItems().add(quest);
        } else {

            // Don't want to do Due Date calculations for Quests with the RECURRING
            // Occurrence Type (since they do not have Due Dates)
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
                        questLists.get(dueDateTimeframe.toString()).getItems().add(quest);
                        break;
                    }
                }
            } else if (quest.getOccurrenceType().equals(OccurrenceType.ONCE)) {
                questLists.get("ONCE").getItems().add(quest);
            } else {
                questLists.get(DueDateTimeframe.RECURRING.toString()).getItems().add(quest);
            }
        }
    }

    /**
     * Displays Quest information when a Quest is selected in a ListView.
     */
    private void questItemOnClick() {
        Quest selectedQuest = getSelectedQuest();

        if (selectedQuest != null) {
            questNameText.setText(selectedQuest.getQuestName());
            questDescriptionTextArea.setText(selectedQuest.getQuestDescription());

            // Due Date Text Logic
            if (selectedQuest.getDueDate() != null) {
                String dueDateString = selectedQuest.getDueDate().toString();
                dueDateText.setText("Due Date: "
                        + dueDateString.substring(0, dueDateString.length() - REMOVE_TRAILING_DIGITS_DUE_DATE_STRING));
            } else {
                dueDateText.setText("Due Date: N/A");
            }

            // Occurrence Type Text Logic
            if (selectedQuest.getOccurrenceType() != null) {
                String occurrenceTypeString = selectedQuest.getOccurrenceType().toString();
                occurrenceTypeText.setText("Occurrence Type: " + occurrenceTypeString);
            }

            expText.setText("EXP: " + selectedQuest.getExperiencePoints());

            completeQuestButton.setVisible(true);

            todoCheckbox.setSelected(selectedQuest.getTodo());
            todoCheckbox.setVisible(true);
        }
    }

    private Quest getSelectedQuest() {
        Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
        return questLists.get(selectedTab.getId()).getSelectionModel().getSelectedItem();
    }

    @FXML
    private void addQuest() {
        WindowHandler.showPopupWindow(FXMLFilenames.ADD_QUEST);
    }

    @FXML
    private void editQuest() {
        Quest questToEdit = getSelectedQuest();

        if (questToEdit != null) {
            ((AddQuestController) FXMLHandler.getFxmlController(FXMLFilenames.ADD_QUEST)).setQuestToSave(questToEdit);
            WindowHandler.showPopupWindow(FXMLFilenames.ADD_QUEST);

            // Remove Quest from ListView (if Quest is being created or edited)
            // If the Window closed due to Cancel being pressed, do NOT remove the Quest
            // from the ListView
            if (AddQuestController.cancelWasClicked().equals(Boolean.FALSE)) {
                Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
                questLists.get(selectedTab.getId()).getItems().remove(questToEdit);
            }
        }

        clearQuestDisplay();
    }

    @FXML
    private void deleteQuest() {
        Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
        Quest selectedQuest = getSelectedQuest();

        if (selectedQuest != null) {
            // Removes the Quest from the associated ListView
            questLists.get(selectedTab.getId()).getItems().remove(selectedQuest);

            // Removes the Quest from the database
            questController.deleteEntry(selectedQuest);

            clearQuestDisplay();
        }
    }

    @FXML
    private void completeQuest() {
        Quest completedQuest = getSelectedQuest();

        // Grant Player experience points
        PlayerHandler.gainExp(completedQuest.getExperiencePoints());

        // Keep track of TO-DO status of the Quest
        Boolean wasTodo = completedQuest.getTodo();

        // If the Quest was marked as TO-DO, un-mark it
        completedQuest.setTodo(false);

        // Check if Quest has an OccurrenceType (and it isn't Recurring)
        if (completedQuest.getOccurrenceType() != null
                && !completedQuest.getOccurrenceType().equals(OccurrenceType.RECURRING)) {

            // Remove Quest from ListView
            Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
            questLists.get(selectedTab.getId()).getItems().remove(completedQuest);

            // If a one-off Quest, delete it from the database
            // If the Quest is recurring, calculate a new Due Date and put it in the
            // appropriate ListView
            if (completedQuest.getOccurrenceType().equals(OccurrenceType.ONCE)) {
                questController.deleteEntry(completedQuest);
            } else {
                updateDueDate(completedQuest);
            }
            // Check if Quest was TO-DO and RECURRING
        } else if (wasTodo == Boolean.TRUE
                && completedQuest.getOccurrenceType().equals(OccurrenceType.RECURRING)) {

            // Remove Quest from the TO-DO ListView
            questLists.get("TODO").getItems().remove(completedQuest);

            // Adds Quest back to the RECURRING ListView
            questLists.get("RECURRING").getItems().add(completedQuest);

            completedQuest.setTodo(false);

            // Updates Quest DB entry to not be TO-DO
            questController.updateEntry(completedQuest);
        }

        updateExpBar();

        clearQuestDisplay();
    }

    /**
     * When a Quest that has an Occurrence Type (other than Recurring) is completed,
     * the app must calculate the new Due Date for the Quest. It must also place
     * that Quest in the appropriate Tab based on this new Due Date.
     * 
     * @param quest - Quest object that needs a new Due Date
     */
    private void updateDueDate(Quest quest) {

        LocalDate newDueDate;

        // Determine which date to use when calculating the new Due Date
        // Strict Due Dates use the previous Due Date value
        // Non-strict Due Dates use the current date
        if (quest.getStrictDueDate().equals(Boolean.TRUE)) {
            LocalDate oldDueDate = LocalDate.from(quest.getDueDate().toLocalDateTime());
            newDueDate = oldDueDate.plusDays(quest.getOccurrenceType().toInt());
        } else {
            newDueDate = LocalDate.now().plusDays(quest.getOccurrenceType().toInt());
        }

        quest.setDueDate(Timestamp.valueOf(newDueDate.atStartOfDay()));

        // Updates the relevant ListViews
        addQuestToListView(quest);

        // Updates the Quest in the database
        questController.updateEntry(quest);
    }

    private void clearQuestDisplay() {
        todoCheckbox.setVisible(false);
        questNameText.setText("");
        questDescriptionTextArea.setText("");
        dueDateText.setText("");
        occurrenceTypeText.setText("");
        expText.setText("");
        completeQuestButton.setVisible(false);
    }

    private void updateExpBar() {
        expBar.setProgress(PlayerHandler.getProgressToNextLevel());
        expBarText.setText("EXP: " + PlayerHandler.getPlayerInstance().getExperiencePoints() + " / "
                + PlayerHandler.getPlayerInstance().getExpToNextLevel());
    }

    /**
     * Determines which ListView the currently selected Quest will go into when the
     * TO-DO checkbox is toggled.
     * 
     * @param quest - the Quest being placed into a List
     */
    @FXML
    private void todoCheckboxOnClick() {

        Quest selectedQuest = getSelectedQuest();

        // If TO-DO is marked, place the Quest in the TO-DO List
        // Else, determine which List it belongs based on OccurrenceType
        if (todoCheckbox.isSelected()) {

            // Update the Quest entry
            selectedQuest.setTodo(true);
            questController.updateEntry(selectedQuest);

            // Add the Quest to the TO-DO ListView
            questLists.get("TODO").getItems().add(selectedQuest);

            // Removes the Quest from the old ListView
            Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
            questLists.get(selectedTab.getId()).getItems().remove(selectedQuest);

            clearQuestDisplay();
        } else {
            selectedQuest.setTodo(false);

            // Determine which ListView to add Quest to based on Due Date and Occurrence
            // Type
            if (selectedQuest.getOccurrenceType() == OccurrenceType.ONCE) {
                QuestViewController.getQuestLists().get("ONCE").getItems().add(selectedQuest);
            } else {
                if (selectedQuest.getDueDate() != null) {
                    addQuestToListView(selectedQuest);
                } else if (selectedQuest.getOccurrenceType() != null
                        && selectedQuest.getOccurrenceType() != OccurrenceType.RECURRING) {
                    questLists.get(DueDateTimeframe.DAY.toString()).getItems().add(selectedQuest);
                } else {
                    questLists.get(DueDateTimeframe.RECURRING.toString()).getItems().add(selectedQuest);
                }
            }
        }

        questController.updateEntry(selectedQuest);

        // Removes the Quest from the old ListView
        Tab selectedTab = questTabPane.getSelectionModel().getSelectedItem();
        questLists.get(selectedTab.getId()).getItems().remove(selectedQuest);

        clearQuestDisplay();
    }

    public static Map<String, ListView<Quest>> getQuestLists() {
        return questLists;
    }
}