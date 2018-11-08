import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.Vector;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Panel_Board extends JPanel {

	Graphics2D g2;
	
	final static int width = 700;
	final static int height = 700;
	final static int gap = 668 / 19;		// gap between grids

	boolean isBlack = false;  	// indicates current turn
	boolean amIBlack = false;
	boolean isAIDone = true;
	
	Point current1 = null;
	Point current2 = null;
	
	short [][] map;		// location of black / white stones, 1: black, 0: white, -1: none
	
	int stone_count = 1;		// counts the first stone in the middle
	
	Vector<Integer> history;		// will be used to replay game
	
	// current mouse point
	Point mousePoint;
	
	// end / start point of winning sequence
	int start_x = -1;
	int start_y = -1;
	int end_x = -1;
	int end_y = -1;
	
	Timer t1;
	Timer t2;
	
	int result = 1;
		
	boolean isGameOver = false;
	
	Panel_GameOver panel_go = new Panel_GameOver();
	
	boolean drew_winning_stones = false;
	
	AI ai;
		
		
	public Panel_Board()
	{
		// set size / location for panel
		setSize(699, 699);
		setLocation(19, 79);
		
		setBackground(new Color(255, 185, 106));
		setLayout(null);
		
		
		// initiate instance variables
		 map = new short[19][19];
		 
		 for (int i = 0; i < 19; i ++) {
			 for (int j = 0; j < 19; j ++) {
				 map[i][j] = -1;
			 }
		 }
				
		
		// add black stone in the middle
		map[9][9] = 1;
		
		
		// initiate history vector
		history = new Vector<Integer>();
		history.add(909);		// 
		
		
		// add mouse listener
		MyMouseListener ml = new MyMouseListener();
		
		addMouseListener(ml);
		addMouseMotionListener(ml);
		
		
		setVisible(true);
		repaint();
	}
	
	
	public void paintComponent (Graphics g)
	{		
		// initiate Graphics2D
		super.paintComponent (g);
		
		g2 = (Graphics2D) g;
		g2.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		// draw board lines
		draw_board();
		
		
		// draw stone on current mouse point
		if (mousePoint != null)
		{
			if (isBlack) {
				g2.setColor(new Color(127, 92, 53));
				g2.fillOval((int)mousePoint.getX(), (int)mousePoint.getY(), 30, 30);
			}
			else {
				g2.setColor(new Color(255, 220, 190));
				g2.fillOval((int)mousePoint.getX(), (int)mousePoint.getY(), 30, 30);
				
				g2.setColor(new Color(127, 92, 53));
				//g2.drawLine((int)(mousePoint.getX()), (int)(mousePoint.getY() + 15), (int)(mousePoint.getX() + 30), (int)(mousePoint.getY() + 15));
				//g2.drawLine((int)(mousePoint.getX() + 15), (int)(mousePoint.getY()), (int)(mousePoint.getX() + 15), (int)(mousePoint.getY() + 30));
			}
		}
		
		
		// draw stones
		draw_stones();
		
		
		// check if current player's turn is over
		check_turn(isBlack);
		
		
		// check if game is over and draw line on winning stones
		if (start_x != -1 && !drew_winning_stones) {
			drew_winning_stones = true;
			isGameOver = true;
			draw_winning_stones();
		}
	}
	
	
	class MyMouseListener extends MouseAdapter implements MouseMotionListener
	{
		public void mousePressed (MouseEvent e)
		{
			if (!isGameOver && (isBlack == amIBlack)) {
				put_stone(e);
			}
		}
		
		public void mouseMoved (MouseEvent e)
		{
			if (!isGameOver && (isBlack == amIBlack)) {
				int x = e.getX();
				int y = e.getY();
				
				int radius = 30;
				
				// get x, y index of stone
				if (x % gap < gap / 2)
					x = x - (x % gap) - (radius / 2) + 1;
				else
					x = x - (x % gap) + gap - (radius / 2) + 1;
							
				if (y % gap < gap / 2)
					y = y - (y % gap) - (radius / 2) + 1;
				else
					y = y - (y % gap) + gap - (radius / 2) + 1;
				
				if (x <= 0) x += gap;
				if (x >= 19 * gap) x -= gap;
				if (y <= 0) y += gap;
				if (y >= 19 * gap) y -= gap;
				
				mousePoint = new Point(x, y);
				repaint();
			}
		}
	}
	
	
	// method to draw board lines
	private void draw_board()
	{
		g2.setColor(new Color(84, 42, 4));
		g2.setStroke(new BasicStroke(1));
		
		for (int i = gap; i < width; i += gap) {
			g2.drawLine(i, gap, i, height - gap);
		}
		
		for (int i = gap; i <= height; i += gap) {
			g2.drawLine(gap, i, width - gap, i);
		}
		
		int radius = 3;
		for (int i = gap * 4 + 1; i < width; i += gap * 6) {
			for (int j = gap * 4 + 1; j < height; j += gap * 6) {
				g2.fillOval(i - radius, j - radius, radius * 2, radius * 2);
			}
		}
	}
	
	
	private void draw_stones ()
	{
		int radius = 30;
		
		for (int i = 0; i < 19; i ++) {
			for (int j = 0; j < 19; j ++) {
				
				if (map[i][j] == -1)	 continue;
				
				else if (map[i][j] == 1) {
					g2.setColor(Color.BLACK);
				}
				else if (map[i][j] == 0)
					g2.setColor(Color.WHITE);
				
				int x_coordinate = (gap * i + 1) - (radius / 2) + gap;
				int y_coordinate = (gap * j + 1) - (radius / 2) + gap;
				
				g2.fillOval(x_coordinate, y_coordinate, radius, radius);
			}
		}		
	}
	
	
	private void put_stone (MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();
		
		// get x, y index of stone
		if (x % gap < gap / 2)
			x = x / gap - 1;
		else
			x = x / gap;
					
		if (y % gap < gap / 2)
			y = y / gap - 1;
		else
			y = y / gap;
		
		
		// checks if x, y index is out of boundary
		if (x < 0)	x = 0;
		if (x > 18)	x = 18;
		
		if (y < 0)	y = 0;
		if (y > 18)	y = 18;
		
		System.out.println(x + " " + y);
		
		// checks if x, y index is redundant
		if (map[x][y] != -1)	 return;
		
		// add x,y index to vector
		if (isBlack) {
			map[x][y] = 1;
			history.add(x * 100 + y);
		}
		else {
			map[x][y] = 0;
			history.add(x * 100 + y);
		}
		
		stone_count ++;
		
		if (stone_count % 2 == 0)
			current1 = new Point(x, y);
		else
			current2 = new Point(x, y);
		
		//Main.client.clientSender.sendMsg("[stone]," + Main.frame.start.selected + "," + x + "," + y);
		
		Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Put_Go.wav");


		repaint();
		
		
		// checks if game is over
		result = is_game_over(x, y);
		
		if (result != -1) {
			game_over (result);
		}
	}
	
	private int is_game_over(int x, int y)
	{
		int check;
		int stage = 0;
		
		// if current stone is black, check black stones. Otherwise, check whites.
		if (isBlack)
			check = 1;
		else
			check = 0;
		
	//	for (stage = 0; stage <= 1; stage ++) {
			
	//		if (stage == 0)
	//			check = 0;
	//		else
	//			check = 1;
			
			// check horizontally
			int left = x - 1;
			int right = x + 1;
			short count = 1;
			
			for (int i = 0; i < 5; i ++) {
				
				if (left >= 0) {
					if (map[left][y] == check) {
						count ++;
						left --;
					}
				}
					
				if (right < 16) {
					if (map[right][y] == check) {
						count ++;
						right ++;
					}
				}
			}
	
			if (count >= 6)	{
				
				start_x = left + 1;
				start_y = y;
				end_x = right - 1;
				end_y = y;			
				
				return check;
			}
			
			
			// check vertically
			int up = y - 1;
			int down = y + 1;
			count = 1;
			
			for (int i = 0; i < 5; i ++) {
				
				if (up > 0) {
					if (map[x][up] == check) {
						count ++;
						up --;
					}
				}
					
				if (down < 16) {
					if (map[x][down] == check) {
						count ++;
						down ++;
					}
				}
			}
	
			if (count >= 6)	{
				start_x = x;
				start_y = up + 1;
				end_x = x;
				end_y = down - 1;		
				
				return check;
			}
			
			
			// check diagonally
			int leftup_x = x - 1;
			int leftup_y = y - 1;
			int rightdown_x = x + 1;
			int rightdown_y = y + 1;
			count = 1;
			
			for (int i = 0; i < 5; i ++) {
				
				if (leftup_x > 0 && leftup_y > 0) {
					if (map[leftup_x][leftup_y] == check) {
						count ++;	
						
						leftup_x --;
						leftup_y --;
					}
				}
				if (rightdown_x < 16 && rightdown_y < 16) {
					if (map[rightdown_x][rightdown_y] == check) {
						count ++;
						
						rightdown_x ++;
						rightdown_y ++;
					}
				}
			}
	
			if (count >= 6)	{
				start_x = leftup_x + 1;
				start_y = leftup_y + 1;
				end_x = rightdown_x - 1;
				end_y = rightdown_y - 1;
				
				return check;
			}
			
			
			// check diagonally
			int rightup_x = x + 1;
			int rightup_y = y - 1;
			int leftdown_x = x - 1;
			int leftdown_y = y + 1;
			count = 1;
			
			for (int i = 0; i < 5; i ++) {
				
				if (rightup_x < 16 && rightup_y > 0) {
					if (map[rightup_x][rightup_y] == check) {
						count ++;
						
						rightup_x ++;
						rightup_y --;
					}
				}
					
				if (leftdown_x > 0 && leftdown_y < 16) {
					if (map[leftdown_x][leftdown_y] == check) {
						count ++;	
						
						leftdown_x --;
						leftdown_y ++;
					}
				}
			}
	
			if (count >= 6)	{
				start_x = rightup_x - 1;
				start_y = rightup_y + 1;
				end_x = leftdown_x + 1;
				end_y = leftdown_y - 1;
				
				return check;
			}
	//	}
		return -1;
	}
	
	
	private void check_turn (Boolean turn)
	{
		// check if turn should be changed
		if (((stone_count + 1) / 2) % 2 == 0)
			isBlack = true;
		else
			isBlack = false;
		
		if (ai.isBlack == isBlack && !isAIDone) {
			
			for (int i = 0; i < 2; i ++) {
				Point p = ai.AI_stone(map, current1, current2);
				
				if (ai.isBlack == true) {
					map[p.x][p.y] = 1;
				}
				else
					map[p.x][p.y] = 0;
				
				stone_count ++;
				
				// checks if game is over
				result = is_game_over(p.x, p.y);
				
				if (result != -1) {
					game_over (result);
				}
			}
			
			isAIDone = true;
			repaint();
		}
		
		else {
			isAIDone = false;
		}

		
	}
	
	private void game_over (int result)
	{ 					
		  int delay = 2500;
		 		  
		  // indicates end of game 
		  history.add(-1);
	  
		  ActionListener al_1 = new ActionListener() {
		      public void actionPerformed(ActionEvent e) {
		    	  	panel_go = new Panel_GameOver (result, start_x, start_y, end_x, end_y);
		    	  	
		    	  	add (panel_go);
		    		
		    		repaint();
		    		revalidate();
		    		
		    		t2.stop();
		      }
		  };
		  t2 = new Timer(delay, al_1);
		  t2.start();
	}
	
	
	private void draw_winning_stones ()
	{	
		// draw line on winning stones
		g2.setColor(new Color(237, 33, 33));
		g2.setStroke(new BasicStroke(4));
		
		start_y++;
		end_y++;
		
		if (start_x < 1000) {
			start_x *= gap;
			start_y *= gap;
			end_x *= gap;
			end_y *= gap;
		}
	  	
		Line2D line = new Line2D.Double(start_x + gap, start_y, end_x + gap, end_y);
		

		
		g2.draw(line);	
		

		//
		t1 = new Timer(2000, new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				repaint();
				t1.stop();
			}
		});
		t1.setRepeats(false);
		t1.start();
	}
	
	public void draw_other_stone(int x, int y)
	{
		// checks if x, y index is redundant
		if (map[x][y] != -1)	 return;
		
		// add x,y index to vector
		if (isBlack) {
			map[x][y] = 1;
			history.add(x * 100 + y);
		}
		else {
			map[x][y] = 0;
			history.add(x * 100 + y);
		}
		
		stone_count ++;
		
		// checks if game is over
		result = is_game_over(x, y);
		
		if (result != -1) {
			game_over(result);
		}
		
		repaint();
	}
	
	
	public void init ()
	{
		// initialize instance variables
		 map = new short[19][19];
		 
		 for (int i = 0; i < 19; i ++) {
			 for (int j = 0; j < 19; j ++) {
				 map[i][j] = -1;
			 }
		 }
		 
		map[9][9] = 1;
		 
		start_x = -1;
		start_y = -1;
		end_x = -1;
		end_y = -1;
		
		isGameOver = false;
		
		isBlack = false;
		
		stone_count = 1;
		
		history.clear();
		history.add(909);
		
		drew_winning_stones = false;
		
		panel_go.setVisible(false);
		if (Main.frame.rep != null)
			Main.frame.rep.setVisible(false);
		
		ai = new AI(false);
		
		//Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Get_Go.wav");
		
		repaint();
		revalidate();
	}

}