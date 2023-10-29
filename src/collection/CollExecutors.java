package collection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CollExecutors {
    public static void main(String[] args) {
        List<String> synchronizedList = Collections.synchronizedList(new ArrayList<>());

        ExecutorService executorService = Executors.newFixedThreadPool(5);

        for (int i = 0; i < 10; i++) {
            final int index = i;
            executorService.execute(() -> { // Добавляем задачи в пул потоков
                synchronized (synchronizedList) {
                    synchronizedList.add("Элемент " + index);
                    System.out.println("Добавлен элемент: " + index);
                }
            });
        }

        executorService.shutdown(); // Останавливаем ExecutorService после завершения всех задач

        while (!executorService.isTerminated()) { // Ждем, пока все задачи не завершатся
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("Содержимое списка:");
        synchronized (synchronizedList) {
            for (String element : synchronizedList) {
                System.out.println(element);
            }
        }
    }
}
