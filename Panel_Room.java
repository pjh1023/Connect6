import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Panel_Room extends JPanel implements MouseListener {
	
	int width = 220;
	int height = 100;
	
	JLabel title;
	JLabel content;
	
	int room_no = -1;
	
	public Panel_Room (int room_no, int x, int y) {
		// set size / location for panel
		setSize(width, height);
		setLocation(x, y);
		
		this.room_no = room_no;
		
		// label for room title
		title = new JLabel(Integer.toString(room_no), SwingConstants.CENTER);
		title.setLocation(1, 1);
		title.setSize(218, 38);
		title.setBackground(new Color(143, 176, 123));
		title.setForeground(Color.WHITE);
		title.setOpaque(true);
		title.setFont(new Font("Herculanum", Font.PLAIN, 20));
		
		add(title);
		
		
		// label for room content
		content = new JLabel("(Empty)", SwingConstants.CENTER);
		content.setLocation(1, 42);
		content.setSize(218, 38);
		content.setBackground(new Color(83, 135, 83));
		content.setForeground(Color.WHITE);
		content.setOpaque(true);
		content.setFont(new Font("Herculanum", Font.PLAIN, 20));
		
		addMouseListener(this);
		add(content);
		
		setBackground(new Color(83, 135, 83));
		setLayout(null);
		setVisible(true);
		
		repaint();
		
	}
	
	public void paintComponent (Graphics g)
	{
		// initiate Graphics2D
		super.paintComponent (g);
		
		Graphics2D g2 = (Graphics2D) g;
		g2.setRenderingHint(
	            RenderingHints.KEY_ANTIALIASING,
	            RenderingHints.VALUE_ANTIALIAS_ON);
		
		
		// draw frame of the panel
		g2.setStroke(new BasicStroke(3));
		
		if (room_no != Main.frame.start.selected)
			g2.setColor(new Color(163, 215, 163));
		else
			g2.setColor(Color.RED);
		
		g2.drawLine(0, 0, width, 0);
		g2.drawLine(width, 0, width, height);
		g2.drawLine(width, height, 0, height);
		g2.drawLine(0, height, 0, 0);
		
		g2.setColor(Color.WHITE);
		g2.drawLine(0, 40, width, 40);
	}
	
	public void update_room(String client1, String client2)
	{
		int count = 0;
		boolean is_client1 = false;
		boolean is_client2 = false;
				
		if (client1.compareTo("$") != 0) {
			count ++;
			is_client1 = true;
		}
		
		if (client2.compareTo("$") != 0) {
			count ++;
			is_client2 = true;
		}
		
		if (count == 0)
			content.setText("(Empty)");
		
		else if (count == 1) {
			if (is_client1)
				content.setText("(1/2) " + client1);
			else
				content.setText("(1/2) " + client2);
		}
		
		else
			content.setText("(Full");
		
		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		Main.frame.start.change_selected(room_no);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
