import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Job{
	String action;
	int num1;
	int num2;
	PrintWriter out;
	Socket socket;

	public Job(String action, int num1, int num2, Socket socket){
		this.action = action;
		this.num1 = num1;
		this.num2 = num2;
		this.socket = socket;
		try{
			this.out = new PrintWriter(socket.getOutputStream(),true);
		}
		catch(IOException e){}
	}

	public String getAction(){
		return this.action;
	}

	public int getNum1(){
		return num1;
	}

	public int getNum2(){
		return num2;
	}

	public void printResult(int result){
		out.println(num1 + " " + action + " " + num2 + " = " + result);
	}

	public Socket getSocket(){
		return this.socket;
	}
}