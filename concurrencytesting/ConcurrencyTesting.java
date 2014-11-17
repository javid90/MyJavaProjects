
package concurrencytesting;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author Javid
 */



public class ConcurrencyTesting {

    
    
    
    static class Location {
        
        public static final Lock location = new ReentrantLock();
        public static final Condition arrivalEvent = location.newCondition();
        
    } 
    
    static class Person extends Thread {
        
        
        public Person(String name) {
            setName(name);
        }
        
        @Override
        public void run() {
            
            try {
                
                Thread.sleep(2000);
                Location.location.lock();
                Thread.sleep(3000);
                System.out.println(this.getName() + " has arrived at the exit of London metro station...");
                
                if(this.getName().equals("Edward"))
                    Location.arrivalEvent.signalAll();
                
            } catch (InterruptedException ex) {
            
            } finally {
                Location.location.unlock();
            }
            
            
            
        }
        
    }
    
    
    static class SearcherPerson extends Thread {
        
        public SearcherPerson(String name) {
            setName(name);
        }
        
        @Override
        public void run() {
            
            System.out.println(this.getName() + " is looking for Edward at the exit of Nizami metro station...");
            Location.location.lock();
            
            try {
                Location.arrivalEvent.await();
                System.out.println("Finally, " + this.getName() + " had found Edward and they met...");
            } catch (InterruptedException ex) {
                
            } finally {
                Location.location.unlock();
            }
            
        }
        
    }
    
    
    public static void main(String[] args) {
       
        Thread searcherPerson = new SearcherPerson("Javid");
        Thread person1 = new Person("Mike");
        Thread person2 = new Person("Edward");
        Thread person3 = new Person("Kate");
        
        searcherPerson.start();
        person1.start();
        person2.start();
        person3.start();
        
        
        
    }
}
