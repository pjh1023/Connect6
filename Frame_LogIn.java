import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

public class Frame_LogIn extends JFrame implements ActionListener {
	
	JTextField id_field;
	JPasswordField pw_field;
	
	public Frame_LogIn()
	{
		// set title, size, location of frame
		setTitle("Log-In");
		setSize(400, 600);
		setLocation(150, 100);
		
		
		// set background color
		getContentPane().setBackground(new Color(4, 61, 10));
		
		
		// label for log-in message
		JLabel info = new JLabel("Log-In!");
		info.setFont(new Font("Herculanum", Font.PLAIN, 18));
		info.setOpaque(true);
		info.setBackground(Color.WHITE);
		info.setLocation(150, 95);
		info.setSize(130, 45);
		info.setHorizontalAlignment(SwingConstants.CENTER);
		add(info);
		
		
		// label and text field for id
		My_Label id_label = new My_Label("ID:", 100, 190);
		add(id_label);
				
		id_field = new JTextField();
		id_field.setSize(160, 35);
		id_field.setLocation(155, 188);
		add(id_field);
				
				
		// label and text field for pw
		My_Label pw_label = new My_Label("PW:", 100, 260);
		add(pw_label);
				
		pw_field = new JPasswordField();
		pw_field.setSize(160, 35);
		pw_field.setLocation(155, 258);
		add(pw_field);
		
		
		// create "log_in" button
		JButton log_in = new JButton("Log In");
		log_in.setSize(67, 35);
		log_in.setLocation(245, 350);
		log_in.setFont(new Font("Herculanum", Font.PLAIN, 14));
		log_in.setForeground(Color.RED);
		log_in.addActionListener(this);
		add(log_in);
		
		
		// set layout option, etc.
		setLayout(null);
				
		setResizable(false);
		setVisible(true);
	}
	
	class My_Label extends JLabel {
		
		public My_Label (String name, int x, int y)
		{
			super(name);
			setFont(new Font("Herculanum", Font.PLAIN, 16));
			setOpaque(true);
			setBackground(Color.WHITE);
			setLocation(x, y);
			setSize(40, 30);
			setHorizontalAlignment(SwingConstants.CENTER);
		}
	}
	
	public void actionPerformed (ActionEvent e)
	{
		String id = "";
		String pw = "";
		
		id = id_field.getText();
		pw = pw_field.getText();
			
		Main.frame.nickname = id;
	//	Main.client.clientSender.sendMsg("[login]," + id + "," + pw);
	}
	
	
	public void close_login() 
	{
		Main.frame.start.welcome.setText("Welcome, " + Main.frame.nickname + "!");
		
		setVisible(false);
		dispose();
	}
}
