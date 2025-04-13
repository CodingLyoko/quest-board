package src.backend.controller;

import java.util.List;

import lombok.NoArgsConstructor;
import src.backend.model.Quest;
import src.backend.service.QuestService;

@NoArgsConstructor
public class QuestController extends ControllerTemplate {

    QuestService questService = new QuestService();

    public List<Quest> getAllQuests() {
        return super.getAllEntries(Quest.class);
    }
}
