import java.util.HashMap;

public class ManagerTasks {
    public HashMap<Integer, Task> tasks = new HashMap<>();
    public HashMap<Integer, EpicTask> epicTasks = new HashMap<>();
    protected final String[] STATUS = {"NEW", "IN_PROGRESS", "DONE"};

    public void createTask(Task task){ //Создание задачи
        int id = tasks.size();
        task.id = id;
        task.status = this.STATUS[0];
        tasks.put(id, task);
    }

    public void createEpicTask(EpicTask epicTask){ //Создание Epic задачи
        int id = epicTasks.size();
        epicTask.id = id;
        epicTask.status = this.STATUS[0];
        epicTasks.put(id, epicTask);
    }

    public void createSubTask(SubTask subTask){ //Создание подзадачи
        if (checkSubTaskInEpicTask(subTask)){
            int id = epicTasks.get(subTask.idEpicTask).listSubTasks.size();
            subTask.id = id;
            subTask.status = this.STATUS[0];
            subTask.idEpicTask = epicTasks.get(subTask.idEpicTask).id;
            epicTasks.get(subTask.idEpicTask).listSubTasks.put(id, subTask);
        } else {
            System.out.println("Epic задачи с номером " + subTask.idEpicTask + " нет. Введите правильный номер");
        }
    }

    private boolean checkSubTaskInEpicTask(SubTask subTask){ // Проверка наличия Epic задачи по SubTask
        return (!epicTasks.isEmpty()
                && epicTasks.containsKey(subTask.idEpicTask)
                && !epicTasks.get(subTask.idEpicTask).listSubTasks.isEmpty()
                && epicTasks.get(subTask.idEpicTask).listSubTasks.containsKey(subTask.id)
                && epicTasks.get(subTask.idEpicTask).listSubTasks.get(subTask.id).id == subTask.id);
    }

    public void getAllTasks(){ //Получение списка всех задач
        System.out.println("tasks = " + tasks);
        System.out.println("epicTasks = " + epicTasks);
    }

    public void deleteAllTasks(){ //Удаление всех задач
        tasks.clear();
        System.out.println("Все задачи удалены");
    }

    public void deleteAllEpicTasks(){ //Удаление всех Epic задач
        epicTasks.clear();
        System.out.println("Все Epic задачи и подзадачи удалены");
    }

    public void getTaskById(int id){ //Получение задачи по id
        if (id >= 0 && !tasks.isEmpty() && tasks.containsKey(id)){
            System.out.println("Задача №" + id + ": " + tasks.get(id));
        } else {
            System.out.println("Задачи под номером " + id + " нет");
        }
    }

    public void getEpicTaskById(int id){ //Получение Epic задачи по id
        if (id >= 0 && !epicTasks.isEmpty() && epicTasks.containsKey(id)){
            System.out.println("Epic задача №" + id + ": " + epicTasks.get(id));
        } else {
            System.out.println("Epic задачи под номером " + id + " нет");
        }
    }

    public void getSubTaskById(int idEpicTask, int idSubTask){ //Получение подзадачи по id EpicTask и id SubTask
        if (idEpicTask >= 0
                && idSubTask >= 0
                && !epicTasks.isEmpty()
                && epicTasks.containsKey(idEpicTask)
                && !epicTasks.get(idEpicTask).listSubTasks.isEmpty()
                && epicTasks.get(idEpicTask).listSubTasks.containsKey(idSubTask)
                && epicTasks.get(idEpicTask).listSubTasks.get(idSubTask).id == idSubTask){
            System.out.println("Подзадача № " + idSubTask + " в Epic задаче " + idEpicTask + ": "
                    + epicTasks.get(idEpicTask).listSubTasks.get(idSubTask));
        } else {
            System.out.println("Данных задач не сущесвует");
        }
    }

    public void updateTask(){}//Обновление задачи

    public void deleteTaskById(){}//Удаление задачи по id

    /*
    Получение списка всех задач.
    Удаление всех задач.
    Получение по идентификатору.
    Создание. Сам объект должен передаваться в качестве параметра.
    Обновление. Новая версия объекта с верным идентификатором передаётся в виде параметра.
    Удаление по идентификатору.
*/
}