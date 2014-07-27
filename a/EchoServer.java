import java.net.*;
import java.io.*;

public class EchoServer {

ServerSocket m_ServerSocket;

public EchoServer() {
    try {
        m_ServerSocket = new ServerSocket(1234);
    } catch (IOException ioe) {
        System.out.println("could not create server socket at 1234 Quitting");
        System.exit(-1);
    }
    System.out.println("Listening for clients on 1234....");
    int id = 0;
    while (true) {
        try {
            Socket clientSocket = m_ServerSocket.accept();
            ClientServiceThread cliThread = new ClientServiceThread(clientSocket, id++);
            cliThread.start();
        } catch (IOException ioe) {
            System.out.println("Exception encountered on accept.Ignoring.StackTrace :");
            ioe.printStackTrace();
        }
    }
}

public static void main(String args[]) {
    new EchoServer();
}

class ClientServiceThread extends Thread {

    Socket m_clientSocket;
    int m_clientID = -1;
    boolean m_bRunThread = true;
	BufferedReader in ;
    PrintWriter out;
        
    ClientServiceThread(Socket s, int clientID) {
        m_clientSocket = s;
        m_clientID = clientID;
    	try{
    	    in = new BufferedReader(new InputStreamReader(m_clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(m_clientSocket.getOutputStream()));
    		System.out.println("Accepted Client :ID - " + m_clientID + " :Address - " + m_clientSocket.getInetAddress().getHostName());
        }catch(Exception e){}
    }

    public void run(){
    	while (m_bRunThread)
	 	{
	 		String op="";
	 		String clientCommand=null;
	 			try{
	 				FileOutputStream fos=new FileOutputStream("R.TXT");
					fos.write(new String("Executed").getBytes());					
	 				fos.close();
	 				clientCommand = in.readLine();
	 			}catch(Exception e){m_bRunThread = false;}
				if(clientCommand!=null){
					System.out.println("Client "+m_clientID+":" + clientCommand);
                	if (clientCommand.equalsIgnoreCase("quit"))
	   				{
                    	m_bRunThread = false;
                    	out.close();
                    	System.out.println("Stopping client thread for client :" + m_clientID);
                	}
	  				else {
	  							Runtime r = Runtime.getRuntime();
								Process p = null;
								try {
									p = r.exec(clientCommand);
									p.waitFor();
									try{
										FileInputStream fis=new FileInputStream("R.TXT");
										byte buffer[]=new byte[28];
										int n=0;
										while((n=fis.read(buffer))!=-1){
											op+=new String(buffer);	
										}
										fis.close();
									}catch (Exception e) {
										op=e.getMessage();
									}	
									
								}
								catch (Exception e) {
									op=e.getMessage();
								}
	  					out.println(op);
           				out.flush();
           				System.out.println(op);
           				
                	}}
            	}
    	}
	}
}
