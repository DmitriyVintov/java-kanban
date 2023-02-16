import java.util.HashMap;

public class ManagerTasks {
    public HashMap<Integer, Task> tasksRepo = new HashMap<>();
    public HashMap<Integer, EpicTask> epicTasksRepo = new HashMap<>();
    public HashMap<Integer, SubTask> subTasksRepo = new HashMap<>();
    private int countId = 0;

    public int createTask(Task task){ //Создание задачи
        task.id = countId++;
        task.status = Status.NEW;
        tasksRepo.put(task.id, task);
        return task.id;
    }

    public int createEpicTask(EpicTask epicTask){ //Создание Epic задачи
        epicTask.id = countId++;
        epicTask.status = Status.NEW;
        epicTasksRepo.put(epicTask.id, epicTask);
        return epicTask.id;
    }

    public int createSubTask(SubTask subTask){ //Создание подзадачи
        if (!epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())){
            subTask.id = countId++;
            subTask.status = Status.NEW;
            subTask.setIdEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()).id);
            subTasksRepo.put(subTask.id, subTask);
            epicTasksRepo.get(subTask.getIdEpicTask()).setIdSubTasks(subTask.id);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
            return subTask.id;
        } else return -1;
    }

    public HashMap<Integer, Task> getTasksRepo(){ //Получение списка всех Task задач
        return tasksRepo;
    }

    public HashMap<Integer, EpicTask> getEpicTasksRepo(){ //Получение списка всех Epic задач
        return epicTasksRepo;
    }

    public HashMap<Integer, SubTask> getSubTasksRepo(){ //Получение списка всех SubTask задач
        return subTasksRepo;
    }

    public Task getTaskById(int id){ //Получение Task по id
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id)){
            return tasksRepo.get(id);
        } else {
            return null;
        }
    }

    public EpicTask getEpicTaskById(int id){ //Получение Epic по id
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id)){
            return epicTasksRepo.get(id);
        } else {
            return null;
        }
    }

    public SubTask getSubTaskById(int id){ //Получение SubTask по id
        if (!subTasksRepo.isEmpty() && subTasksRepo.containsKey(id)){
            return subTasksRepo.get(id);
        } else {
            return null;}
    }

    public void updateTask(Task task, Status status){ //Обновление Task задачи
        if (!tasksRepo.isEmpty() && tasksRepo.get(task.id).equals(task)){
            task.status = status;
            tasksRepo.put(task.id, task);
        }
    }

    private void updateEpicTask(EpicTask epicTask){ // Обновление EpicTask задачи
        int countNEW = 0;
        int countDONE = 0;
        if (!epicTasksRepo.isEmpty() && !epicTask.getIdSubTasks().isEmpty()) {
            for (Integer idSubTask : epicTask.getIdSubTasks()) {
                if (subTasksRepo.get(idSubTask).status.equals(Status.NEW)){
                    countNEW++;
                }
                if (subTasksRepo.get(idSubTask).status.equals(Status.DONE)) {
                    countDONE++;
                }
            }
        }
        if (epicTask.getIdSubTasks().isEmpty()){
            epicTask.status = Status.NEW;
            epicTasksRepo.put(epicTask.id, epicTask);
            return;
        }
        if (countNEW == epicTask.getIdSubTasks().size()){
            epicTask.status = Status.NEW;
            epicTasksRepo.put(epicTask.id, epicTask);
            return;
        }
        if (countDONE == epicTask.getIdSubTasks().size()) {
            epicTask.status = Status.DONE;epicTasksRepo.put(epicTask.id, epicTask);
        } else {
            epicTask.status = Status.IN_PROGRESS;
        }

    }

    public void updateSubTask(SubTask subTask, Status status){ //Обновление SubTask задачи
        if (!subTasksRepo.isEmpty()
                && !epicTasksRepo.isEmpty()
                && epicTasksRepo.containsKey(subTask.getIdEpicTask())
                && subTasksRepo.get(subTask.id).equals(subTask)){
            subTask.status = status;
            subTasksRepo.put(subTask.id, subTask);
            updateEpicTask(epicTasksRepo.get(subTask.getIdEpicTask()));
        }
    }

    public void deleteTaskById(int id){ //Удаление Task задачи по id
        if (!tasksRepo.isEmpty() && tasksRepo.containsKey(id) && tasksRepo.get(id).id == id){
            tasksRepo.remove(id);
        }
    }

    public void deleteEpicTaskById(int id){ //Удаление EpicTask задачи по id
        if (!epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(id) && epicTasksRepo.get(id).id == id
                && !epicTasksRepo.get(id).getIdSubTasks().isEmpty()){
            for (Integer idSubTask : epicTasksRepo.get(id).getIdSubTasks()) {
                subTasksRepo.remove(idSubTask);
            }
            epicTasksRepo.remove(id);
        }
    }

    public void deleteSubTaskById(int id){ //Удаление SubTask задачи по id
        int idEpicTask = subTasksRepo.get(id).getIdEpicTask();
        int idSubTask = epicTasksRepo.get(idEpicTask).getIdSubTasks().indexOf(id);
        if (id > 0 && !epicTasksRepo.isEmpty() && epicTasksRepo.containsKey(idEpicTask)
                && !subTasksRepo.isEmpty() && subTasksRepo.containsKey(id) && subTasksRepo.get(id).id == id){
            epicTasksRepo.get(idEpicTask).getIdSubTasks().remove(idSubTask);
            subTasksRepo.remove(id);
        }
        updateEpicTask(epicTasksRepo.get(idEpicTask));
    }

    public void deleteAllTasks(){ //Удаление всех Task задач
        tasksRepo.clear();
    }

    public void deleteAllEpicTasks(){ //Удаление всех Epic задач
        epicTasksRepo.clear();
    }

    public void deleteAllSubTasks(){ //Удаление всех Epic задач
        subTasksRepo.clear();
    }

    /*
    Получение списка всех задач.
    Удаление всех задач.
    Получение по идентификатору.
    Создание. Сам объект должен передаваться в качестве параметра.
    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Удаление по идентификатору.
*/
}
