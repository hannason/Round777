package connect6;
/*
 1) �씪諛� 諛붾몣�뙋 �궗�슜 (19x19)

2) ��蹂� �봽濡쒓렇�옩 �꽕移섎맂 而댄벂�꽣 1���� �꽑�닔 1紐� 異쒖쟾

3) 寃뚯엫�쓽 �떎�뼇�꽦�쓣 �넂�씠湲� �쐞�빐 �떖�뙋�씠 0~5媛쒖쓽 李⑹닔湲덉��젏�쓣 �엫�쓽濡� 諛곗튂

4) �쓳諛깆씠 援먮�濡� �븳踰덉뿉 2媛쒖뵫 諛붾몣�룎�쓣 �넃�뒗�뜲 �쓳�� 泥レ닔�뿉 �룎 �븯�굹留� �젙以묒븰�뿉 李⑹닔

5) �긽�����쓽 2�닔瑜� �옄�떊�쓽 �� �봽濡쒓렇�옩�뿉 �엯�젰

6) �� �봽濡쒓렇�옩�씠 �젣�떆�븳 2�닔瑜� 洹몃�濡� 諛붾몣�뙋�뿉 李⑹닔

    (李⑹닔 湲덉��젏�뿉 李⑹닔�븳 �룎�� 臾댁떆) (30珥� �젣�븳)

7) 6媛� �씠�긽�쓽 �룎�씠 媛숈��깋�쑝濡� �씪�젹濡� 留뚮뱶�뒗 ���씠 �듅由�

8) 3x3怨� 媛숈� �듅蹂꾪븳 湲덉� �뾾�쓬

9) �쓳諛깆� 寃쎄린吏곸쟾 寃곗젙 
 */

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Stack;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class ConnectSix extends JFrame {
	static public JPanel connectSixPanel;
	static public DrawLine draw;
	static public boolean ban=false;
	static boolean press=false;
	static public int banNumber=0;
	static public boolean start=false;
	static public Point[][] checkDot=  new Point [19][19];
	static public int[][] check=  new int [19][19];
	static public int[][] weight = new int[19][19];
	static public int count=0, pointX, pointY;
	static public Point prev, dot;
	static JTextField inputX, inputY;
	JLabel lbX, lbY;
	
	ConnectSix(){
		setTitle("connectSix");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setScreen();
		setSize(1000, 1000);
		setLocationRelativeTo(null);
        setVisible(true);

    }
    
    public void setScreen() {
    	JPanel containerPanel = new JPanel();
    	containerPanel.setLayout(null);
    	connectSixPanel = new JPanel();
    	connectSixPanel.setBounds(50,50,800,800);
    	connectSixPanel.setBackground(Color.YELLOW);
  
        for(int i=0; i<19; i++) { 
        	for(int j=0; j<19; j++) {
        		ConnectSix.check[i][j] =-1 ;
        		weight[i][j] = 0;
        	}
        }
    	draw = new DrawLine();
    	draw.setBounds(0,0,800,800);
    	connectSixPanel.add(draw);
    	
    	
    	JTextField bandN = new JTextField();
    	bandN.setBounds(850,50,50,50);
    	containerPanel.add(bandN);
    	
    	JButton banbtn = new JButton("착수 금지돌 설정");
    	banbtn.setBounds(900,50,100,50);
    	banbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	banNumber=Integer.parseInt(bandN.getText());
       		 	ban=true;
       		 	System.out.println("main "+banNumber);
       		 	draw.repaint();
            }
        });
    	containerPanel.add(banbtn);
    	
    	JButton startbtn = new JButton("게임 시작");
    	startbtn.setBounds(900,120,100,50);
    	startbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ban=false;
       		 	count=1;
       		 	start=true;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(startbtn);
    	
    	JButton resetbtn = new JButton("새로고침");
    	resetbtn.setBounds(900,180,100,50);
    	resetbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ban=false; 
       		 	count=1;
       		 	start=false;
       		 	
       	        for(int i=0; i<19; i++) {
       	        	for(int j=0; j<19; j++) {
       	        		ConnectSix.check[i][j] =-1 ;
       	        		weight[i][j] = 0;
       	        	}
       	        }
       		 	draw.repaint();
            }
        });
    	containerPanel.add(resetbtn);
    	
    	JButton backbtn = new JButton("한수 무르기");
    	backbtn.setBounds(900,240,100,50);
    	backbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ConnectSix.check[prev.x][prev.y] =-1 ;
       		 	if(ban)
       		 		banNumber++;
       		 	if(start)
       		 		count--;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(backbtn);
    	
    	lbX = new JLabel("X");
    	lbX.setBounds(920, 310, 40, 40);
    	containerPanel.add(lbX);
    	inputX = new JTextField();
    	inputX.setBounds(905, 350, 40, 40);
    	containerPanel.add(inputX);
    	lbY = new JLabel("Y");
    	lbY.setBounds(965, 310, 40, 40);
    	containerPanel.add(lbY);
    	inputY = new JTextField();
    	inputY.setBounds(950, 350, 40, 40);
    	containerPanel.add(inputY);
    	
    	JButton put = new JButton("적용");
    	put.setBounds(900, 400, 100, 40);
    	put.setBackground(Color.WHITE);
    	put.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				press=true;
				pointX = Integer.parseInt(inputX.getText());
				pointY = Integer.parseInt(inputY.getText());
				dot = checkDot[pointX][pointY];
				System.out.println("pointX : " + pointX + " pointY : " + pointY);
				System.out.println(dot.x  + " " + dot.y);
				draw.validate();
				draw.repaint();
//				inputX.setText("");
//				inputY.setText("");
			}
    	});
    	containerPanel.add(put);
    	
    	containerPanel.add(connectSixPanel);
		add(containerPanel);
    }
    
    public static void main(String[] args) {
    	new ConnectSix();
    }
}
