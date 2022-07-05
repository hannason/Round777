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
	private boolean press=false;
	private int startX,startY; //X,Y
	private int checkOK;
	JFrame end;
	JLabel black;
	JLabel white;
	@Override
    public void paintComponent(Graphics var){

        super.paintComponent(var);
        Graphics2D var2d = (Graphics2D) var;
        this.setBackground(new Color(220, 179, 92));
        this.setBounds(0,0,800,800);
        var2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);//선 부드럽게 
        var2d.setStroke(new BasicStroke(2,BasicStroke.CAP_ROUND, BasicStroke.JOIN_BEVEL));//선 끝 둥글,꼭지 둥글
        ConnectSix.connectSixPanel.addMouseListener(new MouseAdapter() {
    		@Override
    		public void mousePressed(MouseEvent e) {
    			press=true;
    			startX=e.getX();
    			startY=e.getY();
//    			point=e.getLocationOnScreen();
//    			System.out.println(e.getPoint());
    			ConnectSix.draw.validate();
    			ConnectSix.draw.repaint();
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
        		ConnectSix.checkDot[i][j] =point ;
        	}
        }
        /////////////////////////////////////////////
        //ban
        if(ConnectSix.ban) {

        	if(ConnectSix.banNumber>0 && press) {
        		
            	for(int i=0; i<19;i++) {
	        		for(int j=0; j<19; j++) {
	        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면 
	        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -startX)<10 && Math.abs(ConnectSix.checkDot[i][j].y -startY)<10 ) {
	    					if(i==9 && j==9) {
	    						JOptionPane.showMessageDialog(null, "이 위치에는 돌을 놓을 수 없습니다. ", "착수금지돌 개수 에러 ", JOptionPane.DEFAULT_OPTION);

	    						//int result = JOptionPane.showConfirmDialog (null, "이 위치에는 돌을 놓을 수 없습니다. ", "착수금지돌 위치 에러 ", JOptionPane.DEFAULT_OPTION);			
	    						//if(result==JOptionPane.OK_OPTION) {
	    							ConnectSix.draw.repaint();
	    						//}
	    					}else {
	    						ConnectSix.check[i][j]=0;
	        					ConnectSix.banNumber--;
	        					ConnectSix.prev=new Point(i,j);
	        					
	    					}
	        			}
	        		}
	        	}
            	
     
        	}else if(ConnectSix.banNumber<=0 && press) {
        		if(!ConnectSix.start) {
        			//MyDialog dialog = new MyDialog(this);
					JOptionPane.showMessageDialog(null, "이미 설정한 모든 착수금지돌을 두었습니다. ", "착수금지돌 개수 에러 ", JOptionPane.DEFAULT_OPTION);
					ConnectSix.draw.repaint();
					//ConnectSix.ban=false;
					//ConnectSix.draw.repaint();
        		}
        	}
        	
        }
        if(ConnectSix.start) {
        	//맨 처음 흑
        	
        	if(ConnectSix.count==1) {
        		ConnectSix.check[9][9]=1;
        		ConnectSix.count++;
        	}else {
	        	if(( ConnectSix.count%4==0 || ConnectSix.count%4==1 )&& press) {
	        		
	            	for(int i=0; i<19;i++) {
		        		for(int j=0; j<19; j++) {
		        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면 
		        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -startX)<15 && Math.abs(ConnectSix.checkDot[i][j].y -startY)<15 ) {
		        				ConnectSix.check[i][j]=1;
		        				ConnectSix.count++;
		        				ConnectSix.prev=new Point(i,j);
		        				
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
	        	if(( ConnectSix.count%4==2 || ConnectSix.count%4==3 ) && press) {
	        		
	            	for(int i=0; i<19;i++) {
		        		for(int j=0; j<19; j++) {
		        			
		        			//이미 선택된 돌이 아니고 포인트의 위치가 바운더리 안에 있으면 
		        			if(ConnectSix.check[i][j]<0 && Math.abs(ConnectSix.checkDot[i][j].x -startX)<15 && Math.abs(ConnectSix.checkDot[i][j].y -startY)<15 ) {
		        				ConnectSix.check[i][j]=2;
		        				ConnectSix.count++;
		        				
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
//        	//흑 승 
//        	if(win==1) {
//				JOptionPane.showMessageDialog(null, "흑돌이 승리했습니다.  ","게임 종료 ", JOptionPane.DEFAULT_OPTION);
//				ConnectSix.ban=false; 
//				ConnectSix.count=1;
//				ConnectSix.start=false;
//				win=0;
//       		 	//교차점 초기화 
//       	        for(int i=0; i<19; i++) {//행 
//       	        	for(int j=0; j<19; j++) { //열 
//       	        		ConnectSix.check[i][j] =-1 ;
//       	        	}
//       	        }
//				ConnectSix.draw.repaint();
//        	}
//        	//백 승
//        	if(win==2) {
//				JOptionPane.showMessageDialog(null, "백돌이 승리했습니다.", "게임 종료 ", JOptionPane.DEFAULT_OPTION);
//				ConnectSix.ban=false; 
//				ConnectSix.count=1;
//				ConnectSix.start=false;
//				win=0;
//       		 	//교차점 초기화 
//       	        for(int i=0; i<19; i++) {//행 
//       	        	for(int j=0; j<19; j++) { //열 
//       	        		ConnectSix.check[i][j] =-1 ;
//       	        	}
//       	        } 
//				ConnectSix.draw.repaint();
//        	}
        	//ConnectSix.draw.repaint();
        }
        
        press=false;
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
	    		return winloseAntiCross(row, column+rightleft,count,  color, rightleft);
	    	}
	    	else {
	    		return count;
	    	}
		}catch(RuntimeException e) {
			return 0;
		}
	}
}