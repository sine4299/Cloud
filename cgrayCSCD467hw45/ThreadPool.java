import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.time.LocalDateTime;

public class ThreadPool{
	private final int MAX = 50;
	private int currentMax;
	private int pastMax;
	private WorkerThread holders[];
	private boolean stopped;
	private boolean exit = false;

	private MyMonitor jobQueue;

	public class WorkerThread extends Thread{
		int id = 0;
		int sleepTime = 1000;
		boolean stopped = false;

		public WorkerThread(int id){
			this.id = id;
		}

		public void run(){
			Job j;
			while(!stopped){
				try{			
					j = jobQueue.dequeue();
					String command = j.getAction();
					int one = j.getNum1();
					int two = j.getNum2();
					int result = 0;
					if(command.equals("ADD")){
						result = one + two;
					}
					if(command.equals("SUB")){
						result = one - two;
					}
					if(command.equals("DIV")){
						result = one / two;
					}
					if(command.equals("MUL")){
						result = one * two;
					}
					System.out.println("Worker thread id=" + id + 
						               " processed service request " + 
						               command + "," + one + "," + two + 
						               " at the time " + LocalDateTime.now());
					j.printResult(result);
					Thread.sleep(sleepTime);
				}
				catch(InterruptedException e){
					System.out.println("Thread-" + id + " is terminating");
					return;
				}	
			}					
		}
	}

	public ThreadPool(MyMonitor queue){
		holders = new WorkerThread[MAX];
		pastMax = 0;
		currentMax = 5;
		this.jobQueue = queue;
		int i;
		for(i = 0; i < 5; i++){
			WorkerThread t = new WorkerThread(i+1);
			holders[i] = t;
		}
		startPool();
	}

	public void startPool(){
		int i;
		for(i = pastMax; i < currentMax; i++){
			holders[i].start();
		}
	}

	public void increaseThreads(){
		int i;
		pastMax = currentMax;
		currentMax = currentMax * 2;
		for(i = pastMax; i < currentMax; i++){
			WorkerThread t = new WorkerThread(i+1);
			holders[i] = t;
		}
		startPool();
		System.out.println("ThreadManager doubled the number of threads in the pool at " +
						    LocalDateTime.now() + 
						    ", now total running threads in pool is " + 
						    currentMax);
	}

	public void decreaseThreads(){
		int i;
		for(i = pastMax; i < currentMax; i++){
			holders[i].interrupt();
		}
		currentMax = pastMax;
		if(pastMax == 5){
			pastMax = 0;
		}
		else{
			pastMax = pastMax / 2;
		}
		stopPool();
		System.out.println("ThreadManager halfed the number of threads in the pool at " +
						    LocalDateTime.now() + 
						    ", now total running threads in pool is " + 
						    currentMax);
	}

	public void stopPool(){
		int i;
		for(i = pastMax; i < currentMax; i++){
			holders[i].interrupt();
		}
		if(exit){
			System.exit(0);
		}	
	}

	public int numThreadsRunning(){
		return currentMax;
	}

	public void maxCapacity(int max){
		this.currentMax = max;
	}

	public void setExit(boolean exit){
		this.exit = exit;
	}
}