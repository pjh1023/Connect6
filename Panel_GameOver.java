import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class Panel_GameOver extends JPanel implements ActionListener {

	int width = 500;
	int height = 300;
	
	JLabel info1;
	JLabel info2;
	
	JButton playAgain;
	JButton exit;
	JButton replay;
	
	Panel_Replay replayPanel;
	
	int result;
	
	int start_x;
	int start_y;
	int end_x;
	int end_y;
	
	// 여기 수정
	public Panel_GameOver ()
	{
		// set size / location for panel
		setSize(width, height);
		setLocation(92, 190);
		
		setBackground(new Color(84, 111, 60));
		setLayout(null);
		
		
		// set winner string
		String winner = "";
		
		// initiate info labels
		info1 = new JLabel ("Game Over!");
		info1.setFont(new Font("Apple Chancery", Font.BOLD, 30));
		info1.setOpaque(true);
		info1.setBackground(new Color(84, 111, 60));
		info1.setForeground(Color.WHITE);
		info1.setLocation(185, 40);
		info1.setSize(170, 40);
		add(info1);
		
		info2 = new JLabel ("Winner is '" + winner + "'");
		info2.setFont(new Font("Apple Chancery", Font.BOLD, 30));
		info2.setOpaque(true);
		info2.setBackground(new Color(84, 111, 60));
		info2.setForeground(Color.WHITE);
		info2.setLocation(130, 95);
		info2.setSize(280, 40);
		add(info2);
		
		
		// add "Play Again" button
		playAgain = new JButton("Play Again");
		playAgain.setSize(127, 80);
		playAgain.setLocation(80, 155);
		playAgain.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		playAgain.setForeground(Color.RED);
		playAgain.addActionListener((ActionListener) this);
		add(playAgain);
		
		
		// add "Replay" button
		replay = new JButton("Replay");
		replay.setSize(40, 40);
		replay.setLocation(155, 155);
		replay.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		replay.setForeground(Color.RED);
		replay.addActionListener((ActionListener) this);
		add(replay);
		
		
		// add "Exit" button
		exit = new JButton("Exit");
		exit.setSize(127, 80);
		exit.setLocation(280, 155);
		exit.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		exit.setForeground(Color.RED);
		exit.addActionListener((ActionListener) this);
		add(exit);
				
		
		repaint();
	}
	
	public Panel_GameOver (int in_result, int in_start_x, int in_start_y, int in_end_x, int in_end_y)
	{
		// set size / location for panel
		setSize(width, height);
		setLocation(92, 190);
		
		setBackground(new Color(84, 111, 60));
		setLayout(null);
		
		//Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Appalause.wav");
		
		start_x = in_start_x;
		start_y = in_start_y;
		end_x = in_end_x;
		end_y = in_end_y;

		
		// figure out who's winner and who's loser
		//User winner = null;
		//User loser = null;
		
		/*if (Main.frame.board.result == 1) {
			if (Main.frame.thisUser_panel.isBlack == true) {
				winner = Main.frame.thisUser_panel.user;
				loser = Main.frame.otherUser_panel.user;
			}
			else {
				winner = Main.frame.otherUser_panel.user;
				loser = Main.frame.thisUser_panel.user;
			}
		}
		else {
			if (Main.frame.thisUser_panel.isBlack == false) {
				winner = Main.frame.thisUser_panel.user;
				loser = Main.frame.otherUser_panel.user;
			}
			else {
				winner = Main.frame.otherUser_panel.user;
				loser = Main.frame.thisUser_panel.user;
			}
		}
		
		winner.win ++;
		loser.lose ++;
		
		Main.frame.thisUser_panel.update();
		Main.frame.otherUser_panel.update();*/
		
		// initiate info labels
		info1 = new JLabel ("Game Over!");
		info1.setFont(new Font("Apple Chancery", Font.BOLD, 30));
		info1.setOpaque(true);
		info1.setBackground(new Color(84, 111, 60));
		info1.setForeground(Color.WHITE);
		info1.setLocation(185, 70);
		info1.setSize(170, 40);
		add(info1);
		
		info2 = new JLabel ("Winner is '" + "'");
		info2.setFont(new Font("Apple Chancery", Font.BOLD, 24));
		info2.setOpaque(true);
		info2.setBackground(new Color(84, 111, 60));
		info2.setForeground(Color.WHITE);
		info2.setLocation(130, 95);
		info2.setSize(280, 40);
		//add(info2);
		
		
		// add "Play Again" button
		playAgain = new JButton("Play Again");
		playAgain.setSize(147, 80);
		playAgain.setLocation(80, 165);
		playAgain.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		playAgain.setForeground(Color.RED);
		playAgain.addActionListener((ActionListener) this);
		add(playAgain);
		
		
		// add "Replay" button
		replay = new JButton("Replay");
		replay.setSize(87, 30);
		replay.setLocation(10, 10);
		replay.setFont(new Font("Apple Chancery", Font.PLAIN, 14));
		replay.setForeground(Color.BLACK);
		replay.addActionListener((ActionListener) this);
		add(replay);
		
		
		// add "Exit" button
		exit = new JButton("Exit");
		exit.setSize(147, 80);
		exit.setLocation(280, 165);
		exit.setFont(new Font("Apple Chancery", Font.BOLD, 20));
		exit.setForeground(Color.RED);
		exit.addActionListener((ActionListener) this);
		add(exit);
				
		
		result = in_result;
		
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
		g2.setStroke(new BasicStroke(2));
		g2.setColor(Color.WHITE);
		g2.drawLine(0, 0, width, 0);
		g2.drawLine(width, 0, width, height);
		g2.drawLine(width, height, 0, height);
		g2.drawLine(0, height, 0, 0);
	}
	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand() == "Play Again") {
			//Main.client.clientSender.sendMsg("[play again ready]," + Main.frame.start.selected);
			
			playAgain.setText("Ready");
			play_again();
			
			repaint();
		}
		
		else if (e.getActionCommand() == "Exit") {
			/*int reply = JOptionPane.showConfirmDialog(null, "Are you sure?", "WARNING", JOptionPane.YES_NO_OPTION);
			
			if (reply == JOptionPane.YES_OPTION) {
				System.exit(0);
			}*/
			//Main.client.clientSender.sendMsg("[Exit]," + (Main.frame.start.selected - 1) + "," + Main.frame.nickname);
			Main.frame.go_back_to_start();
		}
		
		else if (e.getActionCommand() == "Replay") {
			this.setVisible(false);
			
			Main.frame.replay(Main.frame.board.history, result, start_x, start_y, end_x, end_y);
			
		}
	}
	
	public void play_again()
	{
		Main.frame.play_again();
		playAgain.setText("Play Again");
	}
	
	public void set_result (int in_result) {
		result = in_result;
	}
	
	public void identify_winner()
	{
		// figure out who's winner and who's loser
		//User winner = null;
		//User loser = null;
		
		/*if (Main.frame.board.result == 1) {
			if (Main.frame.thisUser_panel.isBlack == true) {
				winner = Main.frame.thisUser_panel.user;
				loser = Main.frame.otherUser_panel.user;
			}
			else {
				winner = Main.frame.otherUser_panel.user;
				loser = Main.frame.thisUser_panel.user;
			}
		}
		else {
			if (Main.frame.thisUser_panel.isBlack == false) {
				winner = Main.frame.thisUser_panel.user;
				loser = Main.frame.otherUser_panel.user;
			}
			else {
				winner = Main.frame.otherUser_panel.user;
				loser = Main.frame.thisUser_panel.user;
			}
		}*/
		
		//winner.win ++;
		//loser.lose ++;
		
		//Main.frame.thisUser_panel.update();
		//Main.frame.otherUser_panel.update();
		
		//info2.setText("Winner is '" + winner.nickname + "'");
	}
}
