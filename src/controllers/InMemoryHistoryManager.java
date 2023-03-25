package controllers;

import models.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Класс реализует интерфейс менеджера истории просмотров. Собственная реализация связанного списка
 */
public class InMemoryHistoryManager implements HistoryManager<Task> {
    /**
     * Первый элемент (Node) связанного списка
     */
    Node<Task> first;
    /**
     * Последний элемент (Node) связанного списка
     */
    Node<Task> last;
    /**
     * Неупорядоченная таблица элементов истории просмотров
     */
    private final HashMap<Integer, Node<Task>> nodes = new HashMap<>();

    @Override
    public void add(Task task) {
        if (task != null) {
            if (nodes.containsKey(task.getId())) remove(task.getId());
            nodes.put(task.getId(), linkLast(task));
        }
    }

    @Override
    public List<Task> getHistory() {
        List<Task> history = new ArrayList<>();
        Node<Task> target = first;
        while (target != null) {
            history.add(target.item);
            target = target.next;
        }
        return history;
    }

    @Override
    public void remove(int id) {
        Node<Task> target = nodes.get(id);
        if (target == null) return;
        if (target == first) {
            target.next.prev = null;
            first = target.next;
            nodes.remove(id);
            return;
        }
        if (target == last) {
            target.prev.next = null;
            last = target.prev;
            nodes.remove(id);
            return;
        }
        target.prev.next = target.next;
        target.next.prev = target.prev;
        nodes.remove(id);
    }

    /**
     * Метод создания связанного списка истории просмотров
     *
     * @param task
     * @return Элемент истории просмотров, присоединенный к связанному списку
     */
    private Node<Task> linkLast(Task task) {
        final Node<Task> newNode = new Node<>(last, task, null);
        if (first == null & last == null) {
            first = newNode;
        } else if (last == null) {
            first.next = newNode;
            newNode.prev = first;
            last = newNode;
        } else {
            last.next = newNode;
            last = newNode;
        }
        return newNode;
    }

    /**
     * Класс-модель элемента (Node) истории просмотров
     *
     * @param <T>
     */
    private static class Node<T extends Task> {
        T item;
        Node<T> next;
        Node<T> prev;

        Node(Node<T> prev, T element, Node<T> next) {
            this.item = element;
            this.next = next;
            this.prev = prev;
        }
    }
}