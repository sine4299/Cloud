
public class Counter {
	private int c = 0;

    public synchronized void increment( int n) {
        this.c = c + n;
    }

    public synchronized int total() {
        return c;
    }
}
