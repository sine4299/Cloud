import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
  * A simple Swing-based client for the capitalization server.
  * It has a main frame window with a text field for entering
  * strings and a textarea to see the results of capitalizing
  * them.
  */
public class TestClient extends Thread{

      private static BufferedReader in;
      private static PrintWriter out;
      private static int MAXCLIENTS = 10;

      public void run(){
          int i;
          String[] s = {"ADD,4,5", "SUB,10,9", "MUL,2,3", "DIV,4,2"};
          try{
            for(i = 0; i < s.length; i++){ 
                out.println(s[i]);
            }
            Thread.sleep(9000);
          }  
          catch(InterruptedException e){}
      }

      public static void connectToServer() throws IOException {

            // Get the server address from a dialog box.
            String serverAddress = "localhost";

            // Make connection and initialize streams
            Socket socket = new Socket(serverAddress, 9898);
            in = new BufferedReader(
                        new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream(), true);

            // Consume the initial welcoming messages from the server
            /*for (int i = 0; i < 3; i++) {
                 System.out.println(in.readLine() + "\n");
            }*/
      }

      public static void main(String[] args) throws Exception {
            TestClient[] clientArr = new TestClient[MAXCLIENTS];
            for(int i = 0; i < MAXCLIENTS; i++){
              TestClient tc = new TestClient();
              tc.connectToServer();
              tc.start();
              clientArr[i] = tc;
            }
            out.println("KILL");
      }
}