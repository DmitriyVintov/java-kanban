package controller;

import exception.ManagerException;
import model.EpicTask;
import model.Status;
import model.SubTask;
import model.Task;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * Данный класс предназначен для записи задач из оперативной памяти в файл и восстановления задач в памяти из файла
 * Реализует интерфейсы менеджера задач и менеджера истории просмотров задач
 */
public class FileBackedTaskManager extends InMemoryTaskManager {
    private final String path;

    public FileBackedTaskManager(String path) {
        this.path = path;
    }

    @Override
    public List<Task> getHistory() {
        return super.getHistory();
    }

    /**
     * Сохраняет текущее состояние менеджера в указанный файл
     */
    protected void save() {
        try (FileWriter fw = new FileWriter(path, StandardCharsets.UTF_8)) {
            StringBuilder result = new StringBuilder("id,type,name,status,description,startTime,duration,epic" + "\n");
            for (Task task : getTasksRepo().values()) {
                result.append(String.format("%s,%s,%s,%s,%s,%s,%s,", task.getId(), task.getType(), task.getName(),
                        task.getStatus(), task.getDescription(), task.getStartTimeToString(), task.getDuration())).append("\n");
            }
            for (EpicTask epicTask : getEpicTasksRepo().values()) {
                result.append(String.format("%s,%s,%s,%s,%s,%s,%s,", epicTask.getId(), epicTask.getType(), epicTask.getName(), epicTask.getStatus(),
                        epicTask.getDescription(), epicTask.getStartTimeToString(), epicTask.getDuration())).append("\n");
            }
            for (SubTask subTask : getSubTasksRepo().values()) {
                result.append(String.format("%s,%s,%s,%s,%s,%s,%s,%s", subTask.getId(), subTask.getType(), subTask.getName(), subTask.getStatus(),
                        subTask.getDescription(), subTask.getStartTimeToString(), subTask.getDuration(),
                        subTask.getIdEpicTask())).append("\n");
            }
            fw.write(result.append("\n").toString());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * Метод восстанавливает данные из файла при запуске программы
     *
     * @param path Относительный путь к файлу, из которого нужно восстановить данные в менеджер
     * @return Данный менеджер задач
     */
    public static FileBackedTaskManager loadFromFile(String path) {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager(path);
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            while (br.ready() && ((line = br.readLine()) != null)) {
                if (!Objects.equals(line.split(",")[0], "id") && !line.equals("")) {
                    String typeTask = line.split(",")[1];
                    switch (typeTask) {
                        case "TASK":
                            Task task = fileBackedTaskManager.getTaskFromString(line);
                            fileBackedTaskManager.getTasksRepo().put(task.getId(), task);
                            break;
                        case "EPIC":
                            EpicTask epicTask = fileBackedTaskManager.getEpicTaskFromString(line);
                            fileBackedTaskManager.getEpicTasksRepo().put(epicTask.getId(), epicTask);
                            break;
                        case "SUBTASK":
                            SubTask subTask = fileBackedTaskManager.getSubTaskFromString(line);
                            fileBackedTaskManager.getSubTasksRepo().put(subTask.getId(), subTask);
                            break;
                    }
                }
                if (line.equals("")) {
                    line = br.readLine();
                    historyFromString(fileBackedTaskManager, line);
                    return fileBackedTaskManager;
                }
            }
        } catch (IOException e) {
            throw new ManagerException("Ошибка загрузки из файла");
        }
        return fileBackedTaskManager;
    }

    /**
     * Метод получения задачи из строки
     *
     * @param string
     * @return Задача
     */
    private Task getTaskFromString(String string) {
        String[] elem = string.split(",");
        Task task = new Task(elem[2], elem[4], elem[5], Long.parseLong(elem[6]));
        task.setId(Integer.parseInt(elem[0]));
        task.setStatus(Status.valueOf(elem[3]));
        setCountId(task.getId() + 1);
        getTasksRepo().put(task.getId(), task);
        return task;
    }

    /**
     * Метод получения Эпик задачи из строки
     *
     * @param string
     * @return Эпик задача
     */
    private EpicTask getEpicTaskFromString(String string) {
        String[] elem = string.split(",");
        EpicTask epicTask = new EpicTask(elem[2], elem[4], elem[5], Long.parseLong(elem[6]));
        epicTask.setId(Integer.parseInt(elem[0]));
        epicTask.setStatus(Status.valueOf(elem[3]));
        setCountId(epicTask.getId() + 1);
        getEpicTasksRepo().put(epicTask.getId(), epicTask);
        return epicTask;
    }

    /**
     * Метод получения подзадачи из строки
     *
     * @param string
     * @return Подзадача
     */
    private SubTask getSubTaskFromString(String string) {
        String[] elem = string.split(",");
        SubTask subTask = new SubTask(elem[2], elem[4], elem[5], Long.parseLong(elem[6]), Integer.parseInt(elem[7]));
        subTask.setId(Integer.parseInt(elem[0]));
        EpicTask epic = getEpicTasksRepo().get(subTask.getIdEpicTask());
        epic.setIdSubTasks(Integer.valueOf(elem[0]));
        setCountId(subTask.getId() + 1);
        getSubTasksRepo().put(subTask.getId(), subTask);
        return subTask;
    }

    /**
     * Статический метод сохранения истории просмотров из менеджера в файл
     *
     * @param historyManager Менеджер истории
     * @param path           Путь к файлу сохранения
     */
    public static void historyToString(HistoryManager<Task> historyManager, String path) {
        StringBuilder result = new StringBuilder();
        try (FileWriter fw = new FileWriter(path, StandardCharsets.UTF_8, true)) {
            if (historyManager.getHistory().isEmpty())
                throw new ManagerException("История просмотров пустая");
            for (Task task : historyManager.getHistory()) {
                result.append(task.getId()).append(",");
            }
            result.deleteCharAt(result.lastIndexOf(","));
            fw.write(result.toString());
        } catch (IOException e) {
            throw new ManagerException("Ошибка записи в файл");
        }
    }

    /**
     * Статический метод восстановления менеджера истории из CSV
     *
     * @param fileBackedTaskManager
     * @param value                 Строка из файла, где содержатся id просмотренных задач
     */
    private static void historyFromString(FileBackedTaskManager fileBackedTaskManager, String value) {
        HistoryManager<Task> historyManager = fileBackedTaskManager.getHistoryManager();
        Optional<String> valueString = Optional.ofNullable(value);
        if (valueString.isEmpty()) {
            return;
        }
        String[] result = value.split(",");
        for (String s : result) {
            for (Task task : fileBackedTaskManager.getTasksRepo().values()) {
                if (fileBackedTaskManager.getTasksRepo().containsKey(Integer.parseInt(s))) {
                    historyManager.add(task);
                }
            }
            for (EpicTask epicTask : fileBackedTaskManager.getEpicTasksRepo().values()) {
                if (fileBackedTaskManager.getEpicTasksRepo().containsKey(Integer.parseInt(s))) {
                    historyManager.add(epicTask);
                }
            }
            for (SubTask subTask : fileBackedTaskManager.getSubTasksRepo().values()) {
                if (fileBackedTaskManager.getSubTasksRepo().containsKey(Integer.parseInt(s))) {
                    historyManager.add(subTask);
                }
            }
        }
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getId();
    }

    @Override
    public int createEpicTask(EpicTask epicTask) {
        super.createEpicTask(epicTask);
        save();
        return epicTask.getId();
    }

    @Override
    public int createSubTask(SubTask subTask) {
        super.createSubTask(subTask);
        save();
        return subTask.getId();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateEpicTask(EpicTask epicTask) {
        super.updateEpicTask(epicTask);
        save();
    }

    @Override
    public void updateSubTask(SubTask subTask) {
        super.updateSubTask(subTask);
        save();
    }

    @Override
    public void deleteTaskById(int id) {
        super.deleteTaskById(id);
        save();
    }

    @Override
    public void deleteEpicTaskById(int id) {
        super.deleteEpicTaskById(id);
        save();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    /**
     * Получение счетчика задач данного менеджера
     *
     * @return
     */
    public int getCountId() {
        return countId;
    }

    /**
     * Метод установки значения счетчика задач данного менеджера. Используется при восстановлении данных из файла
     *
     * @param countId
     */
    protected void setCountId(int countId) {
        if (this.countId < countId) {
            this.countId = countId;
        }
    }
}