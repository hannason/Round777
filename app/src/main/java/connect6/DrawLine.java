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

	private Point []nextStone=new Point[2];

	int[][] clone = new int[19][19];
	Stack<Point> compareAdjPointS = new Stack<Point>();//육목이 될 연속되지 않은 빈 공간 체크 
	Stack<Point> compareSuccPointS = new Stack<Point>(); //육목이 될 연속된 빈 공간 체크 
	static Point[] select = new Point[2]; //시뮬레이션 결과 선택된 돌의 위치 포인트
	
	@Override
    public void paintComponent(Graphics var){
		//판의 기본 설정 .
        super.paintComponent(var);
        Graphics2D var2d = (Graphics2D) var;
        this.setBackground(new Color(220, 179, 92));
        this.setBounds(0,0,800,800);
        var2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
        var2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));
        
//        if(ConnectSix.MyColor == 1)
//		{
//	 		myPoint = ConnectSix.blackS;
//	 		
//		}
//		if(ConnectSix.MyColor == 2) {
//			myPoint = ConnectSix.whiteS;
//		}
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
        //판 그리기 , 교차점 저장 
        drawPanel(var2d);
               
        //착수 금지돌 설정 
        if(ConnectSix.ban) {
        	setBanPoint();
        }
        
        //게임 시작 
        if(ConnectSix.start) {//beforeEdit();
        	ConnectSix.myPoint.add(new Point(8,9));
        	ConnectSix.check[8][9]=ConnectSix.MyColor;
          ConnectSix.myPoint.add(new Point(10,9));
          ConnectSix.check[10][9]=ConnectSix.MyColor;
//          ConnectSix.myPoint.add(new Point(11,9));
//          ConnectSix.check[11][9]=ConnectSix.MyColor;
         ConnectSix.myPoint.add(new Point(12,9));
         ConnectSix.check[12][9]=ConnectSix.MyColor;
//          ConnectSix.check[11][9]=ConnectSix.MyColor;
//          ConnectSix.myPoint.add(new Point(13,9));
//          ConnectSix.check[13][9]=ConnectSix.MyColor;
        	System.out.println("1 : check[0][0]"+ConnectSix.check[0][0]);
        	//이미 선택된 돌이 아니면 두 돌을  적절한 스택에 넣어주는 작업 
        	try {
	        	if(ConnectSix.check[ConnectSix.pointX1][ConnectSix.pointY1]<0) {
	        		ConnectSix.check[ConnectSix.pointX1][ConnectSix.pointY1]=ConnectSix.RivalColor;//판 그리기를 위한 할당 
	        		ConnectSix.rivalPoint.add(new Point(ConnectSix.pointX1,ConnectSix.pointY1));//상대 돌집합을 위한 할당 
	        		ConnectSix.count++;
	        		System.out.println("2 : check[0][0]"+ConnectSix.check[0][0]);
	        	}
	        	if(ConnectSix.check[ConnectSix.pointX2][ConnectSix.pointY2]<0) {
	        		ConnectSix.check[ConnectSix.pointX2][ConnectSix.pointY2]=ConnectSix.RivalColor;//판 그리기를 위한 할당 
	        		ConnectSix.rivalPoint.add(new Point(ConnectSix.pointX2,ConnectSix.pointY2));//상대 돌집합을 위한 할당 
	        		ConnectSix.count++;
	        		System.out.println("3 : check[0][0]"+ConnectSix.check[0][0]);
	        	}
        	}catch(RuntimeException e) {
        		
        	}
        	
        	//내가 이길 상황인가?
        	checkOverWin(); //good!
        	//내가 질 상황인가?
        	checkOverLose(); //
        	//유리한 조건의 돌은 무엇인가? 
        	
        }
       
        //그리기 
        drawAllStone(var2d);
       
        //게임 end 승리 멘트 부분
        gameEnd();
       
        ConnectSix.press=false;
    }
	
private void checkOverLose() {
		// TODO Auto-generated method stub
		
	}

/*
	private void beforeEdit() {
		// TODO Auto-generated method stub
		if(ConnectSix.count==1) {
    		ConnectSix.check[9][9]=1;
    		ConnectSix.count++;
    	}else {
        	if(( ConnectSix.count%4==0 || ConnectSix.count%4==1 )&& ConnectSix.press) {
        		
            	for(int i=0; i<19;i++) {
	        		for(int j=0; j<19; j++) {
	        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면
	        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.crossPoint[i][j].x -ConnectSix.inputPoint.x)<15 && Math.abs(ConnectSix.crossPoint[i][j].y -ConnectSix.inputPoint.y)<15 ) {
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
	        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.crossPoint[i][j].x -ConnectSix.inputPoint.x)<15 && Math.abs(ConnectSix.crossPoint[i][j].y -ConnectSix.inputPoint.y)<15 ) {
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
*/

	private void gameEnd() {
		// TODO Auto-generated method stub
		JFrame end;
		JLabel black;
		JLabel white;
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
	}


	private void drawAllStone(Graphics2D var2d) {
		// TODO Auto-generated method stub
		for(int i=0; i<19;i++) {
    		for(int j=0; j<19; j++) {
    			//선택된 돌   
    			if(ConnectSix.check[i][j]>=0) {
    				switch(ConnectSix.check[i][j]) {
    				case 0:
    					var2d.setColor(Color.RED);
    					break;
    				case 1:
    					var2d.setColor(Color.BLACK);
    					break;
    				case 2:
    					var2d.setColor(Color.WHITE);
    					break;
    				}
            		var2d.fillOval(i*740/18+30-15, j*740/18+30-15, 30, 30);
            		
    			}
    			System.out.print(ConnectSix.check[i][j]+"  ");
    		}
    		System.out.println("");
    	}
		System.out.println("====================");
	}


	private void setBanPoint() {
		if(ConnectSix.banNumber>0 && ConnectSix.press) {
    		
			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면
			if(ConnectSix.check[ConnectSix.pointX1][ConnectSix.pointY1]<0) {
				ConnectSix.check[ConnectSix.pointX1][ConnectSix.pointY1]=0;
				ConnectSix.banNumber--;
				
				ConnectSix.draw.repaint();
			}

 
    	}else if(ConnectSix.banNumber<=0 && ConnectSix.press) {
    		if(!ConnectSix.start) {
				JOptionPane.showMessageDialog(null, "이미 설정한 모든 착수금지돌을 두었습니다.", "착수금지돌 개수 에러 ", JOptionPane.DEFAULT_OPTION);
				ConnectSix.draw.repaint();
				//ConnectSix.ban=false;
				//ConnectSix.draw.repaint();
    		}
    	}
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
	
	
	private void drawPanel(Graphics2D var2d) {
		// TODO Auto-generated method stub
		//육목 판 그리기 
		for(int i=0; i<19; i++) {
	    	
	    	var2d.drawLine(i*740/18+30, 30, i*740/18+30, 740+30);
	    	var2d.drawLine(30, i*740/18+30, 740+30, i*740/18+30);
	    	
	    }
		//육목 교차점 저장하기 
		for(int i=0; i<19; i++) {//행 
	    	for(int j=0; j<19; j++) { //열
	    		Point point = new Point(i*740/18+30,j*740/18+30);
	    		//var2d.drawOval(i*740/18+30-10, j*740/18+30-10, 20, 20);
	    		ConnectSix.crossPoint[i][j] = point ;
	    	}
	    }
	}
	
	
	public int winloseHorizantal(int row, int column, int count, int color) {
		int right=winloseRight(row,column, 0,color,1);
		int left=winloseRight(row, column,0,color,-1);
		
		return right+left+1;
	}
	public int winloseVertical(int row, int column, int count, int color) {
		
		int right=winloseDown(row,column, 0,color,1);
		int left=winloseDown(row, column,0,color,-1);
		return right+left+1;
	}
	public int winlosePlusSlop(int row, int column, int count, int color) {
		
		int right=winloseAntiCross(row,column, 0,color,1);
		int left=winloseAntiCross(row, column,0,color,-1);
		return right+left+1;
	}
	public int winloseMinusSlop(int row, int column, int count, int color) {
		
		int right=winloseCross(row,column, 0,color,1);
		int left=winloseCross(row, column,0,color,-1);
		
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
	
	//x,y 점에 대해 이웃점들의 리스트를 반환해주는 함수 
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
	
	public void checkOverWin()//큐 a: 이웃점의 비교/ 큐 b: 시뮬레이션을 위한 큐/  큐 c: 연속된 점의 비교 
	{
		System.out.println("4 : check[0][0]"+ConnectSix.check[0][0]);
		//1 블랙리스트 각각 점 ->포인트 함수->큐a에 넣어준다.
		ArrayList<Point> NeighborAry = new ArrayList<>();
		for(int i=0; i<ConnectSix.myPoint.size(); i++)
		{
			//나의 점의 이웃들을 모두 저장한다. 
			NeighborAry = getPointAry(ConnectSix.myPoint.elementAt(i).x, ConnectSix.myPoint.elementAt(i).y);
			
			//NeighborAry의 모든 점들을 인접점 비교 스택에 넣어준다. 
			for(int j=0; j<NeighborAry.size(); j++)
			{
				compareAdjPointS.add(NeighborAry.get(j));
			}
		}
	
		//for 문 (빈칸 2개를 위한 for문  , 00xx00) 
				//2 큐a의 첫번째 포인트와  이 포인트를 포인트 함수에 넣어서 큐c를 만든다.
				//3 큐a의 포인트와 큐c의 첫번째 포인트와 묶음을 만든다 .
				
				//4 임의의 큐b를 만든다.(시뮬레이션을 위한…blacklist에서 3번의 점을 넣어서 만든 큐b)
				
				//4 임의의 큐b를 통헤 승리 조건을 판별한다.
				//5 만약 승리 조건이면 for문을 멈추고 묶음을 반환한다. 또는 어떠한 변수에 저장한다. 
		//인접한 점 집합
		for(int i=0; i<compareAdjPointS.size(); i++)
		{
			NeighborAry = new ArrayList<>();
			
			//인접한 점 각각의 이웃점 집합 만들기 
			NeighborAry = getPointAry(compareAdjPointS.elementAt(i).x, compareAdjPointS.elementAt(i).y);

			for(int j=0; j<NeighborAry.size(); j++)
			{
				compareSuccPointS.add(NeighborAry.get(j));
				
			}
			
			//인접한 점과 그 점의 이웃점을 묶음으로 기존 판에 있는 점들과 비교하여 육목이 될 수 있는 지 시뮬레이션 
			for(int j=0; j<compareSuccPointS.size(); j++)
			{
				ConnectSix.myPoint.add(compareAdjPointS.elementAt(i));
				ConnectSix.myPoint.add(compareSuccPointS.elementAt(j));
				ConnectSix.check[compareAdjPointS.elementAt(i).x][compareAdjPointS.elementAt(i).y] = ConnectSix.MyColor;
				ConnectSix.check[compareSuccPointS.elementAt(j).x][compareSuccPointS.elementAt(j).y] = ConnectSix.MyColor;
				for(int l=0; l<ConnectSix.myPoint.size(); l++)
				{
					if(winloseVertical(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y, 1,1)>=6 
							|| winloseHorizantal(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y, 1,1)>=6
							|| winlosePlusSlop(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y,1,1)>=6 
							|| winloseMinusSlop(ConnectSix.myPoint.elementAt(l).x, ConnectSix.myPoint.elementAt(l).y,1,1)>=6
						)
					{
	    				
						select[0] = new Point(compareAdjPointS.elementAt(i).x, compareAdjPointS.elementAt(i).y);
	    				select[1] = new Point(compareSuccPointS.elementAt(j).x, compareSuccPointS.elementAt(j).y);
	    				System.out.println("select1 : "+select[0]+" "+select[1]);
	    				return;
	    			}
					
				}
				ConnectSix.myPoint.pop();
				ConnectSix.myPoint.pop();
				ConnectSix.check[compareAdjPointS.elementAt(i).x][compareAdjPointS.elementAt(i).y] = -1;
				ConnectSix.check[compareSuccPointS.elementAt(j).x][compareSuccPointS.elementAt(j).y] = -1;
			}
		}
		System.out.println("5 : check[0][0]"+ConnectSix.check[0][0]);
		
		
		
		//for 문 (빈칸 1개를 위한 for문 , 0x0000)
			//2 큐a의 첫번째 포인트와  큐a의 다른 포인트와 묶음을 만든다.
			
			//3 임의의 큐b를 만든다.(시뮬레이션을 위한...blacklist에서 2번의 점을 넣어서 만든 큐b)
			
			//4 임의의 큐b를 통헤 승리 조건을 판별한다.
	
			//5 만약 승리 조건이면 for문을 멈추고 묶음을 반환한다. 또는 어떠한 변수에 저장한다. 
		for(int i=0; i<compareAdjPointS.size(); i++)
		{
			for(int j=i+1; j<compareAdjPointS.size(); j++)
			{
				ConnectSix.myPoint.add(compareAdjPointS.elementAt(i));
				ConnectSix.myPoint.add(compareAdjPointS.elementAt(j));
				ConnectSix.check[compareAdjPointS.elementAt(i).x][compareAdjPointS.elementAt(i).y] = ConnectSix.MyColor;
				ConnectSix.check[compareAdjPointS.elementAt(j).x][compareAdjPointS.elementAt(j).y] = ConnectSix.MyColor;
				for(int l=0; l<ConnectSix.myPoint.size(); l++)
				{
					if(winloseVertical(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y, 1,1)>=6 
							|| winloseHorizantal(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y, 1,1)>=6 
							|| winlosePlusSlop(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y,1,1)>=6 
							||winloseMinusSlop(ConnectSix.myPoint.elementAt(l).x,ConnectSix.myPoint.elementAt(l).y,1,1)>=6
					){
	    				select[0] = new Point(compareAdjPointS.elementAt(i).x, compareAdjPointS.elementAt(i).y);
	    				select[1] = new Point(compareAdjPointS.elementAt(j).x, compareAdjPointS.elementAt(j).y);
	    				System.out.println("select2 : "+select[0]+" "+select[1]);
	    				return;
	    			}
				}
				ConnectSix.myPoint.pop();
				ConnectSix.myPoint.pop();
				ConnectSix.check[compareAdjPointS.elementAt(i).x][compareAdjPointS.elementAt(i).y] = -1;
				ConnectSix.check[compareAdjPointS.elementAt(j).x][compareAdjPointS.elementAt(j).y] = -1;
			}
		}
		
		System.out.println("6 : check[0][0]"+ConnectSix.check[0][0]);
	}

}