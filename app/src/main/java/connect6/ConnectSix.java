package connect6;
/*
 1) 일반 바둑판 사용 (19x19)

2) 팀별 프로그램 설치된 컴퓨터 1대와 선수 1명 출전

3) 게임의 다양성을 높이기 위해 심판이 0~5개의 착수금지점을 임의로 배치

4) 흑백이 교대로 한번에 2개씩 바둑돌을 놓는데 흑은 첫수에 돌 하나만 정중앙에 착수

5) 상대팀의 2수를 자신의 팀 프로그램에 입력

6) 팀 프로그램이 제시한 2수를 그대로 바둑판에 착수

    (착수 금지점에 착수한 돌은 무시) (30초 제한)

7) 6개 이상의 돌이 같은색으로 일렬로 만드는 팀이 승리

8) 3x3과 같은 특별한 금지 없음

9) 흑백은 경기직전 결정 
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
	static public int banNumber=0;
	static public boolean start=false;
	static public Point[][] checkDot=  new Point [19][19];
	static public int[][] check=  new int [19][19]; //-1 : 선택되지 않은 곳/ 0 : band된 곳/ 1:흑돌/2:백돌 
	static public int count=0;
	static public Point prev;
	//static public JLabel alert;
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
    	//GetMousePreRelXY prerelxy = new GetMousePreRelXY();
    	//connectSixPanel.addMouseListener(prerelxy);
    	//교차점 초기화 
        for(int i=0; i<19; i++) {//행 
        	for(int j=0; j<19; j++) { //열 
        		ConnectSix.check[i][j] =-1 ;
        	}
        }
    	draw = new DrawLine();
    	draw.setBounds(0,0,800,800);
    	connectSixPanel.add(draw);
    	
    	
    	JTextField bandN = new JTextField();
    	bandN.setBounds(850,50,50,50);
    	containerPanel.add(bandN);
    	
    	JButton banbtn = new JButton("착수 금지돌 설정 ");
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
       		 	ban=false;//게임 시작 후 이제 착수 금지 돌 설정 안됨 
       		 	count=1;
       		 	start=true;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(startbtn);
    	
    	JButton resetbtn = new JButton("새로 고침");
    	resetbtn.setBounds(900,180,100,50);
    	resetbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ban=false; 
       		 	count=1;
       		 	start=false;
       		 	//교차점 초기화 
       	        for(int i=0; i<19; i++) {//행 
       	        	for(int j=0; j<19; j++) { //열 
       	        		ConnectSix.check[i][j] =-1 ;
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
    	
    	
    	containerPanel.add(connectSixPanel);
		add(containerPanel);
    }
    
    public static void main(String[] args) {
    	new ConnectSix();
    }
}
