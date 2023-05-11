package controller;

import exception.ManagerException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;
    protected Task taskInProgress;
    protected Task taskDone;
    protected SubTask subTaskInProgress;
    protected SubTask subTaskDone;
    protected HistoryManager<Task> historyManager;

    @Test
    void shouldGetHistoryManager() {
        taskManager.createTask(task);
        taskManager.getTaskById(0);
        assertNotNull(historyManager);
    }

    @Test
    void shouldGetHistory() {
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size());
    }

    @Test
    void shouldGetHistoryWhenListIsEmpty() {
        List<Task> history = taskManager.getHistory();
        assertEquals(0, history.size());
    }

    @Test
    void shouldCreateTaskWhenReceivedValidTask() {
        taskManager.createTask(task);
        assertEquals(0, task.getId());
        boolean b = taskManager.getTasksRepo().containsKey(task.getId());
        assertTrue(b);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void shouldGetExceptionWhenGetTaskWithEmptyArguments() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createTask(new Task("", "", "", 0)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldGetExceptionWhenCreateTaskWithBusyTime() {
        taskManager.createTask(new Task("task1", "descr1", "29.04.2023 15:00", 2));
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createTask(new Task("task2", "descr2", "29.04.2023 15:00", 2)));
        assertEquals("Невозможно создать задачу. Данное время занято", e.getMessage());
    }

    @Test
    void shouldCreateEpicTaskWhenReceivedValidEpicTask() {
        taskManager.createEpicTask(epicTask);
        assertEquals(0, epicTask.getId());
        boolean b = taskManager.getEpicTasksRepo().containsKey(epicTask.getId());
        assertTrue(b);
        assertEquals(epicTask, taskManager.getEpicTaskById(epicTask.getId()));
    }

    @Test
    void shouldGetExceptionWhenGetEpicTaskWithEmptyArguments() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createEpicTask(new EpicTask("", "", "30.04.2023 12:00", 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldCreateSubTaskTWhenReceivedValidSubTask() {
        taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(subTask);
        assertEquals(1, subTask.getId());
        boolean b = taskManager.getSubTasksRepo().containsKey(subTask.getId());
        assertTrue(b);
        int idEpicTask = subTask.getIdEpicTask();
        assertEquals(0, idEpicTask);
        assertEquals(subTask, taskManager.getSubTaskById(subTask.getId()));
    }

    @Test
    void shouldGetExceptionWhenGetSubTaskWithEmptyArguments() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createSubTask(new SubTask("", "", "30.04.2023 12:00", 1, 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldGetEmptyMapsWhenNoTasksHaveBeenCreated() {
        assertEquals(0, taskManager.getTasksRepo().size());
        assertEquals(0, taskManager.getEpicTasksRepo().size());
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void shouldGetExceptionWhenGetWrongIdTaskForGetById() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldGetExceptionWhenGetWrongIdEpicTaskForGetById() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getEpicTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldGetExceptionWhenGetWrongIdSubTaskForGetById() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getSubTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldUpdateTaskWhenReceivedValidTask() {
        taskManager.createTask(task);
        taskManager.updateTask(taskInProgress);
        Task taskById1 = taskManager.getTaskById(0);
        assertEquals(Status.IN_PROGRESS, taskById1.getStatus());
    }

    @Test
    void shouldGetExceptionWhenUpdateTaskWithEmptyArguments() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateTask(new Task("", "", "30.04.2023 12:00", 5, Status.IN_PROGRESS)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldUpdateEpicTaskWhenReceivedValidEpicTask() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        assertEquals(Status.NEW, epicTaskById.getStatus());
        taskManager.createSubTask(new SubTask("subtask1", "descr subtask", "30.04.2023 12:00", 1, epicTask1, Status.NEW));
        taskManager.updateSubTask(new SubTask("subtask1", "descr subtask", "30.04.2023 12:00", 1, epicTask1, Status.IN_PROGRESS));
        assertEquals(Status.IN_PROGRESS, epicTaskById.getStatus());
        taskManager.updateSubTask(new SubTask("subtask1", "descr subtask", "30.04.2023 12:00", 1, epicTask1, Status.DONE));
        assertEquals(Status.DONE, epicTaskById.getStatus());
    }

    @Test
    void shouldGetExceptionWhenUpdateEpicTaskWithEmptyArguments() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateEpicTask(new EpicTask("", "", "30.04.2023 12:00", 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldGetExceptionWhenUpdateEpicTaskButMapDoesNotContainTask() {
        int epicTask1 = taskManager.createEpicTask(new EpicTask("epicTask1", "descr1", "30.04.2023 12:00", 1));
        taskManager.createEpicTask(new EpicTask("epicTask2", "descr2", "30.04.2023 13:00", 1));
        taskManager.deleteEpicTaskById(epicTask1);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateEpicTask(new EpicTask("epicTask", "descr", "30.04.2023 14:00", 1)));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldBeEpicTaskStatusNewWhenAllSubTasksIsNew() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask1", "descr subtask1", "30.04.2023 12:00", 1, epicTask1));
        taskManager.createSubTask(new SubTask("subtask2", "descr subtask2", "30.04.2023 13:00", 1, epicTask1));
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        assertEquals(Status.NEW, epicTaskById.getStatus());
    }

    @Test
    void shouldUpdateSubTaskWhenReceivedValidSubTask() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask", "descr subtask", "30.04.2023 12:00", 1, epicTask1, Status.NEW));
        taskManager.updateSubTask(new SubTask("subtask", "descr subtask", "30.04.2023 12:00", 1, epicTask1, Status.IN_PROGRESS));
        SubTask subTaskById = taskManager.getSubTaskById(subTask1);
        assertEquals(Status.IN_PROGRESS, subTaskById.getStatus());
    }

    @Test
    void shouldGetExceptionWhenUpdateSubTaskWithEmptyArguments() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateSubTask(new SubTask("", "", "30.04.2023 12:00", 1, epicTask1, Status.IN_PROGRESS)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void shouldDeleteTaskByIdWhenReceivedValidId() {
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasksRepo().size());
        taskManager.deleteTaskById(0);
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void shouldGetExceptionWhenDeleteTaskByIdWithWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldDeleteEpicTaskByIdWhenReceivedValidId() {
        taskManager.createEpicTask(epicTask);
        assertEquals(1, taskManager.getEpicTasksRepo().size());
        taskManager.deleteEpicTaskById(0);
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void shouldGetExceptionWhenDeleteEpicTaskByIdWithWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteEpicTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldDeleteSubTaskByIdWhenReceivedValidId() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask", "descr task", "30.04.2023 12:00", 1, epicTask1));
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        taskManager.getSubTaskById(subTask1);
        assertEquals(1, taskManager.getSubTasksRepo().size());
        assertEquals(1, epicTaskById.getIdSubTasks().size());
        taskManager.deleteSubTaskById(subTask1);
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void shouldGetExceptionWhenDeleteSubTaskByIdWithWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteSubTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldDeleteAllTasks() {
        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void shouldDeleteAllTasksWhenMapIsEmpty() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void shouldDeleteAllEpicTasks() {
        taskManager.createEpicTask(epicTask);
        taskManager.deleteAllEpicTasks();
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void shouldDeleteAllEpicTasksWhenMapIsEmpty() {
        taskManager.deleteAllEpicTasks();
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void shouldDeleteAllSubTasks() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask", "descr", "30.04.2023 12:00", 1, epicTask1));
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void shouldDeleteAllSubTasksWhenMapIsEmpty() {
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void shouldLoadMapBusyTime() {
        controller.Test.loadMapBusyTime();
        Assertions.assertEquals(InMemoryTaskManager.NUM_OF_15_MIN_INTERVALS, InMemoryTaskManager.busyTime.size());
    }

    @Test
    void shouldGetTrueWhenTimeIsBusy() {
        controller.Test.loadMapBusyTime();
        assertEquals(InMemoryTaskManager.NUM_OF_15_MIN_INTERVALS, InMemoryTaskManager.busyTime.size());
        int task1 = taskManager.createTask(task);
        Task taskById = taskManager.getTaskById(task1);
        assertTrue(InMemoryTaskManager.busyTime.containsKey(InMemoryTaskManager.getTimeToSearch(taskById)));
    }

    @Test
    void shouldGetPrioritizedTasks() {
        taskManager.createTask(task);
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask", "descr", "30.04.2023 12:00", 1, epicTask1));
        assertEquals(2, taskManager.getPrioritizedTasks().size());
    }
}