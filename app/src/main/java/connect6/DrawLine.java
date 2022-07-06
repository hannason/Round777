package connect6;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Stack;
import java.nio.file.*;
import java.util.stream.*;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;


public class DrawLine extends JPanel {
	//carnivorousPanel.setBounds(50,50,800,800);
	//private GetMousePreRelXY prerelxy;
	public int win =0;
	private int startX,startY; //X,Y

	private int checkOK;
	private Point []nextStone=new Point[2];

	JFrame end;
	JLabel black;
	JLabel white;

	int[][] clone = new int[19][19];
	Stack<Point> compareAdjPointS = new Stack<Point>();
	
	@Override
    public void paintComponent(Graphics var){

        super.paintComponent(var);
        Graphics2D var2d = (Graphics2D) var;
        this.setBackground(new Color(220, 179, 92));
        this.setBounds(0,0,800,800);
        var2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        var2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        ConnectSix.connectSixPanel.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
//    			press=true;
//    			startX=e.getX();
//    			startY=e.getY();
//    			ConnectSix.draw.validate();
//    			ConnectSix.draw.repaint();
    		}
    	});
        //판 
        for(int i=0; i<19; i++) {
        	
        	var2d.drawLine(i*740/18+30, 30, i*740/18+30, 740+30);
        	var2d.drawLine(30, i*740/18+30, 740+30, i*740/18+30);
        	
        }
        //교차점 저장
        for(int i=0; i<19; i++) {//행 
        	for(int j=0; j<19; j++) { //열
        		Point point = new Point(i*740/18+30,j*740/18+30);
        		//var2d.drawOval(i*740/18+30-10, j*740/18+30-10, 20, 20);
        		ConnectSix.checkDot[i][j] = point ;
        	}
        }
        /////////////////////////////////////////////
        //ban
        if(ConnectSix.ban) {

        	if(ConnectSix.banNumber>0 && ConnectSix.press) {
        		
            	for(int i=0; i<19;i++) {
	        		for(int j=0; j<19; j++) {
	        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면
	        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -ConnectSix.dot.x)<10 && Math.abs(ConnectSix.checkDot[i][j].y -ConnectSix.dot.y)<10 ) {
	    					if(i==9 && j==9) {
	    						JOptionPane.showMessageDialog(null, "이 위치에는 돌을 놓을 수 없습니다.", "착수금지돌 개수 에러", JOptionPane.DEFAULT_OPTION);

	    						ConnectSix.draw.repaint();
	    					}else {
	    						ConnectSix.check[i][j]=0;
	        					ConnectSix.banNumber--;
	        					ConnectSix.prev=new Point(i,j);
	        					
	    					}
	        			}
	        		}
	        	}
            	 
     
        	}else if(ConnectSix.banNumber<=0 && ConnectSix.press) {
        		if(!ConnectSix.start) {
        			//MyDialog dialog = new MyDialog(this);
					JOptionPane.showMessageDialog(null, "이미 설정한 모든 착수금지돌을 두었습니다.", "착수금지돌 개수 에러 ", JOptionPane.DEFAULT_OPTION);
					ConnectSix.draw.repaint();
					//ConnectSix.ban=false;
					//ConnectSix.draw.repaint();
        		}
        	}
        	
        }
        if(ConnectSix.start) {
        //착수금지돌 개수 에러 
        	
        	if(ConnectSix.count==1) {
        		ConnectSix.check[9][9]=1;
        		ConnectSix.count++;
        	}else {
	        	if(( ConnectSix.count%4==0 || ConnectSix.count%4==1 )&& ConnectSix.press) {
	        		
	            	for(int i=0; i<19;i++) {
		        		for(int j=0; j<19; j++) {
		        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면
		        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -ConnectSix.dot.x)<15 && Math.abs(ConnectSix.checkDot[i][j].y -ConnectSix.dot.y)<15 ) {
		        				ConnectSix.check[i][j]=1;
		        				ConnectSix.count++;
		        				ConnectSix.prev=new Point(i,j);
		        				ConnectSix.blackList.add(new Point(i,j));
		        				//우리가 이길 조건이면by 시뮬레이션 
		        				checkOverWin();
		        				//상대 방이 이길 조건 
		        				
		        				//내가 유리한 조건이면 
		        				
		        				//4이상 판별한 위치가 돌이 없으면 nextstone으로 간주.
		        				//있으면 가중치 체크해서 stone결정 
		        				//
		        				//흑 승리 판별  
		                		if(ConnectSix.check[i][j]==1) {
		                			if(winloseVertical(i,j, 1,1)>=6 || winloseHorizantal(i,j, 1,1)>=6 || winlosePlusSlop(i,j,1,1)>=6 ||winloseMinusSlop(i,j,1,1)>=6) {
		                				win=1;
		                				System.out.println("Black win");
		                			}
		                		}
		        			}
		        		}
		        	}
	        	}
	        	
	        	//백
	        	if(( ConnectSix.count%4==2 || ConnectSix.count%4==3 ) && ConnectSix.press) {
	        		
	            	for(int i=0; i<19;i++) {
		        		for(int j=0; j<19; j++) {
		        			
		        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면
		        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -ConnectSix.dot.x)<15 && Math.abs(ConnectSix.checkDot[i][j].y -ConnectSix.dot.y)<15 ) {
		        				ConnectSix.check[i][j]=2;
		        				ConnectSix.count++;
		        				ConnectSix.whiteList.add(new Point(i,j));
		        				ConnectSix.prev=new Point(i,j);
		        				//백 승리 판별
		                		if(ConnectSix.check[i][j]==2) {
		                			if(winloseVertical(i,j, 1,2)>=6 || winloseHorizantal(i,j, 1,2)>=6 || winlosePlusSlop(i,j,1,2)>=6 ||winloseMinusSlop(i,j,1,2)>=6) {
		                				win=2;
		                				System.out.println("White win");
			                	
		                			}
		                		}
		        			}
		        		}
		        	}
	        	}
        	}
        }
        
        //그리기 
        for(int i=0; i<19;i++) {
    		for(int j=0; j<19; j++) {
    			//선택된 돌   
    			if(ConnectSix.check[i][j]>=0) {
    				switch(ConnectSix.check[i][j]) {
    				case 0:
    					var.setColor(Color.RED);
    					break;
    				case 1:
    					var.setColor(Color.BLACK);
    					break;
    				case 2:
    					var.setColor(Color.WHITE);
    					break;
    				}
            		var2d.fillOval(i*740/18+30-15, j*740/18+30-15, 30, 30);
    			}

    		}
    	}
       
        //승리 멘트 부분
        if(win>0) {
        	 if(win == 1)
             {
                System.out.println("흑돌 승");
                end = new JFrame();
                end.setLayout(null);
                black = new JLabel("흑돌 승");
                black.setFont(new Font("고딕체", Font.BOLD, 20));
                black.setBounds(100, 50, 70, 30);
                end.add(black);
                end.setBounds(600, 200, 300, 200);
                end.setVisible(true);
                end.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                repaint();
             }
             else if(win == 2)
             {
                System.out.println("백돌 승");
                end = new JFrame();
                end.setLayout(null);
                white = new JLabel("백돌 승");
                white.setFont(new Font("고딕체", Font.BOLD, 20));
                white.setBounds(100, 50, 70, 30);
                end.add(white);
                end.setBounds(600, 200, 300, 200);
                end.setVisible(true);
                end.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//                repaint();
             }
        }
        ConnectSix.press=false;
    }
	

//	private void checkOverFour() {
//		// TODO Auto-generated method stub
//		for(int i=0;i<19;i++) {
//			for(int j=0; j<19; j++) {
//				//4이상이면 
//				if(winloseVertical(i,j, 1,1)>=4) {
//					
//				}
//				if(winloseHorizantal(i,j, 1,1)>=4) {
//					
//				}
//				if(winlosePlusSlop(i,j,1,1)>=4) {
//					
//				}
//				if(winloseMinusSlop(i,j,1,1)>=4) {
//					
//				}
//			}
//		}	
//	}

	public int winloseHorizantal(int row, int column, int count, int color) {
		int right=winloseRight(row,column, 0,color,1);
		int left=winloseRight(row, column,0,color,-1);
		
		if(right+left+1>=4) {
			Point temp = new Point(row+right+1, column);
			nextStone[0]=temp;
			temp = new Point(row-left-1, column);
			nextStone[1]=temp;
			System.out.println("first "+nextStone[0]+"  second  "+nextStone[1]);
		}
		return right+left+1;
	}
	public int winloseVertical(int row, int column, int count, int color) {
		
		int right=winloseDown(row,column, 0,color,1);
		int left=winloseDown(row, column,0,color,-1);
		if(right+left+1>=4) {
			Point temp = new Point(row, column+right+1);
			nextStone[0]=temp;
			temp = new Point(row, column-left-1);
			nextStone[1]=temp;
			System.out.println("first "+nextStone[0]+"  second  "+nextStone[1]);
		}
		return right+left+1;
	}
	public int winlosePlusSlop(int row, int column, int count, int color) {
		
		int right=winloseAntiCross(row,column, 0,color,1);
		int left=winloseAntiCross(row, column,0,color,-1);
		System.out.println("right "+right+" left "+left);
		if(right+left+1>=4) {
			
			Point temp = new Point(row+right+1, column-right-1);
			nextStone[0]=temp;
			temp = new Point(row+left+1, column-left-1);
			nextStone[1]=temp;
			System.out.println("first "+nextStone[0]+"  second  "+nextStone[1]);
		}
		return right+left+1;
	}
	public int winloseMinusSlop(int row, int column, int count, int color) {
		
		int right=winloseCross(row,column, 0,color,1);
		int left=winloseCross(row, column,0,color,-1);
		
		if(right+left+1>=4) {
			Point temp = new Point(row+right+1, column+right+1);
			nextStone[0]=temp;
			temp = new Point(row-left-1, column-left-1);
			nextStone[1]=temp;
			System.out.println("first "+nextStone[0]+"  second  "+nextStone[1]);
		}
		return right+left+1;
	}
	
	public int winloseRight(int row, int column, int count, int color,int rightleft) {
		try {

	    	if(ConnectSix.check[row+rightleft][column]==color) {
	    		count++;
	    		
	    		return winloseRight(row+rightleft, column,count,  color, rightleft);
	    	}
	    	else {
	    		return count;
	    	}
		}catch(RuntimeException e) {
			return 0;
		}
    	
    }
	public int winloseCross(int row, int column, int count, int color,int rightleft) {
		try {

	    	if(ConnectSix.check[row+rightleft][column+rightleft]==color) {
	    		count++;
	    		return winloseCross(row+rightleft, column+rightleft,count,  color, rightleft);
	    	}
	    	else {
	    		return count;
	    	}
		}catch(RuntimeException e) {
			return 0;
		}
    }
	public int winloseDown(int row, int column, int count, int color,int rightleft) {
		
		try {
			
	    	if(ConnectSix.check[row][column+rightleft]==color) {
	    		count++;
	    		return winloseDown(row, column+rightleft,count,  color, rightleft);
	    	}
	    	else {
	    		return count;
	    	}
		}catch(RuntimeException e) {
			return 0;
		}
    }
	public int winloseAntiCross(int row, int column, int count, int color, int rightleft) {
		try {
			

	    	if(ConnectSix.check[row+rightleft][column-rightleft]==color) {
	    		count++;
	    		return winloseAntiCross(row+rightleft, column-rightleft,count,  color, rightleft);
	    	}
	    	else {
	    		return count;
	    	}
		}catch(RuntimeException e) {
			return 0;
		}
	}
	
	public ArrayList<Point> getPointAry(int x, int y)
    {
    	ArrayList<Point> nPoint = new ArrayList<Point>();
    	if(x-1 >= 0 && y-1 >=0 && ConnectSix.check[x-1][y-1] == -1)
    	{
    		nPoint.add(new Point(x-1, y-1));
    	}
    	if(y-1 >=0 && ConnectSix.check[x][y-1] == -1)
    	{
    		nPoint.add(new Point(x, y-1));
    	}
    	if(x+1 <19 && y-1 >=0 && ConnectSix.check[x+1][y-1] == -1)
    	{
    		nPoint.add(new Point(x+1, y-1));
    	}
    	if(x-1 >= 0 && ConnectSix.check[x-1][y] == -1)
    	{
    		nPoint.add(new Point(x-1, y));
    	}
    	if(x+1 < 19 && ConnectSix.check[x+1][y] == -1)
    	{
    		nPoint.add(new Point(x+1, y));
    	}
    	if(x-1 >= 0 && y+1 < 19 && ConnectSix.check[x-1][y+1] == -1)
    	{
    		nPoint.add(new Point(x-1, y+1));
    	}
    	if( y+1 >= 0 && ConnectSix.check[x][y+1] == -1)
    	{
    		nPoint.add(new Point(x, y+1));
    	}
    	if(x+1 < 19 && y+1 < 19 && ConnectSix.check[x+1][y+1] == -1)
    	{
    		nPoint.add(new Point(x+1, y+1));
    	}
    	return nPoint;
    }
	
	public void checkOverWin()
	{
		//큐 a: 이웃점의 비교/ 큐 b: 시뮬레이션을 위한 큐/  큐 c: 연속된 점의 비교 
		//1 블랙리스트 각각 점 ->포인트 함수->큐a에 넣어준다.
		ArrayList<Point> ary = new ArrayList<>();
		for(int i=0; i<ConnectSix.blackS.size(); i++)
		{
			ary = getPointAry(ConnectSix.blackS.elementAt(i).x, ConnectSix.blackS.elementAt(i).y);
			for(int j=0; j<ary.size(); j++)
			{
				compareAdjPointS.add(ary.get(j));
			}
		}
		//for 문 (빈칸 1개를 위한 for문 , 0x0000)
			//2 큐a의 첫번째 포인트와  큐a의 다른 포인트와 묶음을 만든다.
			
			//3 임의의 큐b를 만든다.(시뮬레이션을 위한...blacklist에서 2번의 점을 넣어서 만든 큐b)
			
			//4 임의의 큐b를 통헤 승리 조건을 판별한다.

			//5 만약 승리 조건이면 for문을 멈추고 묶음을 반환한다. 또는 어떠한 변수에 저장한다. 
		for(Point p : compareAdjPointS)
		{
			for(Point pnt : compareAdjPointS)
			{
				
			}
		}
		
		//for 문 (빈칸 2개를 위한 for문  , 00xx00) 
				//2 큐a의 첫번째 포인트와  이 포인트를 포인트 함수에 넣어서 큐c를 만든다.
				//3 큐a의 포인트와 큐c의 첫번째 포인트와 묶음을 만든다 .
				
				//4 임의의 큐b를 만든다.(시뮬레이션을 위한…blacklist에서 3번의 점을 넣어서 만든 큐b)
				
				//4 임의의 큐b를 통헤 승리 조건을 판별한다.

				//5 만약 승리 조건이면 for문을 멈추고 묶음을 반환한다. 또는 어떠한 변수에 저장한다. 

	}
}