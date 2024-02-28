   import java.io.*;
   import java.net.*;
import java.util.Base64;
import java.awt.Desktop;
import java.awt.desktop.*;

    public class TCPServer {
       public static void main(String[] args) throws IOException {
      	
			// Variables for setting up connection and communication
         Socket Socket = null; // socket to connect with ServerRouter
         PrintWriter out = null; // for writing to ServerRouter
         BufferedReader in = null; // for reading form ServerRouter
			InetAddress addr = InetAddress.getLocalHost();
			String host = addr.getHostAddress(); // Server machine's IP			
			String routerName = "j263-08.cse1.spsu.edu"; // ServerRouter host name
			int SockNum = 5555; // port number
         
			
			// Tries to connect to the ServerRouter
         try {
            Socket = new Socket(routerName, SockNum);
            out = new PrintWriter(Socket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(Socket.getInputStream()));
         } 
             catch (UnknownHostException e) {
               System.err.println("Don't know about router: " + routerName);
               System.exit(1);
            } 
             catch (IOException e) {
               System.err.println("Couldn't get I/O for the connection to: " + routerName);
               System.exit(1);
            }
				
      	// Variables for message passing			
         String fromServer; // messages sent to ServerRouter
         String fromClient; // messages received from ServerRouter      
 			String address ="10.5.3.196"; // destination IP (Client)
			
			// Communication process (initial sends/receives)
			out.println(address);// initial send (IP of the destination Client)
			fromClient = in.readLine();// initial receive from router (verification of connection)
			System.out.println("ServerRouter: " + fromClient);
			
         String medium = in.readLine();
			// Communication while loop
         FileWriter sizeLogger = new FileWriter("sizelogger.txt");
      	while ((fromClient = in.readLine()) != null) {
            if (fromClient.equals("Bye.")) // exit statement
					   break;
            if (medium=="F"){
               byte[] bytearray = fromClient.getBytes();//May need to be changed to decode method for Base64 Characterset
               FileOutputStream fos = new FileOutputStream("fromUser");//converts the byte array into a file
               fos.write(bytearray);
               Desktop.getDesktop().open(new File("fromUser"));//Will open the file in its default format
               sizeLogger.write("Size of message: "+ fromClient.length());
            }
            else{
               System.out.println("Client said: " + fromClient);
               if (fromClient.equals("Bye.")) // exit statement
					   break;
				   fromServer = fromClient.toUpperCase(); // converting received message to upper case
				   System.out.println("Server said: " + fromServer);
               sizeLogger.write("Size of message: "+ fromClient.length());//Determines length of message
               out.println(fromServer); // sending the converted message back to the Client via ServerRouter
            }
         }
			
			// closing connections
         out.close();
         in.close();
         Socket.close();
         sizeLogger.close();
      }
   }
