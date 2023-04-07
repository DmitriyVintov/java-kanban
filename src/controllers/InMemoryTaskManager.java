package controllers;

import models.EpicTask;
import models.Status;
import models.SubTask;
import models.Task;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данный класс предназначен для хранения задач в оперативной памяти
 * Реализует интерфейсы менеджера задач и менеджера истории просмотров задач
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasksRepo = new HashMap<>();
    private final Map<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    private final Map<Integer, SubTask> subTasksRepo = new HashMap<>();
    protected int countId = 0;
    private final HistoryManager<Task> historyManager = Managers.getDefaultHistory();

    /**
     * Метод получения менеджера истории
     * @return
     */
    public HistoryManager<Task> getHistoryManager() {
        return historyManager;
    }

    /**
     * Получение списка истории просмотров задач
     * @return
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public int createTask(Task task) {
        task.setId(countId++);
        task.setStatus(Status.NEW);
        tasksRepo.put(task.getId(), task);
        return task.getId();
    }

    @Override
    public int createEpicTask(EpicTask epicTask) {
        epicTask.setId(countId++);
        epicTask.setStatus(Status.NEW);
        epicTasksRepo.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (!epicTasksRepo.containsKey(subTask.getIdEpicTask())) {
            return -1;
        }
        subTask.setId(countId++);
        subTask.setStatus(Status.NEW);
        subTask.setIdEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()).getId());
        subTasksRepo.put(subTask.getId(), subTask);
        epicTasksRepo.get(subTask.getIdEpicTask()).setIdSubTasks(subTask.getId());
        updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
        return subTask.getId();
    }

    @Override
    public Map<Integer, Task> getTasksRepo() {
        return tasksRepo;
    }

    @Override
    public Map<Integer, EpicTask> getEpicTasksRepo() {
        return epicTasksRepo;
    }

    @Override
    public Map<Integer, SubTask> getSubTasksRepo() {
        return subTasksRepo;
    }

    @Override
    public Task getTaskById(int id) {
        if (!tasksRepo.containsKey(id)) {
            return null;
        }
        historyManager.add(tasksRepo.get(id));
        return tasksRepo.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (!epicTasksRepo.containsKey(id)) {
            return null;
        }
        historyManager.add(epicTasksRepo.get(id));
        return epicTasksRepo.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (!subTasksRepo.containsKey(id)) {
            return null;
        }
        historyManager.add(subTasksRepo.get(id));
        return subTasksRepo.get(id);
    }

    @Override
    public void updateTask(Task task, Status status) {
        if (!tasksRepo.isEmpty() && tasksRepo.get(task.getId()).equals(task)) {
            task.setStatus(status);
            tasksRepo.put(task.getId(), task);
        }
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        int countNEW = 0;
        int countDONE = 0;
        if (!epicTasksRepo.isEmpty() && !epicTask.getIdSubTasks().isEmpty()) {
            for (Integer idSubTask : epicTask.getIdSubTasks()) {
                if (subTasksRepo.get(idSubTask).getStatus().equals(Status.NEW)) {
                    countNEW++;
                }
                if (subTasksRepo.get(idSubTask).getStatus().equals(Status.DONE)) {
                    countDONE++;
                }
            }
        }
        if (epicTask.getIdSubTasks().isEmpty()) {
            epicTask.setStatus(Status.NEW);
            epicTasksRepo.put(epicTask.getId(), epicTask);
            return;
        }
        if (countNEW == epicTask.getIdSubTasks().size()) {
            epicTask.setStatus(Status.NEW);
            epicTasksRepo.put(epicTask.getId(), epicTask);
            return;
        }
        if (countDONE == epicTask.getIdSubTasks().size()) {
            epicTask.setStatus(Status.DONE);
            epicTasksRepo.put(epicTask.getId(), epicTask);
        } else {
            epicTask.setStatus(Status.IN_PROGRESS);
        }
    }

    @Override
    public void updateSubTask(SubTask subTask, Status status) {
        if (epicTasksRepo.containsKey(subTask.getIdEpicTask())
                && subTasksRepo.get(subTask.getId()).equals(subTask)) {
            subTask.setStatus(status);
            subTasksRepo.put(subTask.getId(), subTask);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        tasksRepo.remove(id);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (epicTasksRepo.containsKey(id)) {
            for (Integer idSubTask : epicTasksRepo.get(id).getIdSubTasks()) {
                subTasksRepo.remove(idSubTask);
            }
            epicTasksRepo.remove(id);
        }
    }

    @Override
    public void deleteSubTaskById(int id) {
        int idEpicTask = subTasksRepo.get(id).getIdEpicTask();
        int idSubTask = epicTasksRepo.get(idEpicTask).getIdSubTasks().indexOf(id);
        if (id > 0 && epicTasksRepo.containsKey(idEpicTask)
                && subTasksRepo.containsKey(id)) {
            epicTasksRepo.get(idEpicTask).getIdSubTasks().remove(idSubTask);
            subTasksRepo.remove(id);
        }
        updateEpicTask(epicTasksRepo.get(idEpicTask));
    }

    @Override
    public void deleteAllTasks() {
        tasksRepo.clear();
    }

    @Override
    public void deleteAllEpicTasks() {
        epicTasksRepo.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        subTasksRepo.clear();
    }
}