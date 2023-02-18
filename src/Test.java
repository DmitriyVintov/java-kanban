public final class Test {
    private Test() {
    }

    public static void test() {
        ManagerTasks managerTasks = new ManagerTasks();
        /**
         * Инициализация задач
         */
        Task newT1 = new Task("Помыть полы", "До вечера помыть во всем доме");
        Task newT2 = new Task("Починить розетки", "На первом этаже");
        EpicTask newE1 = new EpicTask("Переезд", "Переезд в новый дом");
        SubTask newS1 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 2);
        SubTask newS2 = new SubTask("Собрать коробки", "Положить одежду в коробки", 2);
        EpicTask newE2 = new EpicTask("Переезд", "Переезд в новый дом");
        SubTask newS3 = new SubTask("Вынести мусор", "Выкинуть старое пианино", 5);
        SubTask newS4 = new SubTask("Собрать коробки", "Положить одежду в коробки", 5);

        /**
         * Создание задач
         */
        System.out.println("Создание задач");
        int taskN1 = managerTasks.createTask(newT1);
        int taskN2 = managerTasks.createTask(newT2);
        int epicN1 = managerTasks.createEpicTask(newE1);
        int subN1 = managerTasks.createSubTask(newS1);
        int subN2 = managerTasks.createSubTask(newS2);
        int epicN2 = managerTasks.createEpicTask(newE2);
        int subN3 = managerTasks.createSubTask(newS3);
        int subN4 = managerTasks.createSubTask(newS4);

        /**
         * Проверка записи созданных задач
         */
        System.out.println("\nmanagerTasks.getTasksRepo() = " + managerTasks.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());

        /**
         * Проверка поиска задач по id
         */
        System.out.println("\nПроверка поиска по id");
        System.out.println("managerTasks.getTaskById(taskN1) = " + managerTasks.getTaskById(taskN1));
        System.out.println("managerTasks.getEpicTaskById(epicN1) = " + managerTasks.getEpicTaskById(epicN1));
        System.out.println("managerTasks.getSubTaskById(subN2) = " + managerTasks.getSubTaskById(subN2));

        /**
         * Изменение статусов
         */
        System.out.println("\nИзменение статусов");
        managerTasks.updateTask(newT2, Status.DONE);
        managerTasks.updateSubTask(newS1, Status.IN_PROGRESS);
        managerTasks.updateSubTask(newS2, Status.DONE);
        managerTasks.updateSubTask(newS3, Status.DONE);
        managerTasks.updateSubTask(newS4, Status.DONE);

        /**
         * Проверка изменения статусов задач
         */
        System.out.println("\nПроверка изменения статусов");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());

        /**
         * Удаление задач по id
         */
        System.out.println("\nУдаление по id");
        managerTasks.deleteTaskById(taskN2);
        managerTasks.deleteEpicTaskById(epicN2);
        managerTasks.deleteSubTaskById(subN2);

        /**
         * Проверка удаления задач по id
         */
        System.out.println("\nПроверка удаления по id");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());

        /**
         * Удаление всех задач
         */
        System.out.println("\nУдаление всех задач");
        managerTasks.deleteAllTasks();
        managerTasks.deleteAllEpicTasks();
        managerTasks.deleteAllSubTasks();

        /**
         * Проверка удаления всех задач
         */
        System.out.println("\nПроверка удаления всех задач");
        System.out.println("managerTasks.getTasksRepo() = " + managerTasks.getTasksRepo());
        System.out.println("managerTasks.getEpicTasksRepo() = " + managerTasks.getEpicTasksRepo());
        System.out.println("managerTasks.getSubTasksRepo() = " + managerTasks.getSubTasksRepo());


    }

}
