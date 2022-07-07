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

import java.util.ArrayList;
import java.util.Stack;

import javax.swing.*;

public class ConnectSix extends JFrame {
	//게임 시작 전 필요한 기본 변수들 
	static public JPanel connectSixPanel;
	static public DrawLine draw;
	static public boolean ban=false;
	static public int banNumber=0;
	static boolean press=false;
	static public boolean start=false;
	
	//게임 시작 후 필요한 변수들 
	static public int MyColor=-1;//내가 쓰는 돌의 색깔 
	static public int RivalColor=-1;
	static public Point[][] crossPoint=  new Point [19][19];//판의 교차점 저장 
	static public int[][] check=  new int [19][19];//-1 : 선택되지 않은 곳/ 0 : band된 곳/ 1:흑돌/2:백돌 
	
	
	static public ArrayList <Point> blackList=new ArrayList <Point>(); //흑돌의 집합 
	static public ArrayList <Point> whiteList=new ArrayList <Point>(); //백돌의 집합 
	static Stack<Point> myPoint = new Stack<Point>(); //나의 돌의 위치 집합 
	static Stack<Point> rivalPoint = new Stack<Point>();//상대 돌의 위치 집합 
	static Stack<Point> blackS = new Stack<>();
	static Stack<Point> whiteS = new Stack<>();
	
	static public Point inputPoint1; //입력 받은 점 
	static public Point inputPoint2; //입력 받은 점 
	static public int count=0;
	static public int pointX1=-1;
	static public int pointY1=-1;//입력 받은 상대돌 
	static public int pointX2=-1;
	static public int pointY2=-1;//입력 받은 상대돌 
	static public boolean myTurn=false;
	
	
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
        	}
        }
    	draw = new DrawLine();
    	draw.setBounds(0,0,800,800);
    	connectSixPanel.add(draw);
    	
    	//착수 금지돌 관련 
    	JTextField bandN = new JTextField();
    	bandN.setBounds(850,50,40,40);
    	containerPanel.add(bandN);
    	
    	JButton banbtn = new JButton("착수 금지돌 설정");
    	banbtn.setBounds(900,50,130,40);
    	banbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	banNumber=Integer.parseInt(bandN.getText());
       		 	ban=true;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(banbtn);
    	
    	
    	//새로고침 버튼 
    	JButton resetbtn = new JButton("새로고침");
    	resetbtn.setBounds(900,180,100,50);
    	resetbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ban=false; 
       		 	count=1;
       		 	start=false;
       		 	MyColor=-1;
       		 	myPoint.removeAllElements();
       		 	rivalPoint.removeAllElements();
       		 	draw.repaint();
            }
        });
    	containerPanel.add(resetbtn);
    	
    	//한수 무르기 버튼 
    	JButton backbtn = new JButton("한수 무르기");/////////이거 잠깐 안됨 수정해라ㅏㅏ
    	backbtn.setBounds(900,240,100,50);
    	backbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	//ConnectSix.check[prev.x][prev.y] =-1 ;
       		 	if(ban)
       		 		banNumber++;
       		 	if(start)
       		 		count--;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(backbtn);
    	
    	//돌을 둘 위치 좌표 입력 받기 
    	JLabel lbX = new JLabel("X");
    	lbX.setBounds(920, 310, 40, 40);
    	containerPanel.add(lbX);
    	JTextField inputX1 = new JTextField();
    	inputX1.setBounds(905, 350, 40, 40);
    	containerPanel.add(inputX1);
    	JTextField inputX2 = new JTextField();
    	inputX2.setBounds(905, 400, 40, 40);
    	containerPanel.add(inputX2);
    	
    	JLabel lbY = new JLabel("Y");
    	lbY.setBounds(965, 310, 40, 40);
    	containerPanel.add(lbY);
    	JTextField inputY1 = new JTextField();
    	inputY1.setBounds(950, 350, 40, 40);
    	containerPanel.add(inputY1);
    	JTextField inputY2 = new JTextField();
    	inputY2.setBounds(950, 400, 40, 40);
    	containerPanel.add(inputY2);
    	
    	//나의 색깔 정하기 버튼 
    	JButton ImBlack = new JButton("흑");
    	JButton ImWhite = new JButton("백");
    	
    	ImBlack.setBounds(900, 500, 50, 40);
    	ImBlack.setBackground(Color.BLACK);
    	ImBlack.setForeground(Color.WHITE);
    	ImBlack.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImBlack.setBackground(Color.BLACK);
				ImBlack.setForeground(Color.WHITE);
				ImWhite.setBackground(Color.WHITE);
				ImWhite.setForeground(Color.WHITE);
				MyColor = 1;
				RivalColor=2;
				
			}
    	});
    	containerPanel.add(ImBlack);
    	
    	
    	ImWhite.setBounds(950, 500, 50, 40);
    	ImWhite.setBackground(Color.WHITE);
    	ImWhite.setForeground(Color.BLACK);
    	ImWhite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ImWhite.setBackground(Color.WHITE);
				ImWhite.setForeground(Color.BLACK);
				ImBlack.setBackground(Color.BLACK);
				ImBlack.setForeground(Color.BLACK);
				MyColor = 2;
				RivalColor=1;
			}
    	});
    	containerPanel.add(ImWhite);
    	
    	//게임 시작 버튼 
    	JButton startbtn = new JButton("게임 시작");
    	startbtn.setBounds(900,120,100,50);
    	startbtn.addActionListener(new ActionListener() {
       	 @Override
            public void actionPerformed(ActionEvent e) {
       		 	ban=false;
       		 	count=1;
       		 	start=true;
       		 	//내가 흑을 선택하면 자동으로 나의 점 집합에 중앙점을 넣어준다 .
       		 	if(MyColor == 1) {
       		 		myPoint.add(new Point(9,9));
       		 		check[9][9]=ConnectSix.MyColor;
       		 	}
       		 	else if (MyColor ==2) {
				//내가 백이면 자동으로 상대의 점 집합에 중앙점을 넣어준다.
       		 		rivalPoint.add(new Point(9,9));
       		 		check[9][9]=ConnectSix.RivalColor;
       		 		
       		 		myPoint.add(new Point(8,9));
       		 		myPoint.add(new Point(9,8));
       		 		check[8][9]=ConnectSix.MyColor;
       		 		check[9][8]=ConnectSix.MyColor;
       		 	}
    		 	count++;
       		 	draw.repaint();
            }
        });
    	containerPanel.add(startbtn);
    	
    	// 입력 받은 좌표에 돌을 둠.
    	JButton setPoint = new JButton("적용");
    	setPoint.setBounds(900, 450, 100, 40);
    	setPoint.setBackground(Color.WHITE);
    	setPoint.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				press=true;
				pointX1 = Integer.parseInt(inputX1.getText());
				pointY1 = Integer.parseInt(inputY1.getText());
				pointX2 = Integer.parseInt(inputX2.getText());
				pointY2 = Integer.parseInt(inputY2.getText());
//				inputPoint = crossPoint[pointX][pointY];
//				
//				if(ConnectSix.count%4==0 || ConnectSix.count%4==1) /////여기서 이 조건을 이용해서 마이턴이라는 변수를 설정한다. 
//					blackS.add(new Point(pointX, pointY));
//				if(ConnectSix.count%4==2 || ConnectSix.count%4==3 )
//					whiteS.add(new Point(pointX, pointY));

				draw.validate();
				draw.repaint();
			}
    	});
    	containerPanel.add(setPoint);
    	
    	containerPanel.add(connectSixPanel);
		add(containerPanel);
    }
    
    
    public static void main(String[] args) {
    	new ConnectSix();
    }
}
