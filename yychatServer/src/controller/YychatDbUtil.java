package controller;

import java.sql.Connection;
import java.util.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class YychatDbUtil {
	//��̬��Ա���ַ������͵ķ��ų�����,Ҳ�����Ա
	public static final String MYSQLDRIVER="com.mysql.jdbc.Driver";
	public static final String URL="jdbc:mysql://127.0.0.1:3306/yychat?useUnicode=true&characterEncoding=UTF-8";
	public static final String DBUSER="root";
	public static final String DBPASS="";  //ʵ�������󣩱�����yychat1=new YychatDbUtil();
	                          //             yychat2=new YychatDbUtil();  
	
    
	//1.������������(��̬����),�ص㣺������ص�ʱ����ܱ�����
	public static void loadDriver(){
		try {
			Class.forName(MYSQLDRIVER);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
    
    
    //2.��������,Ĭ��GBK
    public static Connection getConnection(){
    	loadDriver();
    	Connection conn=null;
		try {
			conn = DriverManager.getConnection(URL,DBUSER,DBPASS);
		} catch (SQLException e) {
			e.printStackTrace();
		}
    	return conn;
    }
    
    //������û��ķ���
    public static void addUser(String userName,String passWord){
    	Connection conn=getConnection();
    	String user_Add_Sql="insert into user(userName,passWord,registertimestamp) values(?,?,?)";
        PreparedStatement ptmt=null;
		try {
			ptmt = conn.prepareStatement(user_Add_Sql);
			ptmt.setString(1,userName);
			ptmt.setString(2,passWord);
			//java.util.Date date=new java.util.Date();//�����ȫ�޶���
			Date date=new Date();
			java.sql.Timestamp timestamp=new java.sql.Timestamp(date.getTime());
			ptmt.setTimestamp(3,timestamp);
	        //ִ��preparedStatement
	        int count=ptmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeDB(conn,ptmt);
		}

    }
    //��User���в���userName�û�
    public static boolean seekUser(String userName){
    	boolean seekSuccess=false;
    	Connection conn=getConnection();
    	String user_Seek_Sql="select * from user where username=?";
        PreparedStatement ptmt=null;
        ResultSet rs=null;
		try {
			ptmt = conn.prepareStatement(user_Seek_Sql);
			ptmt.setString(1,userName);
	        //ִ��preparedStatement
	        rs=ptmt.executeQuery();
	        //�жϽ����
	        seekSuccess=rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeDB(conn,ptmt,rs);
		}
    	
    	
    	return seekSuccess;
    }
   
    
    //3.����һ��preparedStatement
    public static boolean loginValidate(String userName,String passWord){	
    	boolean loginSuccess=false;
    	Connection conn=getConnection();
    	String user_Login_Sql="select * from user where username=? and password=?";
        PreparedStatement ptmt=null;
        ResultSet rs=null;
		try {
			ptmt = conn.prepareStatement(user_Login_Sql);
			ptmt.setString(1,userName);
	        ptmt.setString(2,passWord);
	        //4.ִ��preparedStatement
	        rs=ptmt.executeQuery();
	        //5.�жϽ����
	        loginSuccess=rs.next();
		} catch (SQLException e) {
			e.printStackTrace();
		}finally{
			closeDB(conn,ptmt,rs);
		}
        return loginSuccess;
    }
    
    public static String getFriendString(String userName){
    	Connection conn=getConnection();
    	String friend_Relation_Sql="select slaveuser from relation where majoruser=? and relationtype='1'";
    	PreparedStatement ptmt;
    	String friendString="";
		try {
			ptmt = conn.prepareStatement(friend_Relation_Sql);
			ptmt.setString(1,userName);
	        ResultSet rs=ptmt.executeQuery();
	        while(rs.next()){//�ƶ�������е�ָ�룬һ������ȡ�����ѵ�����
	        	//rs.getString(1);
	        	friendString=friendString+rs.getString("slaveuser")+" ";
	        }
		} catch (SQLException e) {
			e.printStackTrace();
		}
        return friendString;
    }
    public static void closeDB(Connection conn,PreparedStatement ptmt,ResultSet rs){
    	if(conn!=null){
    		try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    	if(ptmt!=null){
    		try {
				ptmt.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}if(rs!=null){
    		try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    	}
    }
    	public static void closeDB(Connection conn,PreparedStatement ptmt){//����������
        	if(conn!=null){
        		try {
    				conn.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
        	}
        	if(ptmt!=null){
        		try {
    				ptmt.close();
    			} catch (SQLException e) {
    				e.printStackTrace();
    			}
        	}	 
    }
 }
