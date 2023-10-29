package deadlock;

public class Deadlock {
    public static void main(String[] args) throws InterruptedException {
        DeadlockClass firstObjectForDeadlock = new DeadlockClass();
        DeadlockClass secondObjectForDeadlock = new DeadlockClass();

        System.out.println("Creating and starting threads...");

        Thread firstT = new Thread(() -> {
            try {
                firstObjectForDeadlock.firstMethod(secondObjectForDeadlock, 1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
        Thread secondT = new Thread(() -> {
            try {
                secondObjectForDeadlock.firstMethod(firstObjectForDeadlock, 2);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });

        firstT.start();
        secondT.start();

        firstT.join();
        secondT.join();

        System.out.println("End...");
    }

    static class DeadlockClass {
        public synchronized void firstMethod(DeadlockClass deadlockClass, int num) throws InterruptedException {
            System.out.println("First method. Thread num:" + num);

            Thread.sleep(1000); //Типа наша лоджик

            deadlockClass.secondMethod(deadlockClass); // Вызывает второй синк метод, которому в первом потоке нужен монитор secondObjectForDeadlock,
                                                       // который занят вторым потоком, который в свою очередь ждёт монитор firstObjectForDeadlock
            System.out.println("First method: End.");
        }

        public synchronized void secondMethod(DeadlockClass deadlockClass) {
            System.out.println("Second method.");
        }
    }
}
