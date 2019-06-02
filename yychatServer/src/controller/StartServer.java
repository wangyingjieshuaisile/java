package controller;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import com.yychat.model.Message;
import com.yychat.model.User;

public class StartServer {
	ServerSocket ss;
	Socket s;
	Message mess;
	String userName;
	String passWord;
	ObjectOutputStream oos;
	
	
	
	public static HashMap hmSocket=new HashMap<String,Socket>();//���ͣ�ͨ����
	
	public StartServer(){
		try {
	        ss=new ServerSocket(3456);//�������˿ڼ���3456
	        System.out.println("�������Ѿ�����������3456�˿�...");
	        while(true){//?���߳�����
	        	s=ss.accept();//�ȴ��ͻ��˽�������
	            System.out.println(s);//������Ӷ���
	        
	            //�ֽ������� ��װ�� ����������
	            ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
	            User user=(User)ois.readObject();//�����û���¼����user
	            userName=user.getUserName();
	            passWord=user.getPassWord();
	            System.out.println(userName);
	            System.out.println(passWord);
	            //ע�����û�����7���ڷ�������������û���ע��
	            if(user.getUserMessageType().equals("USER_REGISTER")){
	            	//ע�����û�����8����ע���û������в�ѯ,seekSuccessΪtrue������ͬ���û���Ϊfalse��������ͬ���û�
	            	boolean seekSuccess=YychatDbUtil.seekUser(userName);
	            	mess=new Message();
 		            mess.setSender("Server");
 	        	    mess.setReceiver(userName);
	            	
	            	
	            	if(seekSuccess){
	            		//����ע��ʧ�ܵ�message
	            		mess.setMessageType(Message.message_RegisterFailure);
	            	}else{
		            	//ע�����û�����9�����û��ͬ���û��������û������ֺ�����д�뵽user���У�������ע��ɹ���message
	            		YychatDbUtil.addUser(userName,passWord);
	 	        	    mess.setMessageType(Message.message_RegisterFailure);
	            	}
		            sendMessage(s,mess);
	            	s.close();//ע�������Ӧ�ùر�socket����
	            }
	            
	            
	            if(user.getUserMessageType().equals("USER_LOGIN")){
	            	 //ʹ�����ݿ�����֤�û���������
		            boolean loginSuccess=YychatDbUtil.loginValidate(userName,passWord);
		            mess=new Message();
		            mess.setSender("Server");
	        	    mess.setReceiver(user.getUserName());
	        	    if(loginSuccess){
	        	    //��Ϣ���ݣ�����Message����
	        	    	mess.setMessageType(Message.message_LoginSuccess);
	        	    	//�����ݿ�relation���ж�ȡ������Ϣ�����º����б�1����������ȡ��������
		        	    String friendString=YychatDbUtil.getFriendString(userName);   
		        	    mess.setContent(friendString);
		        	    System.out.println(userName+"��ȫ�����ѣ�"+friendString);
		            }
		            else{
		        	    mess.setMessageType(Message.message_LoginFailure);//��֤��ͨ��
		            }
		            sendMessage(s,mess);
		            
		            if(loginSuccess){
		            	//���������ߺ��ѵ�ͼ�경��1�������������û����ȸ��û��ȵ�¼��������Ϣ
		            	mess.setMessageType(Message.message_NewOnlineFriend);
		            	mess.setSender("Server");
		            	mess.setContent(this.userName);//����ͼ����û���
		            	Set friendSet=hmSocket.keySet();
		            	Iterator it=friendSet.iterator();
		            	String friendName;
		            	while(it.hasNext()){
		            		friendName=(String)it.next();//ȡ��һ����������
		            		mess.setReceiver(friendName);
		            		Socket s1=(Socket)hmSocket.get(friendName);
		            		/*oos=new ObjectOutputStream(s1.getOutputStream());
		            		 oos.writeObject(mess);*/
		            		sendMessage(s1,mess);
		            	}
		            	
		            	
		            	//����ÿһ���û���Socket
		                hmSocket.put(userName, s);
		                System.out.println("�����û���Socket"+userName+s);
		                //��ν��տͻ��˵�������Ϣ����һ���߳�������������Ϣ
		                new ServerReceiverThread(s,hmSocket).start();//�����̣߳������߳̾���
		                System.out.println("�����̳߳ɹ�");
		            }
		            
		        }
	            }
	           

		} catch (IOException | ClassNotFoundException e) {
			
			e.printStackTrace();
		}
	}


	private void sendMessage(Socket s,Message mess) throws IOException {
		oos=new ObjectOutputStream(s.getOutputStream());
		oos.writeObject(mess);
	}

	
	public static void main(String[] args) {
		

	}

}
