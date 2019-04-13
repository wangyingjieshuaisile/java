package controller;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

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
				
				//��2�����������ߺ�����Ϣ���ͻ���
				if(mess.getMessageType().equals(Message.message_RequestOnlineFriend)){
					//����Ҫ�õ����ߺ�����Ϣ
					Set friendSet=StartServer.hmSocket.keySet();
					Iterator it=friendSet.iterator();//��������Ŀ���Ƕ�friendSet�����е�Ԫ�ؽ��б���
					String friendName;
					String friendString=" ";
					while(it.hasNext()){
						friendName=(String)it.next();
						if(!friendName.equals(mess.getSender()))//�ų��Լ�
							friendString=friendString+friendName+" ";//ΪʲôҪ�ӿո�
					}
					System.out.println("ȫ�����ѵ����֣�"+friendString);
					//�ٷ���
					
				}
				
			} catch (IOException | ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}
	
}