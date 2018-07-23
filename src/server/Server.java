package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static final int PORT = 8081; //port listening

    public static void main(String [] args){
        Server server = new Server();
        server.init();
    }

    public void init(){
        ServerSocket serverSocket = null;
        try{
            serverSocket = new ServerSocket(PORT);
            while(true){
                Socket client = serverSocket.accept();
                new Thread(new ReadHandlerThread(client)).start();
                new Thread(new WriteHandlerThread(client)).start();
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(serverSocket != null){
                    serverSocket.close();
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}

class ReadHandlerThread implements Runnable{
    private Socket client;

    public ReadHandlerThread(Socket client){
        this.client = client;
    }

    @Override
    public void run(){
        DataInputStream dis = null;
        try{
            while(true){
                dis = new DataInputStream(client.getInputStream());
                String receiver = dis.readUTF();
                System.out.println("Content Received from client:" + receiver);
            }
        }catch(Exception e) {
            e.printStackTrace();
        }finally{
            try{
                if(dis != null){
                    dis.close();
                }
                if(client != null){
                    client = null;
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
}

class WriteHandlerThread implements Runnable{
    private Socket client;

    public WriteHandlerThread(Socket client){
        this.client = client;
    }

    @Override
    public void run(){
        DataOutputStream dos = null;
        BufferedReader br = null;
        try{
            while(true){
                //Reply to client
                dos = new DataOutputStream(client.getOutputStream());
                System.out.print("Please enter contents that you are sending:");
                br = new BufferedReader(new InputStreamReader(System.in));
                String send = br.readLine();
                dos.writeUTF(send);
            }
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            try{
                if(dos!=null){
                    dos.close();
                }
                if(br!=null){
                    br.close();
                }
                if(client!=null){
                    client = null;
                }
            }catch(IOException e){
                e.printStackTrace();
            }
        }
    }
}