package controller;

import exception.ManagerException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    protected Task task;
    protected EpicTask epicTask;
    protected SubTask subTask;
    protected HistoryManager<Task> historyManager;

    @Test
    void getHistoryManager() {
        taskManager.createTask(task);
        taskManager.getTaskById(0);
        assertNotNull(historyManager);
    }

    @Test
    void getHistory() {
        taskManager.createTask(task);
        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(1, history.size(), "История не пустая");
    }

    @Test
    void getHistoryWhenListIsEmpty() {
        List<Task> history = taskManager.getHistory();
        assertEquals(0, history.size(), "История пустая");
    }

    @Test
    void createTask() {
        taskManager.createTask(task);
        assertEquals(0, task.getId());
        boolean b = taskManager.getTasksRepo().containsKey(task.getId());
        assertTrue(b);
        assertEquals(task, taskManager.getTaskById(task.getId()));
    }

    @Test
    void createTaskWhenArgumentsIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createTask(new Task("", "", "", 0)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void createTaskWhenTimeIsBusy() {
        taskManager.createTask(new Task("task1", "descr1", "29.04.2023 15:00", 2));
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createTask(new Task("task2", "descr2", "29.04.2023 15:00", 2)));
        assertEquals("Невозможно создать задачу. Данное время занято", e.getMessage());
    }

    @Test
    void createEpicTask() {
        taskManager.createEpicTask(epicTask);
        assertEquals(0, epicTask.getId());
        boolean b = taskManager.getEpicTasksRepo().containsKey(epicTask.getId());
        assertTrue(b);
        assertEquals(epicTask, taskManager.getEpicTaskById(epicTask.getId()));
    }

    @Test
    void createEpicTaskWhenArgumentsIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createEpicTask(new EpicTask("", "", "30.04.2023 12:00", 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void createSubTask() {
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
    void createSubTaskWhenArgumentsIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createSubTask(new SubTask("", "", "30.04.2023 12:00", 1, 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void createSubTaskWhenWrongIdEpicTask() {
        taskManager.createEpicTask(epicTask);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.createSubTask(new SubTask("subtask", "descr", "30.04.2023 12:00", 5, -1)));
        assertEquals("Данной Эпик задачи не существует", e.getMessage());
    }

    @Test
    void getReposWhenTheyEmpty() {
        assertEquals(0, taskManager.getTasksRepo().size());
        assertEquals(0, taskManager.getEpicTasksRepo().size());
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void getTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void getEpicTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getEpicTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void getSubTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.getSubTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void updateTask() {
        int task1 = taskManager.createTask(task);
        Task taskById = taskManager.getTaskById(task1);
        taskManager.updateTask(taskById, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, taskById.getStatus());
    }

    @Test
    void updateTaskWhenArgumentsIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateTask(new Task("", "", "30.04.2023 12:00", 5), Status.IN_PROGRESS));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void updateTaskWhenStatusIsNull() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateTask(new Task("task", "descr", "30.04.2023 12:00", 1), null));
        assertEquals("Введите правильный статус задачи", e.getMessage());
    }

    @Test
    void updateTaskWhenRepoDoesNotContainTask() {
        int task1 = taskManager.createTask(new Task("task1", "descr1", "30.04.2023 12:00", 1));
        taskManager.createTask(new Task("task2", "descr2", "30.04.2023 13:00", 1));
        taskManager.deleteTaskById(task1);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateTask(new Task("task", "descr", "30.04.2023 14:00", 1), Status.IN_PROGRESS));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void updateEpicTask() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask", "descr subtask", "30.04.2023 12:00", 1, epicTask1));
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        SubTask subTaskById = taskManager.getSubTaskById(subTask1);
        assertEquals(Status.NEW, epicTaskById.getStatus());
        taskManager.updateSubTask(subTaskById, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, epicTaskById.getStatus());
        taskManager.updateSubTask(subTaskById, Status.DONE);
        assertEquals(Status.DONE, epicTaskById.getStatus());
    }

    @Test
    void updateEpicTaskWhenArgumentsIsEmpty() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateEpicTask(new EpicTask("", "", "30.04.2023 12:00", 1)));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void updateEpicTaskWhenRepoDoesNotContainTask() {
        int epicTask1 = taskManager.createEpicTask(new EpicTask("epicTask1", "descr1", "30.04.2023 12:00", 1));
        taskManager.createEpicTask(new EpicTask("epicTask2", "descr2", "30.04.2023 13:00", 1));
        taskManager.deleteEpicTaskById(epicTask1);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateEpicTask(new EpicTask("epicTask", "descr", "30.04.2023 14:00", 1)));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void shouldBeEpicTaskStatusIsNew() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask1", "descr subtask1", "30.04.2023 12:00", 1, epicTask1));
        taskManager.createSubTask(new SubTask("subtask2", "descr subtask2", "30.04.2023 13:00", 1, epicTask1));
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        assertEquals(Status.NEW, epicTaskById.getStatus());
    }

    @Test
    void shouldBeEpicTaskStatusIsInProgress() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask1", "descr subtask1", "30.04.2023 12:00", 1, epicTask1));
        taskManager.createSubTask(new SubTask("subtask2", "descr subtask2", "30.04.2023 13:00", 1, epicTask1));
        SubTask subTaskById = taskManager.getSubTaskById(subTask1);
        taskManager.updateSubTask(subTaskById, Status.IN_PROGRESS);
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        assertEquals(Status.IN_PROGRESS, epicTaskById.getStatus());
        taskManager.updateSubTask(subTaskById, Status.DONE);
        assertEquals(Status.IN_PROGRESS, epicTaskById.getStatus());
    }

    @Test
    void shouldBeEpicTaskStatusIsDone() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask1", "descr subtask1", "30.04.2023 12:00", 1, epicTask1));
        int subTask2 = taskManager.createSubTask(new SubTask("subtask2", "descr subtask2", "30.04.2023 13:00", 1, epicTask1));
        SubTask subTaskById1 = taskManager.getSubTaskById(subTask1);
        SubTask subTaskById2 = taskManager.getSubTaskById(subTask2);
        taskManager.updateSubTask(subTaskById1, Status.DONE);
        taskManager.updateSubTask(subTaskById2, Status.DONE);
        EpicTask epicTaskById = taskManager.getEpicTaskById(epicTask1);
        assertEquals(Status.DONE, epicTaskById.getStatus());
    }

    @Test
    void updateSubTask() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subtask", "descr subtask", "30.04.2023 12:00", 1, epicTask1));
        SubTask subTaskById = taskManager.getSubTaskById(subTask1);
        taskManager.updateSubTask(subTaskById, Status.IN_PROGRESS);
        assertEquals(Status.IN_PROGRESS, subTaskById.getStatus());
    }

    @Test
    void updateSubTaskWhenArgumentsIsEmpty() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateSubTask(new SubTask("", "", "30.04.2023 12:00", 1, epicTask1), Status.IN_PROGRESS));
        assertEquals("Введены неверные имя или описание задачи", e.getMessage());
    }

    @Test
    void updateSubTaskWhenStatusIsNull() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateSubTask(new SubTask("task", "descr", "30.04.2023 12:00", 1, epicTask1), null));
        assertEquals("Введите правильный статус задачи", e.getMessage());
    }

    @Test
    void updateSubTaskWhenRepoDoesNotContainTask() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        int subTask1 = taskManager.createSubTask(new SubTask("subTask1", "descr1", "30.04.2023 12:00", 1, epicTask1));
        taskManager.createSubTask(new SubTask("subTask2", "descr2", "30.04.2023 13:00", 1, epicTask1));
        taskManager.deleteSubTaskById(subTask1);
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.updateTask(new SubTask("subTask", "descr", "30.04.2023 14:00", 1, 1), Status.IN_PROGRESS));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void deleteTaskById() {
        taskManager.createTask(task);
        assertEquals(1, taskManager.getTasksRepo().size());
        taskManager.deleteTaskById(0);
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void deleteTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void deleteEpicTaskById() {
        taskManager.createEpicTask(epicTask);
        assertEquals(1, taskManager.getEpicTasksRepo().size());
        taskManager.deleteEpicTaskById(0);
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void deleteEpicTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteEpicTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void deleteSubTaskById() {
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
    void deleteSubTaskByIdWhenWrongId() {
        final ManagerException e = assertThrows(ManagerException.class,
                () -> taskManager.deleteSubTaskById(-1));
        assertEquals("Данной задачи не существует", e.getMessage());
    }

    @Test
    void deleteAllTasks() {
        taskManager.createTask(task);
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void deleteAllTasksIfEmptyMap() {
        taskManager.deleteAllTasks();
        assertEquals(0, taskManager.getTasksRepo().size());
    }

    @Test
    void deleteAllEpicTasks() {
        taskManager.createEpicTask(epicTask);
        taskManager.deleteAllEpicTasks();
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void deleteAllEpicTasksIfEmptyMap() {
        taskManager.deleteAllEpicTasks();
        assertEquals(0, taskManager.getEpicTasksRepo().size());
    }

    @Test
    void deleteAllSubTasks() {
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask", "descr", "30.04.2023 12:00", 1, epicTask1));
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void deleteAllSubTasksIfEmptyMap() {
        taskManager.deleteAllSubTasks();
        assertEquals(0, taskManager.getSubTasksRepo().size());
    }

    @Test
    void loadMapBusyTime() {
        InMemoryTaskManager.loadMapBusyTime();
        assertEquals(35040, InMemoryTaskManager.busyTime.size());
    }

    @Test
    void checkTaskInBusyTimeMap() {
        InMemoryTaskManager.loadMapBusyTime();
        assertEquals(35040, InMemoryTaskManager.busyTime.size());
        int task1 = taskManager.createTask(task);
        Task taskById = taskManager.getTaskById(task1);
        assertTrue(InMemoryTaskManager.busyTime.containsKey(InMemoryTaskManager.getTimeToSearch(taskById)));
    }

    @Test
    void getPrioritizedTasks() {
        taskManager.createTask(task);
        int epicTask1 = taskManager.createEpicTask(epicTask);
        taskManager.createSubTask(new SubTask("subtask", "descr", "30.04.2023 12:00", 1, epicTask1));
        assertEquals(3, taskManager.getPrioritizedTasks().size());
    }
}