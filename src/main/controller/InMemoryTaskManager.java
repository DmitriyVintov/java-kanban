package controller;

import exception.ManagerException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Данный класс предназначен для хранения задач в оперативной памяти.
 * Реализует интерфейсы менеджера задач и менеджера истории просмотров задач
 */
public class InMemoryTaskManager implements TaskManager {
    protected final Map<Integer, Task> tasksRepo = new HashMap<>();
    protected final Map<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    protected final Map<Integer, SubTask> subTasksRepo = new HashMap<>();
    protected int countId = 0;
    protected final HistoryManager<Task> historyManager = Managers.getDefaultHistory();
    private final Comparator<Task> comparator = Comparator.comparing(Task::getStartTime,
            Comparator.nullsLast(Comparator.naturalOrder()));
    protected Set<Task> sortedSet = new TreeSet<>(comparator);
    public static final int NUM_OF_15_MIN_INTERVALS = 365 * 24 * 60 / 15;
    public static final Map<LocalDateTime, Boolean> busyTime = new HashMap<>(NUM_OF_15_MIN_INTERVALS);

    public static void loadMapBusyTime() {
        for (long i = 0; i < InMemoryTaskManager.NUM_OF_15_MIN_INTERVALS; i++) {
            LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
            InMemoryTaskManager.busyTime.put(start.plusMinutes(i * 15), false);
        }
    }

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
        List<Task> collect = tasksRepo.values().stream()
                .filter(task1 -> task1.getName().equals(task.getName())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            updateTask(task);
        }
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
        List<Task> collect = epicTasksRepo.values().stream()
                .filter(epicTask1 -> epicTask1.getName().equals(epicTask.getName())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            throw new ManagerException("Задача с таким именем существует");
        }
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
        List<Task> collect = subTasksRepo.values().stream()
                .filter(subTask1 -> subTask1.getName().equals(subTask.getName())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            updateSubTask(subTask);
        }
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
    public void updateTask(Task task) {
        if (task.getName().isBlank() || task.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        List<Task> collect = tasksRepo.values().stream()
                .filter(task1 -> task1.getName().equals(task.getName())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            int id = collect.get(0).getId();
            task.setId(id);
            tasksRepo.put(id, task);
        }
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
    public void updateSubTask(SubTask subTask) {
        if (subTask.getName().isBlank() || subTask.getDescription().isBlank())
            throw new ManagerException("Введены неверные имя или описание задачи");
        List<Task> collect = subTasksRepo.values().stream()
                .filter(subTask1 -> subTask1.getName().equals(subTask.getName())).collect(Collectors.toList());
        if (!collect.isEmpty()) {
            int id = collect.get(0).getId();
            subTask.setId(id);
            subTasksRepo.put(id, subTask);
        }
        updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
    }

    @Override
    public void deleteTaskById(int id) {
        if (!tasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        if (getHistory().contains(tasksRepo.get(id))) {
            historyManager.remove(id);
        }
        for (Map.Entry<Integer, Task> key : tasksRepo.entrySet()) {
            if (key.getKey() == id) {
                sortedSet.remove(key.getValue());
            }
        }
        busyTime.put(getTimeToSearch(getTaskById(id)), false);
        tasksRepo.remove(id);
    }

    @Override
    public void deleteEpicTaskById(int id) {
        if (!epicTasksRepo.containsKey(id))
            throw new ManagerException("Данной задачи не существует");
        List<Integer> deleteSubtask = new ArrayList<>(epicTasksRepo.get(id).getIdSubTasks());
        for (Integer idSubtask : deleteSubtask) {
            deleteSubTaskById(idSubtask);
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
            for (Map.Entry<Integer, SubTask> key : subTasksRepo.entrySet()) {
                if (key.getKey() == id) {
                    sortedSet.remove(key.getValue());
                }
            }
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