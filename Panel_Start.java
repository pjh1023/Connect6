import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

public class Panel_Start extends JPanel {

	JLabel image;
	JButton start;
	JButton login;
	JButton message;

	JLabel welcome;
	
	Panel_Room room[] = new Panel_Room[4];
	
	int selected = 0;
	
	public Panel_Start(String nickname)
	{
		// add room 1
		room[0] = new Panel_Room(1, 750, 40);
		add(room[0]);
		
		
		// add room 2
		room[1] = new Panel_Room(2, 750, 160);
		add(room[1]);
		
		
		// add room 3
		room[2] = new Panel_Room(3, 750, 280);
		add(room[2]);
		
		// add room 4
		room[3] = new Panel_Room(4, 750, 400);
		add(room[3]);
		
		// set size, location of panel
		setSize(1008, 870);
		setLocation(0, 0);
		
		setOpaque(true);
		
		setBackground(new Color(4, 61, 10));
		
		
		// add image label
		image = new JLabel();
		image.setLocation(0, 400);
		image.setSize(708, 390);
		
		ImageIcon icon;
		Image im;
		
		icon = new ImageIcon( "/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/start_goPhoto.jpg" );
		
		im = icon.getImage();
		im = im.getScaledInstance(708, 390, java.awt.Image.SCALE_SMOOTH);
		icon = new ImageIcon(im);
		
		image.setIcon(icon);
		
		add(image);
		
		
		// add name label
		JLabel connect = new JLabel("CONNECT");
		connect.setLocation(60, 0);
		connect.setSize(500, 200);
		
		connect.setFont(new Font("Herculanum", Font.PLAIN, 70));	
		connect.setForeground(Color.WHITE);
		connect.setBackground(new Color(4, 61, 10));
		
		add(connect);
		
		JLabel six = new JLabel("6");
		six.setLocation(60, 80);
		six.setSize(500, 200);
		
		six.setFont(new Font("Herculanum", Font.PLAIN, 70));	
		six.setForeground(Color.WHITE);
		six.setBackground(new Color(4, 61, 10));
		
		add(six);
		
		
		// add start button
		start = new JButton("Start");
		start.setLocation(775, 720);
		start.setSize(160, 60);
		
		start.setFont(new Font("Herculanum", Font.PLAIN, 30));
		start.setForeground(Color.RED);
				
		add(start);
		
		
		// add log in button
		login = new JButton("Log In");
		login.setLocation(775, 620);
		login.setSize(160, 60);
		
		login.setFont(new Font("Herculanum", Font.PLAIN, 30));
				
		add(login);
		
		
		// add welcome message
		welcome = new JLabel("Welcome," + nickname + "!", SwingConstants.RIGHT);
		welcome.setLocation(340, 280);
		welcome.setSize(370, 200);
		
		welcome.setFont(new Font("Herculanum", Font.PLAIN, 30));	
		welcome.setForeground(Color.WHITE);
		welcome.setBackground(new Color(4, 61, 10));
		
		add(welcome);
		
		
		// add message button
		message = new JButton("M");
		message.setSize(40, 40);
		message.setLocation(10, 800);
		message.setFont(new Font("Herculanum", Font.PLAIN, 10));
		try {
			Image img = ImageIO.read(getClass().getResource("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/icon_boy.png"));
			message.setIcon(new ImageIcon(img));
		}
		catch(Exception e) {}
		
		//add(message);

		
		// set layout option, etc.
		setLayout(null);		
		setVisible(true);
	}
	
	public void change_selected(int n)
	{
		selected = n;
		
		room[0].repaint();
		room[1].repaint();
		room[2].repaint();
		room[3].repaint();
	}
	
	public void update_room(int room_no, int client_no, String client_id)
	{
		if (client_no == 1) {
			room[room_no].content.setText("(1/2) " + client_id);
			room[room_no].repaint();
		}
		
		else if (client_no == 2) {
			room[room_no].content.setText("(2/2) full");
		}
					
	}
}
