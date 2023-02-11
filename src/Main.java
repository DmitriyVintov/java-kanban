public class Main {

    public static void main(String[] args) {
        test();
    }
    public static void test(){
        ManagerTasks managerTasks = new ManagerTasks();
        managerTasks.createTask(new Task("Помыть полы", "До вечера помыть во всем доме"));
        managerTasks.createTask(new Task("Починить розетки", "На первом этаже"));
        managerTasks.createTask(new EpicTask("Переезд", "Переезд в новый дом"));
        managerTasks.createSubTask(new SubTask("Вынести мусор", "Выкинуть старое пианино", 1));
        managerTasks.createSubTask(new SubTask("Собрать коробки", "Положить одежду в коробки", 2));
        managerTasks.getListTasks();
    }
}
