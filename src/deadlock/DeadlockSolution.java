package deadlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class DeadlockSolution {
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

        private ReentrantLock lock = new ReentrantLock();

        public synchronized void firstMethod(DeadlockClass deadlockClass, int num) throws InterruptedException {
            System.out.println("First method. Thread num: " + num);

            Thread.sleep(1000); //Типа наша лоджик

            if (deadlockClass.lock.tryLock(1000, TimeUnit.MILLISECONDS)) {
                deadlockClass.secondMethod(deadlockClass);
            }

            System.out.println("First method end. Thread num: " + num);
        }

        public void secondMethod(DeadlockClass deadlockClass) {
            System.out.println("Second method.");
        }
    }
}
