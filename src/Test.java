public final class Test {
    private Test() {}

    public static void test(){
        ManagerTasks managerTasks = new ManagerTasks();
        // Далее в комментариях id элементов, т.к. сквозная нумерация
        Task newT1 = new Task("Помыть полы", "До вечера помыть во всем доме"); // 0
        Task newT2 = new Task("Починить розетки", "На первом этаже"); // 1
        EpicTask newE1 = new EpicTask("Переезд", "Переезд в новый дом"); // 2
        SubTask newS1 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 2); // 3
        SubTask newS2 = new SubTask("Собрать коробки", "Положить одежду в коробки", 2); // 4
        EpicTask newE2 = new EpicTask("Переезд", "Переезд в новый дом"); // 5
        SubTask newS3 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 5); // 6
        SubTask newS4 = new SubTask("Собрать коробки", "Положить одежду в коробки", 5); // 7
        System.out.println("Создание задач");
        int taskN1 = managerTasks.createTask(newT1); // Создание задач
        int taskN2 = managerTasks.createTask(newT2);
        int epicN1 = managerTasks.createEpicTask(newE1);
        int subN1 = managerTasks.createSubTask(newS1);
        int subN2 = managerTasks.createSubTask(newS2);
        int epicN2 = managerTasks.createEpicTask(newE2);
        int subN3 = managerTasks.createSubTask(newS3);
        int subN4 = managerTasks.createSubTask(newS4);
        System.out.println("\nПроверка поиска по id");
        System.out.println("managerTasks.getTaskById(taskN1) = " + managerTasks.getTaskById(taskN1)); // Проверка поиска задач по id
        System.out.println("managerTasks.getEpicTaskById(epicN1) = " + managerTasks.getEpicTaskById(epicN1));
        System.out.println("managerTasks.getSubTaskById(subN2) = " + managerTasks.getSubTaskById(subN2));
        System.out.println("\nmanagerTasks.getTasksRepo() = " + managerTasks.getTasksRepo()); // Проверка изменения статусов
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());
        System.out.println("\nИзменение статусов");
        managerTasks.updateTask(newT2, Status.DONE); // Изменение статусов
        managerTasks.updateSubTask(newS1, Status.IN_PROGRESS);
        managerTasks.updateSubTask(newS2, Status.DONE);
        managerTasks.updateSubTask(newS3, Status.DONE);
        managerTasks.updateSubTask(newS4, Status.DONE);
        System.out.println("\nПроверка изменения статусов");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo()); // Проверка изменения статусов
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());
        System.out.println("\nУдаление по id");
        managerTasks.deleteTaskById(taskN2); // Удаление по id
        managerTasks.deleteEpicTaskById(epicN2);
        managerTasks.deleteSubTaskById(subN2);
        System.out.println("\nПроверка удаления по id");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo()); // Проверка удаления по id
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());
        System.out.println("\nУдаление всех задач");
        managerTasks.deleteAllTasks(); // Удаление всех задач
        managerTasks.deleteAllEpicTasks();
        managerTasks.deleteAllSubTasks();
        System.out.println("\nПроверка удаления всех задач");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo()); // Проверка удаления всех задач
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());
    }

}
