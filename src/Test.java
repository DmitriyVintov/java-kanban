public class Test {
    public static void test(){
        ManagerTasks managerTasks = new ManagerTasks();
        managerTasks.createTask(new Task("Помыть полы", "До вечера помыть во всем доме"));
        managerTasks.createTask(new Task("Починить розетки", "На первом этаже"));
        managerTasks.createEpicTask(new EpicTask("Переезд", "Переезд в новый дом"));
        managerTasks.createSubTask(new SubTask("Вынести мусор", "Выкинуть старое пианино", 0));
        managerTasks.createSubTask(new SubTask("Собрать коробки", "Положить одежду в коробки", 2));

        managerTasks.getAllTasks();
        managerTasks.getTaskById(2);
        managerTasks.getTaskById(1);
        managerTasks.getEpicTaskById(3);
        managerTasks.getEpicTaskById(0);
        managerTasks.getSubTaskById(0, 0);
        managerTasks.getSubTaskById(-1, -1);
        managerTasks.getSubTaskById(0, 3);
        managerTasks.getSubTaskById(4, 0);

    }

}
