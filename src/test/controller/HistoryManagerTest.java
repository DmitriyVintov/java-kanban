package controller;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HistoryManagerTest {
    InMemoryTaskManager taskManager;
    int task1;
    int task2;
    int task3;

    @BeforeEach
    void create() {
        taskManager = (InMemoryTaskManager) Managers.getDefaultTaskManager();
        controller.Test.loadMapBusyTime();
        task1 = taskManager.createTask(new Task("Task1", "Descr1", "30.04.2023 12:00", 61));
        task2 = taskManager.createTask(new Task("Task2", "Descr2", "30.04.2023 13:00", 1));
        task3 = taskManager.createTask(new Task("Task3", "Descr3", "30.04.2023 15:00", 20));
    }

    @Test
    void add() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task2);
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void addSimilarTasks() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void getHistory() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task2);
        assertNotNull(taskManager.getHistory());
    }

    @Test
    void getHistoryIfHeEmpty() {
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void removeFirst() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task2);
        assertEquals(2, taskManager.getHistory().size());
        taskManager.getHistoryManager().remove(task1);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void removeLast() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task2);
        assertEquals(2, taskManager.getHistory().size());
        taskManager.getHistoryManager().remove(task2);
        assertEquals(1, taskManager.getHistory().size());
    }

    @Test
    void removeFromMiddle() {
        taskManager.getTaskById(task1);
        taskManager.getTaskById(task2);
        taskManager.getTaskById(task3);
        assertEquals(3, taskManager.getHistory().size());
        taskManager.getHistoryManager().remove(task2);
        assertEquals(2, taskManager.getHistory().size());
    }
}