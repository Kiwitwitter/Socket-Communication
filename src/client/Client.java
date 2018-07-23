package client;

import java.io.*;
import java.net.Socket;

public class Client {
    public static final String IP = "127.0.0.1";
    public static final int PORT = 8081;

    public static void main(String [] args){
        handler();
    }

    private static void handler(){
        try{
            Socket client = new Socket(IP, PORT);
            new Thread(new ReadHandlerThread(client)).start();
            new Thread(new WriteHandlerThread(client)).start();
        }catch(Exception e){
            e.printStackTrace();
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
                System.out.println("Content Received from Server:" + receiver);
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