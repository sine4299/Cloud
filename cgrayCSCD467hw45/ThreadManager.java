public class ThreadManager extends Thread{
	ThreadPool pool;
	MyMonitor mm;
	int nt, nj, t1 = 10, t2 = 20, t3 = 50, v = 1000;

	public ThreadManager(ThreadPool pool, MyMonitor mm){
		this.pool = pool;
		this.mm = mm;
	}

	public void run(){
		while(true){
			try{
				Thread.sleep(v);
				nt = pool.numThreadsRunning();
				nj = mm.size();
				//System.out.println("Number of threads running: " + nt + " Number of jobs: " + nj);
				if(nj <= t1 && nt > 5){
					pool.decreaseThreads();
				}
				else if(t1 < nj && nj <= t2 && nt < 10){
					pool.increaseThreads();
				}
				else if(t1 < nj && nj <= t2 && nt > 10){
					pool.decreaseThreads();
				}
				else if(t1 < nj && nj <= t3 && nt < 20){
					pool.increaseThreads();
				}
				else if(t1 < nj && nj <= t3 && nt > 20){
					pool.decreaseThreads();
				}
			}
			catch(InterruptedException e){
				return;
			}
		}
	}
} 