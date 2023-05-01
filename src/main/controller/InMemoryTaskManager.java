package controller;

import exception.ManagerException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;

/**
 * Данный класс предназначен для хранения задач в оперативной памяти.
 * Реализует интерфейсы менеджера задач и менеджера истории просмотров задач
 */
public class InMemoryTaskManager implements TaskManager {
    private final Map<Integer, Task> tasksRepo = new HashMap<>();
    private final Map<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    private final Map<Integer, SubTask> subTasksRepo = new HashMap<>();
    protected int countId = 0;
    private final HistoryManager<Task> historyManager = Managers.getDefaultHistory();
    private final Set<Task> sortedSet = new TreeSet<>(Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder())));
    public static final int NUM_OF_15_MIN_INTERVALS = 365 * 24 * 60 / 15;
    public static final Map<LocalDateTime, Boolean> busyTime = new HashMap<>(NUM_OF_15_MIN_INTERVALS);

    /**
     * Метод получения менеджера истории
     *
     * @return
     */
    public HistoryManager<Task> getHistoryManager() {
        return historyManager;
    }

    /**
     * Получение списка истории просмотров задач
     *
     * @return
     */
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public void checkTaskInBusyTimeMap(Task task) {
        LocalDateTime timeToSearch;
        if (task.getStartTime() != null) {
            timeToSearch = getTimeToSearch(task);
            if (busyTime.get(timeToSearch)) {
                throw new ManagerException("Невозможно создать задачу. Данное время занято");
            } else {
                busyTime.put(timeToSearch, true);
            }
        }
    }

    public static LocalDateTime getTimeToSearch(Task task) {
        return task.getStartTime().minusMinutes(task.getStartTime().getMinute() % 15);
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(sortedSet);
    }

    @Override
    public int createTask(Task task) {
        if (task.getName().isBlank() || task.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        checkTaskInBusyTimeMap(task);
        task.setId(countId++);
        task.setStatus(Status.NEW);
        tasksRepo.put(task.getId(), task);
        sortedSet.add(task);
        return task.getId();
    }

    @Override
    public int createEpicTask(EpicTask epicTask) {
        if (epicTask.getName().isBlank() || epicTask.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        epicTask.setId(countId++);
        epicTask.setStatus(Status.NEW);
        epicTasksRepo.put(epicTask.getId(), epicTask);
        return epicTask.getId();
    }

    @Override
    public LocalDateTime getMinStartTimeSubTask(EpicTask epicTask) {
        LocalDateTime startTimeEpic = LocalDateTime.MAX;
        for (Integer idSubTask : epicTask.getIdSubTasks()) {
            LocalDateTime startTimeSubTask = getSubTaskById(idSubTask).getStartTime();
            if (startTimeSubTask.isBefore(startTimeEpic)) {
                startTimeEpic = startTimeSubTask;
            }
        }
        return startTimeEpic;
    }

    @Override
    public LocalDateTime getMaxEndTimeSubTask(EpicTask epicTask) {
        LocalDateTime endTimeEpic = LocalDateTime.MIN;
        for (Integer idSubTask : epicTask.getIdSubTasks()) {
            LocalDateTime endTimeSubTask = LocalDateTime.parse(getSubTaskById(idSubTask).getEndTimeToString(),
                    epicTask.getFORMAT());
            if (endTimeSubTask.isAfter(endTimeEpic)) {
                endTimeEpic = endTimeSubTask;
            }
        }
        return endTimeEpic;

    }

    @Override
    public int createSubTask(SubTask subTask) {
        if (subTask.getName().isBlank() || subTask.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        if (!epicTasksRepo.containsKey(subTask.getIdEpicTask()))
            throw new ManagerException("Данной Эпик задачи не существует");
        checkTaskInBusyTimeMap(subTask);
        subTask.setId(countId++);
        subTask.setStatus(Status.NEW);
        subTasksRepo.put(subTask.getId(), subTask);
        EpicTask epicTask = epicTasksRepo.get(subTask.getIdEpicTask());
        epicTask.setIdSubTasks(subTask.getId());
        sortedSet.add(subTask);
        updateEpicTask(epicTask);
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
        if (!tasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        historyManager.add(tasksRepo.get(id));
        return tasksRepo.get(id);
    }

    @Override
    public EpicTask getEpicTaskById(int id) {
        if (!epicTasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        historyManager.add(epicTasksRepo.get(id));
        return epicTasksRepo.get(id);
    }

    @Override
    public SubTask getSubTaskById(int id) {
        if (!subTasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        historyManager.add(subTasksRepo.get(id));
        return subTasksRepo.get(id);
    }

    @Override
    public void updateTask(Task task, Status status) {
        if (task.getName().isBlank() || task.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        if (status == null)
            throw new ManagerException("Введите правильный статус задачи");
        if (!tasksRepo.containsKey(task.getId()))
            throw new ManagerException("Данной задачи не существует");
        task.setStatus(status);
        tasksRepo.put(task.getId(), task);
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        if (epicTask.getName().isBlank() || epicTask.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        if (!epicTasksRepo.containsKey(epicTask.getId()))
            throw new ManagerException("Данной задачи не существует");
        epicTask.setStartTime(getMinStartTimeSubTask(epicTask));
        epicTask.setEndTime(getMaxEndTimeSubTask(epicTask));
        long duratinonLong = 0;
        if (!epicTask.getIdSubTasks().isEmpty()) {
            for (Integer idSubTask : epicTask.getIdSubTasks()) {
                duratinonLong += subTasksRepo.get(idSubTask).getDuration();
            }
        }
        epicTask.setDuration(duratinonLong);
        int countNEW = 0;
        int countDONE = 0;
        for (Integer idSubTask : epicTask.getIdSubTasks()) {
            if (subTasksRepo.get(idSubTask).getStatus().equals(Status.NEW)) {
                countNEW++;
            }
            if (subTasksRepo.get(idSubTask).getStatus().equals(Status.DONE)) {
                countDONE++;
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
        if (subTask.getName().isBlank() || subTask.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        if (status == null)
            throw new ManagerException("Введите правильный статус задачи");
        if (!epicTasksRepo.containsKey(subTask.getIdEpicTask())
                || !subTasksRepo.containsKey(subTask.getId()))
            throw new ManagerException("Данной задачи не существует");
        subTask.setStatus(status);
        subTasksRepo.put(subTask.getId(), subTask);
        updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        if (getHistory().contains(tasksRepo.get(id))) {
            historyManager.remove(id);
        }
        sortedSet.remove(getTaskById(id));
        busyTime.put(getTimeToSearch(getTaskById(id)), false);
        tasksRepo.remove(id);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (!epicTasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        for (Integer idSubTask : epicTasksRepo.get(id).getIdSubTasks()) {
            deleteSubTaskById(idSubTask);
        }
        if (getHistory().contains(epicTasksRepo.get(id))) {
            historyManager.remove(id);
        }
        epicTasksRepo.remove(id);
    }

    @Override
    public void deleteSubTaskById(int id) {
        if (!subTasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        int idEpicTask = subTasksRepo.get(id).getIdEpicTask();
        int idSubInEpicList = epicTasksRepo.get(idEpicTask).getIdSubTasks().indexOf(id);
        if (epicTasksRepo.containsKey(idEpicTask)
                && subTasksRepo.containsKey(id)) {
            epicTasksRepo.get(idEpicTask).getIdSubTasks().remove(idSubInEpicList);
            if (getHistory().contains(subTasksRepo.get(id))) {
                historyManager.remove(id);
            }
            sortedSet.remove(getSubTaskById(id));
            busyTime.put(getTimeToSearch(getSubTaskById(id)), false);
            subTasksRepo.remove(id);
        }
        updateEpicTask(epicTasksRepo.get(idEpicTask));
    }

    @Override
    public void deleteAllTasks() {
        for (Integer id : tasksRepo.keySet()) {
            if (getHistory().contains(tasksRepo.get(id))) {
                historyManager.remove(id);
            }
            sortedSet.remove(getTaskById(id));
            busyTime.put(getTimeToSearch(getTaskById(id)), false);
            deleteTaskById(id);
        }
    }

    @Override
    public void deleteAllEpicTasks() {
        for (Integer id : epicTasksRepo.keySet()) {
            if (getHistory().contains(epicTasksRepo.get(id))) {
                historyManager.remove(id);
            }
            deleteEpicTaskById(id);
        }
    }

    @Override
    public void deleteAllSubTasks() {
        for (Integer id : subTasksRepo.keySet()) {
            if (getHistory().contains(subTasksRepo.get(id))) {
                historyManager.remove(id);
            }
            sortedSet.remove(getSubTaskById(id));
            busyTime.put(getTimeToSearch(getSubTaskById(id)), false);
            deleteSubTaskById(id);
        }
    }
}