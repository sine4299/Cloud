import java.util.LinkedList;
import java.util.Queue;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;

public class MyMonitor{

  private final Queue<Job> queue;
  private int SIZE = 50;
  private int currentSize = 0;
  private boolean full = false;
  private boolean wait = false;

  public MyMonitor(){
      queue = new LinkedList<Job>();
   }  
  
   public synchronized void enqueue(Job j) {
      if (full) {
         Socket s = j.getSocket();
         //System.out.println("Queue is full");
         try{
          s.close();
         }
         catch(IOException e){}
         //throw new FullException();
      }
      queue.add(j);
      currentSize++;
      if(currentSize == SIZE){
         isFull();
      }
      notify();
   }

   public synchronized Job dequeue() {
      while (!full && currentSize == 0) {
        try {
          wait();
        }
        catch (InterruptedException e) {}
      }
      if (currentSize == 0) {
         return null;
      }
      currentSize--;
      if(currentSize < SIZE){
         notFull();
      }
      return queue.remove();
   }
  
   public synchronized int size() {
      return currentSize;
   }
  
   public synchronized void isFull() {
      full = true;
      notifyAll();
   }
  
   public synchronized void notFull() {
      full = false;
   }
  /* public static class FullException extends RuntimeException {
      public FullException() {
          super("Queue is full.");
      }
  }*/
}