//package com.chatserver.controller;
//
//import java.io.IOException;
//import java.io.ObjectInputStream;
//import java.io.ObjectOutputStream;
//import java.net.ServerSocket;
//import java.net.Socket;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.PreparedStatement;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Set;
//
//import com.yychat.model.Message;
//import com.yychat.model.User;
//
//public class StartServer {
//	ServerSocket ss;
//	Socket s;
//	
//	public static HashMap hmSocket=new HashMap<String,Socket>();//���ͣ�ͨ����
//	String userName;
//	String passWord;
//	public StartServer(){
//		try {
//			ss=new ServerSocket(3456);//�������˿ڼ���3456
//			System.out.println("�������Ѿ�����������3456�˿�...");
//			//1��������������
//			Class.forName("com.mysql.jdbc.Driver");
//			
//			//2����������,Ĭ��GBK
//			//String url="jdbc:mysql://127.0.0.1:3306/yychat";
//			String url="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
//			String dbuser="root";
//			String dbpass="";				
//			Connection conn=DriverManager.getConnection(url,dbuser,dbpass);
//			System.out.println("���ݿ����ӳɹ���");
//			
////			while(true){//?���߳�����
////				s=ss.accept();//�ȴ��ͻ��˽�������
////				System.out.println(s);//�������Socket����
////				
////				
////				//�ֽ������� ��װ�� ����������
////				ObjectInputStream ois=new ObjectInputStream(s.getInputStream());
////				User user=(User)ois.readObject();//�����û���¼����user
////				this.userName=user.getUserName();
////				System.out.println(user.getUserName());
////				System.out.println(user.getPassWord());
//				
//				//3������һ��preparedStatement
//				String user_Login_Sql="select * from user where username=? and password=?";
//				PreparedStatement ptmt=conn.prepareStatement(user_Login_Sql);
//				ptmt.setString(1, userName);
//				ptmt.setString(2, passWord);
//				
//				//4��ִ��preparedStatement
//				ResultSet rs=ptmt.executeQuery();
//				
//				//5�����ݽ�������ж��Ƿ��ܵ�¼
//				boolean loginSuccess=rs.next();	
//
//				
//				//Server����֤�����Ƿ�123456��
//				Message mess=new Message();
//				mess.setSender("Server");
//				mess.setReceiver(user.getUserName());
//				//if(user.getPassWord().equals("123456")){//������"==",����Ƚ�
//				if(loginSuccess){
//					//��Ϣ���ݣ�����һ��Message����				
//					mess.setMessageType(Message.message_LoginSuccess);//��֤ͨ��		
//					
//					//�����ݿ�relation�ж�ȡ������Ϣ�����º����б�
//					//1����������ȡ�������ݳ���
//					String friend_Relation_Sql="select * from relation where masteruser=? and relationtype='1'";
//					ptmt=conn.prepareStatement(friend_Relation_Sql);
//					ptmt.setString(1, userName);
//					rs=ptmt.executeQuery();
//					String friendString="";
//					while(rs.next()){//�ƶ�������е�ָ��,һ������ȡ�����ѵ�����
//						//rs.getString("1");
//						friendString=friendString+rs.getString("slaveuser"+"");
//					}
//					mess.setContent(friendString);//���������Ϣ��mess�����content��Ա
//					System.out.println(userName+"��ȫ�����ѣ�"+friendString);
//				}
//				else{				
//					mess.setMessageType(Message.message_LoginFailure);//��֤��ͨ��	
//					System.out.println("������֤ʧ��");
//				}		
//				sendMessage(s,mess);
//				
//				if(loginSuccess){
//					//���������ߺ��ѵ�ͼ�경��1�������������û����ȸ��û��ȵ�¼��������Ϣ
//					mess.setMessageType(Message.message_NewOnlineFriend);
//					mess.setSender("Server");
//					mess.setContent(this.userName);//����ͼ����û���
//					Set friendSet=hmSocket.keySet();
//					Iterator it=friendSet.iterator();
//					String friendName;
//					while(it.hasNext()){
//						friendName=(String)it.next();//ȡ��ÿһ�����ѵ�����
//						mess.setReceiver(friendName);
//						Socket s1=(Socket)hmSocket.get(friendName);
//						sendMessage(s1,mess);
//					}
//					//����ÿһ���û���Ӧ��Socket
//					hmSocket.put(userName,s);
//					System.out.println("�����û���Socket"+userName+s);
//					//��ν��տͻ��˵�������Ϣ������һ���߳�������������Ϣ
//					new ServerReceiverThread(s,hmSocket).start();//�����̣߳������߳̾���
//					System.out.println("�����̳߳ɹ�");
//				}
//			}				
//			
//		} catch (IOException | ClassNotFoundException |SQLException e) {			
//			e.printStackTrace();
//		}
//	}
//	private void sendMessage(Socket s, Message mess) throws IOException{
//		ObjectOutputStream oos = new ObjectOutputStream(s.getOutputStream());
//		oos.writeObject(mess);
//		
//	}
//}
//