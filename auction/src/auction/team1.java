package auction;


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.io.*;
import java.net.*;

public class team1 extends JFrame implements ActionListener, Runnable{
    JPanel p1;
    JTextField t1;
    JButton b1;
    JTextArea a1;
    
    BufferedWriter writer;
    BufferedReader reader;
    
    team1(){
        ImageIcon i1 = new ImageIcon(ClassLoader.getSystemResource("auction/kxip.png"));
        Image i2 = i1.getImage().getScaledInstance(170, 170, Image.SCALE_DEFAULT);
        ImageIcon i3 = new ImageIcon(i2);
        JLabel l1 = new JLabel(i3);
        l1.setBounds(120 , 5, 170,170);
        add(l1);
        
        t1 = new JTextField();
        t1.setBounds(5,600,310,40);
        t1.setFont(new Font("SAN_SERIF",Font.PLAIN,16));
        add(t1);
        
        b1 = new JButton("BID");
        b1.setBounds(330,600,70,40);
        b1.setBackground(new Color(237, 29, 36));
        b1.addActionListener(this);
        add(b1);
        
        a1 = new JTextArea();
        a1.setBounds(5,200,400,380);
        a1.setBackground(Color.PINK);
        a1.setFont(new Font("SAN_SERIF",Font.BOLD,20));
        a1.setEditable(false);
        a1.setLineWrap(true);
        add(a1);
        

        
        setLayout(null);
        setSize(450,700);
        setVisible(true);
        
        try{
            
            Socket socketClient = new Socket("localhost",2999);
            writer = new BufferedWriter(new OutputStreamWriter(socketClient.getOutputStream()));
            reader = new BufferedReader(new InputStreamReader(socketClient.getInputStream()));
            
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }
    

    @Override
    public void actionPerformed(ActionEvent e) {
        String str = "KXIP " + t1.getText();
        try{
            writer.write(str);
            writer.write("\n");
            writer.flush();
        }catch(Exception e1){e1.printStackTrace();}
        t1.setText("");
    }
    
    public void run(){
        try{
            String msg = "";
            while((msg = reader.readLine()) != null){
                a1.append(msg + "\n");
            }
        }
        catch(Exception e2){e2.printStackTrace();}
    }
    
public static void main(String[] args){
        System.out.println("This is client 1...");
        team1 one = new team1();
        //setVisible(true);
        Thread t1 = new Thread(one);
        t1.start();
    }
}

