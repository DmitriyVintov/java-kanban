package controller;

import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    void createTasks() {
        taskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
        InMemoryTaskManager.loadMapBusyTime();
        task = new Task("task1", "descrTask1", "29.04.2023 12:00", 10);
        epicTask = new EpicTask("epic1", "descrEpic1", "", 0);
        subTask = new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, epicTask.getId());
        historyManager = taskManager.getHistoryManager();
    }
}