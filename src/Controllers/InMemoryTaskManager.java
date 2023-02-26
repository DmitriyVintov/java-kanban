package Controllers;

import Models.EpicTask;
import Models.Status;
import Models.SubTask;
import Models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Данный класс реализует интерфейсы менеджера задач и менеджера истории просмотров задач
 */
public class InMemoryTaskManager implements TaskManager, HistoryManager {
    private final Map<Integer, Task> tasksRepo = new HashMap<>();
    private final Map<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    private final Map<Integer, SubTask> subTasksRepo = new HashMap<>();
    private final List<Task> history = new ArrayList<>();
    private int countId = 0;

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
        if (!epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())) {
            subTask.setId(countId++);
            subTask.setStatus(Status.NEW);
            subTask.setIdEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()).getId());
            subTasksRepo.put(subTask.getId(), subTask);
            epicTasksRepo.get(subTask.getIdEpicTask()).setIdSubTasks(subTask.getId());
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
            return subTask.getId();
        } else return -1;
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
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id)) {
            addInHistory(tasksRepo.get(id));
            return tasksRepo.get(id);
        } else {
            return null;
        }
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id)) {
            addInHistory(epicTasksRepo.get(id));
            return epicTasksRepo.get(id);
        } else {
            return null;
        }
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (!subTasksRepo.isEmpty() && subTasksRepo.containsKey(id)) {
            addInHistory(subTasksRepo.get(id));
            return subTasksRepo.get(id);
        } else {
            return null;
        }
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
        if (!subTasksRepo.isEmpty()
                && !epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())
                && subTasksRepo.get(subTask.getId()).equals(subTask)) {
            subTask.setStatus(status);
            subTasksRepo.put(subTask.getId(), subTask);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
        }
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id) && tasksRepo.get(id).getId() == id) {
            tasksRepo.remove(id);
        }
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id) && epicTasksRepo.get(id).getId() == id
                && !epicTasksRepo.get(id).getIdSubTasks().isEmpty()) {
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
        if (id > 0 && !epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(idEpicTask)
                && !subTasksRepo.isEmpty() && subTasksRepo.containsKey(id) && subTasksRepo.get(id).getId() == id) {
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

    @Override
    public void addInHistory(Task task) {
        if (history.size() >= 10) {
            history.remove(0);
        }
        history.add(task.getId(), task);
    }

    @Override
    public List<Task> getHistory() {
        return history;
    }
}