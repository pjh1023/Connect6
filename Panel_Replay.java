import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Line2D;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.Timer;


public class Panel_Replay extends JPanel {

	Graphics2D g2;
	
	final static int width = 670;
	final static int height = 670;
	final static int gap = 668 / 18;		// gap between grids
		
	int stone_count = 1;		// counts the first stone in the middle

	int start_x = -1;
	int start_y = -1;
	int end_x = -1;
	int end_y = -1;
	
	Timer timer;
	Timer t1;
	int timer_count = 0;
		
	int vector_index = 0;
		
	boolean isGameOver = false;
	
	int result = 0;
	
	Vector<Integer> history;
		
		
	public Panel_Replay(Vector<Integer> in_history, int in_result, int in_startX, int in_startY, int in_endX, int in_endY)
	{
		// set size / location for panel
		setSize(669, 669);
		setLocation(19, 89);
		
		setBackground(new Color(255, 185, 106));
		setLayout(null);
		
		history = in_history;
		
		start_x = in_startX;
		start_y = in_startY;
		end_x = in_endX;
		end_y = in_endY;
		
		result = in_result;
		// 깜빡거리는 효과
		timer = new Timer(1000, new ActionListener() {
			public void actionPerformed (ActionEvent e) {	
				if (isGameOver)
					timer.stop();
				
				vector_index ++;
				
				Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Put_Go.wav");
				
				repaint();
			}
		});
		
		timer.setRepeats(true);
		timer.start();
		
		setVisible(true);
		
		revalidate();
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
		
			
		// draw stones
		if (!isGameOver)
			draw_stones(vector_index);
	}
	
	
	// method to draw board lines
	private void draw_board()
	{
		g2.setColor(new Color(84, 42, 4));
		g2.setStroke(new BasicStroke(1));
		
		for (int i = 1; i <= width; i += gap) {
			g2.drawLine(i, 0, i, height);
		}
		
		for (int i = 1; i <= height; i += gap) {
			g2.drawLine(0, i, width, i);
		}
		
		int radius = 3;
		for (int i = gap * 3 + 1; i < width; i += gap * 6) {
			for (int j = gap * 3 + 1; j < height; j += gap * 6) {
				g2.fillOval(i - radius, j - radius, radius * 2, radius * 2);
			}
		}
	}
	
	
	private void draw_stones (int index)
	{
		int radius = 35;
				
		for (int i = 0; i <= vector_index; i ++) {
			
			if (history.get(i) == -1) {
				draw_winning_stones ();
				return;
			}
				
			else if (((i + 1) / 2) % 2 == 0 ) {
				g2.setColor(Color.BLACK);
			}
			else
				g2.setColor(Color.WHITE);
				
			int x_coordinate = (gap * (history.get(i)/100) + 1) - (radius / 2);
			int y_coordinate = (gap * (history.get(i)%100) + 1) - (radius / 2);
				
			g2.fillOval(x_coordinate, y_coordinate, radius, radius);
		}		
	}

	
	private void draw_winning_stones ()
	{	
		// draw line on winning stones
		g2.setColor(new Color(237, 33, 33));
		g2.setStroke(new BasicStroke(4));
		
		Line2D line = new Line2D.Double(start_x, start_y, end_x, end_y);
		
		g2.draw(line);
		timer.stop();
		isGameOver = true;

		// 깜빡거리는 효과
		t1 = new Timer(1000, new ActionListener() {
			public void actionPerformed (ActionEvent e) {				
				Main.frame.end_replay();
				
				Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Appalause.wav");
				
				t1.stop();
			}
		});
		t1.setRepeats(false);
		t1.start();
	}
}