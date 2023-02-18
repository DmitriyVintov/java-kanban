import java.util.HashMap;
import java.util.Map;
/**
 * Данный класс содержит методы для выполнения следующих действий:
 * Создание задач. Сам объект должен передаваться в качестве параметра;
 * Получение списка всех задач;
 * Получение по идентификатору;
 * Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра;
 * Удаление по идентификатору.
 * Удаление всех задач;
 */
public class ManagerTasks {
    private final Map<Integer, Task> tasksRepo = new HashMap<>();
    private final Map<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    private final Map<Integer, SubTask> subTasksRepo = new HashMap<>();
    private int countId = 0;

    /**
     * Создание задачи и добавление ее в список tasksRepo
     *
     * @param task Задача
     * @return id задачи
     */
    public int createTask(Task task) {
        task.id = countId++;
        task.status = Status.NEW;
        tasksRepo.put(task.id, task);
        return task.id;
    }

    /**
     * Создание Epic задачи и добавление ее в список epicTasksRepo
     *
     * @param epicTask Epic задача
     * @return id Epic задачи
     */
    public int createEpicTask(EpicTask epicTask) {
        epicTask.id = countId++;
        epicTask.status = Status.NEW;
        epicTasksRepo.put(epicTask.id, epicTask);
        return epicTask.id;
    }

    /**
     * Cоздание подзадачи и добавление ее в список subTasksRepo
     *
     * @param subTask Подзадача
     * @return id добавленной подзадачи или -1, если введен неверный id номер Epic задачи
     * при создании подзадачи
     */
    public int createSubTask(SubTask subTask) {
        if (!epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())) {
            subTask.id = countId++;
            subTask.status = Status.NEW;
            subTask.setIdEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()).id);
            subTasksRepo.put(subTask.id, subTask);
            epicTasksRepo.get(subTask.getIdEpicTask()).setIdSubTasks(subTask.id);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
            return subTask.id;
        } else return -1;
    }

    /**
     * Получение списка всех задач
     *
     * @return Список всех задач
     */
    public Map<Integer, Task> getTasksRepo() {
        return tasksRepo;
    }

    /**
     * Получение списка всех Epic задач
     *
     * @return Список всех Epic задач
     */
    public Map<Integer, EpicTask> getEpicTasksRepo() {
        return epicTasksRepo;
    }

    /**
     * Получение списка всех подзадач
     *
     * @return Список всех подзадач
     */
    public Map<Integer, SubTask> getSubTasksRepo() {
        return subTasksRepo;
    }

    /**
     * Получение задачи по id
     *
     * @param id id задачи, которую необходимо найти
     * @return Подзадача или null, если введен неверный id задачи
     */
    public Task getTaskById(int id) {
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id)) {
            return tasksRepo.get(id);
        } else {
            return null;
        }
    }

    /**
     * Получение Epic задачи по id
     *
     * @param id id Epic задачи, которую необходимо найти
     * @return Epic задача или null, если введен неверный id Epic задачи
     */
    public EpicTask getEpicTaskById(int id) {
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id)) {
            return epicTasksRepo.get(id);
        } else {
            return null;
        }
    }

    /**
     * Получение подзадачи по id
     *
     * @param id id подзадачи, которую необходимо найти
     * @return Подзадача или null, если введен неверный id подзадачи
     */
    public SubTask getSubTaskById(int id) {
        if (!subTasksRepo.isEmpty() && subTasksRepo.containsKey(id)) {
            return subTasksRepo.get(id);
        } else {
            return null;
        }
    }

    /**
     * Обновление задачи
     *
     * @param task   Задача, которую нужно обновить
     * @param status Статус, который нужно установить задаче
     */
    public void updateTask(Task task, Status status) {
        if (!tasksRepo.isEmpty() && tasksRepo.get(task.id).equals(task)) {
            task.status = status;
            tasksRepo.put(task.id, task);
        }
    }

    /**
     * Обновление Epic задачи (без участия пользователя)
     *
     * @param epicTask Epic задача
     */
    private void updateEpicTask(EpicTask epicTask) {
        int countNEW = 0;
        int countDONE = 0;
        if (!epicTasksRepo.isEmpty() && !epicTask.getIdSubTasks().isEmpty()) {
            for (Integer idSubTask : epicTask.getIdSubTasks()) {
                if (subTasksRepo.get(idSubTask).status.equals(Status.NEW)) {
                    countNEW++;
                }
                if (subTasksRepo.get(idSubTask).status.equals(Status.DONE)) {
                    countDONE++;
                }
            }
        }
        if (epicTask.getIdSubTasks().isEmpty()) {
            epicTask.status = Status.NEW;
            epicTasksRepo.put(epicTask.id, epicTask);
            return;
        }
        if (countNEW == epicTask.getIdSubTasks().size()) {
            epicTask.status = Status.NEW;
            epicTasksRepo.put(epicTask.id, epicTask);
            return;
        }
        if (countDONE == epicTask.getIdSubTasks().size()) {
            epicTask.status = Status.DONE;
            epicTasksRepo.put(epicTask.id, epicTask);
        } else {
            epicTask.status = Status.IN_PROGRESS;
        }

    }

    /**
     * Обновление подзадачи
     *
     * @param subTask Подзадача
     * @param status  Статус, который нужно установить подзадаче
     */
    public void updateSubTask(SubTask subTask, Status status) {
        if (!subTasksRepo.isEmpty()
                && !epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())
                && subTasksRepo.get(subTask.id).equals(subTask)) {
            subTask.status = status;
            subTasksRepo.put(subTask.id, subTask);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
        }
    }

    /**
     * Удаление задачи по id
     *
     * @param id id задачи, которую нужно удалить
     */
    public void deleteTaskById(int id) {
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id) && tasksRepo.get(id).id == id) {
            tasksRepo.remove(id);
        }
    }

    /**
     * Удаление Epic задачи
     *
     * @param id id Epic задачи, которую нужно удалить
     */
    public void deleteEpicTaskById(int id) {
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id) && epicTasksRepo.get(id).id == id
                && !epicTasksRepo.get(id).getIdSubTasks().isEmpty()) {
            for (Integer idSubTask : epicTasksRepo.get(id).getIdSubTasks()) {
                subTasksRepo.remove(idSubTask);
            }
            epicTasksRepo.remove(id);
        }
    }

    /**
     * Удаление подзадачи
     *
     * @param id id подзадачи, которую нужно удалить
     */
    public void deleteSubTaskById(int id) {
        int idEpicTask = subTasksRepo.get(id).getIdEpicTask();
        int idSubTask = epicTasksRepo.get(idEpicTask).getIdSubTasks().indexOf(id);
        if (id > 0 && !epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(idEpicTask)
                && !subTasksRepo.isEmpty() && subTasksRepo.containsKey(id) && subTasksRepo.get(id).id == id) {
            epicTasksRepo.get(idEpicTask).getIdSubTasks().remove(idSubTask);
            subTasksRepo.remove(id);
        }
        updateEpicTask(epicTasksRepo.get(idEpicTask));
    }

    /**
     * Удаление всех задач
     */
    public void deleteAllTasks() {
        tasksRepo.clear();
    }

    /**
     * Удаление всех Epic задач
     */
    public void deleteAllEpicTasks() {
        epicTasksRepo.clear();
    }

    /**
     * Удаление всех подзадач
     */
    public void deleteAllSubTasks() {
        subTasksRepo.clear();
    }
}
