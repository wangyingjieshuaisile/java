package com.yychat.controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;

import com.yychat.model.Message;
import com.yychat.model.User;

public class ClientConnetion {
	//public static Socket s;//��̬��Ա����
	public Socket s;//�Ǿ�̬��Ա����
	public static HashMap hmSocket=new HashMap<String,Socket>();
			public ClientConnetion(){
		try {//�쳣����ṹ
			s=new Socket("127.0.0.1",3456);//������ַ���ز��ַ
			System.out.println("�ͻ���Socket"+s);
		} catch (IOException e) {
			
			e.printStackTrace();
		}
	}
			
    //ע�����û�5������registerUserIntoDB����������user���󵽷������ˣ����ҽ��շ��������ص�message
	public boolean registerUserIntoDB(User user){
		boolean registerSuccess=false;
		ObjectOutputStream oos;
		Message mess=null;
		try {//���ֽ���������� ��װ�� �������������
			 oos=new ObjectOutputStream(s.getOutputStream());
			 oos.writeObject(user);
			 
			 ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			 mess=(Message)ois.readObject();//���յ���¼�Ƿ�ɹ���mess
			 if(mess.getMessageType().equals(Message.message_RegisterSuccess))
				 registerSuccess=true;
			 s.close();//�رշ������˵�socket����
        } catch (IOException | ClassNotFoundException e) {
			e.printStackTrace();
		}
		return registerSuccess;
		
	}
	
	public Message loginValidate(User user){
		//��������������
		ObjectOutputStream oos;
		Message mess=null;
		try {//���ֽ���������� ��װ�� �������������
			 oos=new ObjectOutputStream(s.getOutputStream());
			 oos.writeObject(user);
			 
			 ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
			 mess=(Message)ois.readObject();//����
			 
			 //��½�ɹ�����Socket����hmSocket��
             if(mess.getMessageType().equals(Message.message_LoginSuccess)){
            	 System.out.println(user.getUserName()+" �ŵ�½�ɹ�");
				 hmSocket.put(user.getUserName(),s);
				 new ClientReceiverThread(s).start();
			 }
			 
		} catch (IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
		return mess;
	}
}
