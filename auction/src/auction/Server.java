package auction;

import java.net.*;
import java.io.*;
import java.util.*;
import java.sql.*;

public class Server implements Runnable{
    
    Socket socket;
    static Connection con;
    public static Map<String, Integer> map = new HashMap<String, Integer>(); 
    
    public static Vector client = new Vector();
    public static int counter = 0;
    
    public Server(Connection con){
        this.con = con;
        System.out.println("Connection established...\n");
    }
    
    public Server(Socket socket){
        
        try{
            this.socket = socket;
        }catch(Exception e1){e1.printStackTrace();}
    }
   
    
    int id_counter = 0;
    public void run(){
        
        
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream())); 
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            
            client.add(writer);
           
                    while(true){
                    
                    id_counter++;
                    
                    String id = Integer.toString(id_counter);
                    
                    Connection con = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    con = DriverManager.getConnection("jdbc:mysql://localhost:3306/auction?","root","");
                    
                    String query = "SELECT * from players where id = "+ id;
           
                    Statement stmt = con.createStatement();
           
                    ResultSet rs = stmt.executeQuery(query);
                    
                    String name="";
                    while(rs.next()){
                        name = rs.getString("name");
                        //System.out.println(name+"\n");
                    }
                    
                    String data = reader.readLine().trim() + " " + name;
                    System.out.println(data);
                    String name1 = name;
                    
                    //System.out.println("Counter for id- "+id_counter);
                    StringTokenizer st = new StringTokenizer(data);
                    String team_name = st.nextToken();
                    int bid_value = Integer.parseInt(st.nextToken());
                    //System.out.println("Team- "+team_name);
                    //System.out.println("Bid- "+bid_value);
                    map.put(team_name,bid_value);
                    
                  
                        for(int i =0; i <client.size();i++){  
                            
                        try{
                            BufferedWriter bw = (BufferedWriter)client.get(i);
                            
                            bw.write(data);
                            bw.write("\n");
                            bw.flush();
                        
                        }catch(Exception e2){e2.printStackTrace();}
                        }
                        /*for(Map.Entry m:map.entrySet()){  
                            System.out.println("Map "+m.getKey()+" "+m.getValue());  
                           }*/  
                        counter++;
                        if(counter%4 == 0){
                            int max = Collections.max(map.values());
                            String maxkey = "abc";
                             for(Map.Entry m:map.entrySet()){  
                            //System.out.println("Map "+m.getKey()+" "+m.getValue());  
                                 if((int)m.getValue() == max){
                                     maxkey = (String) m.getKey();
                                 }
                             }
                            System.out.println("Max value = "+ max);
                            System.out.println("Max key=  "+ maxkey);
                            
                            
                            if(max == 0){
                                 continue;
                             }else if(maxkey.equals("KXIP")){
                                
                                String query1 = " insert into kxip(name,price)"+ " values (?, ?)";
                                PreparedStatement preparedStmt = con.prepareStatement(query1);
                                preparedStmt.setString (1,name1);
                                preparedStmt.setInt(2,max);
                                preparedStmt.execute();
                             } else if(maxkey.equals("CSK")){
                                
                                String query1 = " insert into csk(name,price)"+ " values (?, ?)";
                                PreparedStatement preparedStmt = con.prepareStatement(query1);
                                preparedStmt.setString (1,name1);
                                preparedStmt.setInt(2,max);
                                preparedStmt.execute();
                             } else if(maxkey.equals("MI")){
                                
                                String query1 = " insert into mi(name,price)"+ " values (?, ?)";
                                PreparedStatement preparedStmt = con.prepareStatement(query1);
                                preparedStmt.setString (1,name1);
                                preparedStmt.setInt(2,max);
                                preparedStmt.execute();
                             } else if(maxkey.equals("RCB")){
                                
                                String query1 = " insert into rcb(name,price)"+ " values (?, ?)";
                                PreparedStatement preparedStmt = con.prepareStatement(query1);
                                preparedStmt.setString (1,name1);
                                preparedStmt.setInt(2,max);
                                preparedStmt.execute();
                             }  
                            
                            
                        }
                        
                } 
         }
        
        catch(Exception e){e.printStackTrace();}
    }
    
    public static void main(String[] args) throws Exception{
        ServerSocket s = new ServerSocket(2999);
        System.out.println("This is server....\n");
        
        System.out.println("M S Dhoni\n");
        System.out.println("Virat Kohli\n");
        System.out.println("Rohit Sharma\n");
        System.out.println("Shikhar Dhawan\n");
        System.out.println("Hardin Pandya\n");
        System.out.println("Ravindra Jadeja\n");
        System.out.println("Jasprit Bumrah\n");
        System.out.println("Mohammad Shami\n");
        System.out.println("Bhuvaneshwar Kumar\n");
        System.out.println("Yuzvendra Chahal\n");
        
        
         
        
        while(true){
            Socket socket = s.accept();
            System.out.println("Client is connecting...\n");
            Server server = new Server(socket);
            Thread thread = new Thread(server);
            thread.start();
        }
    }
}
