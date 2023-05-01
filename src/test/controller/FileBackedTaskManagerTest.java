package controller;

import exception.ManagerException;
import model.EpicTask;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private final String path = Managers.getPath();

    @BeforeEach
    void createTasks() {
        taskManager = (FileBackedTaskManager) Managers.getFileBackedTaskManager();
        controller.Test.loadMapBusyTime();
        task = new Task("task1", "descrTask1", "29.04.2023 12:00", 10);
        epicTask = new EpicTask("epic1", "descrEpic1", "", 0);
        subTask = new SubTask("subtask1", "descrSub1", "29.04.2023 15:00", 30, epicTask.getId());
        historyManager = taskManager.getHistoryManager();
    }

    @Test
    void saveToFileAndLoadFromFile() {
        int task1 = taskManager.createTask(task);
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask", "descr subtask", "30.04.2023 15:00", 1, epicTask1));
        taskManager.getTaskById(task1);
        taskManager.getEpicTaskById(epicTask1);
        taskManager.getSubTaskById(subTask1);
        FileBackedTaskManager.historyToString(taskManager.getHistoryManager(), path);
        taskManager.getTasksRepo().clear();
        taskManager.getEpicTasksRepo().clear();
        taskManager.getSubTasksRepo().clear();
        taskManager.getHistoryManager().remove(task1);
        taskManager.getHistoryManager().remove(epicTask1);
        taskManager.getHistoryManager().remove(subTask1);
        FileBackedTaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(path);
        assertEquals(1, fileBackedTaskManager.getTasksRepo().size());
        assertEquals(1, fileBackedTaskManager.getEpicTasksRepo().size());
        assertEquals(1, fileBackedTaskManager.getSubTasksRepo().size());
        assertEquals(3, taskManager.getCountId());
    }

    @Test
    void saveHistoryToFileIfReposIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> FileBackedTaskManager.historyToString(taskManager.getHistoryManager(), path));
        assertEquals("История просмотров пустая", e.getMessage());
    }

    @Test
    void saveHistoryToFileIfWrongPath() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> FileBackedTaskManager.historyToString(taskManager.getHistoryManager(), ""));
        assertEquals("Ошибка записи в файл", e.getMessage());
    }

    @Test
    void loadFromFileIfWrongPath() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> FileBackedTaskManager.loadFromFile(""));
        assertEquals("Ошибка загрузки из файла", e.getMessage());
    }
}