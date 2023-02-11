import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;

public class ManagerTasks {
    public HashMap<Integer, Task> listAllTasks = new HashMap<>(); //todo: из-за Task не могу достучаться к полю EpicTask
    protected final String[] STATUS = {"NEW", "IN_PROGRESS", "DONE"};

    protected int getId(HashMap<Integer, Task> listAllTasks){ //Получение id задачи
        this.listAllTasks = listAllTasks;
        if (listAllTasks == null) {
            return 0;
        }
        return listAllTasks.size();
    }

    public void createTask(Task task){ //Создание задачи
        int id = getId(listAllTasks);
        task.id = id;
        task.status = this.STATUS[0];
        listAllTasks.put(id, task);
    }

    public void createSubTask(SubTask subTask){ //Создание подзадачи
        int id = getId(listAllTasks);
        subTask.id = id;
        subTask.status = this.STATUS[0];
        subTask.idEpicTask = listAllTasks.get(subTask.idEpicTask).id;
        if (checkId(subTask)){
            listAllTasks.put(id, subTask);
            // todo: как положить в список listSubTasks в классе EpicTask?
        } else {
            System.out.println("Такой Эпик Задачи нет. Введите правильный номер Эпик Задачи");
        }
    }

    public boolean checkId(SubTask subTask){ // Проверка id EpicTask
        if (this.listAllTasks == null) {
            return false;
        }
        return (subTask.idEpicTask == listAllTasks.get(subTask.idEpicTask).id
                && listAllTasks.get(subTask.idEpicTask).type.equals("epictask"));
    }

    public void getListTasks(){ //Получение списка всех задач
        System.out.println("listAllTasks = " + listAllTasks);
    }

    public void deleteAllTasks(){}//Удаление всех задач

    public void getTaskById(){}//Получение задачи по id

    public void updateTask(){}//Обновление задачи

    public void deleteTaskById(){}//Удаление задачи по id

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ManagerTasks that = (ManagerTasks) o;
        return listAllTasks.equals(that.listAllTasks) && Arrays.equals(STATUS, that.STATUS);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(listAllTasks);
        result = 31 * result + Arrays.hashCode(STATUS);
        return result;
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
