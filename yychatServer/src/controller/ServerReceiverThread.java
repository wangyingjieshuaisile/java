package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;

public class ServerReceiverThread extends Thread{//����Ҫ��run����
	Socket s;
	HashMap hmSocket;
	public ServerReceiverThread(Socket s){
		this.s=s;
		this.hmSocket=hmSocket;
		
		
	}
	public void run(){
		ObjectInputStream ois;
		
		while(true){
			try {
				//������Ϣ
				ois=new ObjectInputStream(s.getInputStream());
				Message mess=(Message)ois.readObject();//�����û���������������Ϣ������
				System.out.println("�ȴ��û�����������Ϣ");
				System.out.println(mess.getSender()+"��"+mess.getReceiver()+"˵:"+mess.getContent());
				
				//ת����Ϣ
				if(mess.getMessageType().equals(Message.message_Common)){
					Socket s1=(Socket)StartServer.hmSocket.get(mess.getReceiver());
					ObjectOutputStream oos=new ObjectOutputStream(s1.getOutputStream());
					oos.writeObject(mess);//ת��Massage
					System.out.println("������ת������Ϣ"+mess.getSender()+"��"+mess.getReceiver()+"˵:"+mess.getContent());
				}
				
			} catch (IOException | ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
}
