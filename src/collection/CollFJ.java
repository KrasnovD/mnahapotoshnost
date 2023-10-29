package collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;

public class CollFJ {
    public static void main(String[] args) {
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());

        ForkJoinPool forkJoinPool = new ForkJoinPool();

        AddElementsTask addElementsTask = new AddElementsTask(synchronizedList, 0, 10); // Создаем задачу для добавления элементов в список

        forkJoinPool.invoke(addElementsTask); // Запускаем задачу в ForkJoinPool

        System.out.println("Содержимое списка:");
        synchronized (synchronizedList) {
            for (String element : synchronizedList) {
                System.out.println(element);
            }
        }
    }

    static class AddElementsTask extends RecursiveAction {
        private List<String> synchronizedList;
        private int start;
        private int end;

        public AddElementsTask(List<String> synchronizedList, int start, int end) {
            this.synchronizedList = synchronizedList;
            this.start = start;
            this.end = end;
        }

        @Override
        protected void compute() {
            if (end - start <= 1) {
                synchronized (synchronizedList) {
                    synchronizedList.add("Элемент " + start);
                    System.out.println("Добавлен элемент: " + start);
                }
            } else {
                int middle = (start + end) / 2;
                AddElementsTask leftTask = new AddElementsTask(synchronizedList, start, middle);
                AddElementsTask rightTask = new AddElementsTask(synchronizedList, middle, end);
                invokeAll(leftTask, rightTask);
            }
        }
    }
}
