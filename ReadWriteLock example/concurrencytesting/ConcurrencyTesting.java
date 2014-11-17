package concurrencytesting;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class Reader implements Runnable {

Lock readLock;

public Reader(Lock readLock) {
this.readLock = readLock;
}

@Override
public void run() {

if(!readLock.tryLock()) {
System.out.println(Thread.currentThread().getName() + " is waiting for reading access...");
readLock.lock();
}

try {
Thread.sleep(5000);
}catch(InterruptedException ex) {}

System.out.println(Thread.currentThread().getName() + " read from the Counter...(" + Counter.getCount() + ")");
readLock.unlock();

}

}

class Writer implements Runnable {

Lock writeLock;

public Writer(Lock writeLock) {
this.writeLock = writeLock;
}

@Override
public void run() {

if(!writeLock.tryLock()) {
System.out.println(Thread.currentThread().getName() + " is waiting for writing access...");
writeLock.lock();
}

try {
Thread.sleep(5000);
Counter.incrementCount();
}catch(InterruptedException ex) {}

System.out.println(Thread.currentThread().getName() + " added by one to the Counter...");
writeLock.unlock();

}

}

class Counter {

private static int count;

public static void incrementCount() {
count++;
}

public static int getCount() {
return count;
}

}

public class ConcurrencyTesting {

public static void main(String[] args) {

ReadWriteLock readWritelock = new ReentrantReadWriteLock();

new Thread(new Writer(readWritelock.writeLock()), "Writer1").start();
new Thread(new Reader(readWritelock.readLock()), "Reader1").start();
new Thread(new Writer(readWritelock.writeLock()), "Writer2").start();
new Thread(new Reader(readWritelock.readLock()), "Reader2").start();
}
}