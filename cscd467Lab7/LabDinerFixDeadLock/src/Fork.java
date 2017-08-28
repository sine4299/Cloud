import java.awt.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class Fork {

  private PhilCanvas display;
  private int identity;
  private boolean taken;
  private final Lock lock = new ReentrantLock();
  
  Fork(PhilCanvas disp, int id)
    { display = disp; identity = id;}
    
  public synchronized put() {
    lock.unlock();
    taken = false;
    display.setFork(identity,false);
  }  

  public synchronized boolean get1()throws java.lang.InterruptedException {
   while(taken){
      wait();
   }
    lock.Lock();
    taken = true;
    display.setFork(identity,false);
    return true;
  }

  public synchronized boolean get2() throws java.lang.InterruptedException {
	int numtries = 0;
   while(numtries < 3){
    if(lock.tryLock()){
      break;
    }
    else{
      numtries++;
      this.wait(100);
    }
  }
  if(numtries == 3){
    this.notifyAll();
    return false;
  }
   taken =true;
	display.setFork(identity,true);
  return true;
  }
}
