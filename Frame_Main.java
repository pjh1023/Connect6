import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

public class Frame_Main extends JFrame implements ActionListener {
	
	Frame_LogIn login;
	
	Panel_Start start;
	
	Panel_Board board;
	
	//Panel_OtherUser otherUser_panel;
	//Panel_ThisUser thisUser_panel;
	
	//Panel_ChattingRoom chatroom;
	
	Panel_Replay rep;
	JLabel replay_info;
	
	JButton home;
	JButton message;
	
	Timer replay;
	int timer_count = 0;
	
	String nickname = "Me";
	
	int prev_selected = -1;
	
	
	public Frame_Main()
	{
		// set title, size, location of frame
		setTitle("My Connect 6");
		setSize(1008, 870);
		setLocation(0, 0);
		

		// set background color
		getContentPane().setBackground(new Color(4, 61, 10));
		
		
		// generate random nickname
		int num = (int)(Math.random() * 1000000 + 1);
		nickname += Integer.toString(num);

		
	//	Main.client = new Network_Client(nickname);
		
		
		// initiate board panel
		board = new Panel_Board();
		
		
		// add start panel	
		start = new Panel_Start(nickname);
		add(start);
		
		
		// initiate user panels
		//otherUser_panel = new Panel_OtherUser (new User("Jihyun"), true);
		
		//thisUser_panel = new Panel_ThisUser (new User("Chaewon"), false);
		
		
		// initiate chatroom panels
		//chatroom = new Panel_ChattingRoom();
		
		
		// add home button
		home = new JButton("Home");
		home.setSize(90, 60);
		home.setLocation(10, 890);
		home.setForeground(Color.RED);
		home.setFont(new Font("Herculanum", Font.PLAIN, 20));
		home.addActionListener(this);
		
		add(home);
		
		// add message button
		message = new JButton("Message");
		message.setSize(40, 40);
		message.setLocation(10, 840);
		message.setFont(new Font("Herculanum", Font.PLAIN, 10));

		try {
			Image img = ImageIO.read(getClass().getResource("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/icon_boy.png"));
			message.setIcon(new ImageIcon(img));
		}
		catch(Exception e) {}

		
		
		// add chatting room panel
		board.panel_go.playAgain.addActionListener(this);
		start.start.addActionListener(this);
		start.login.addActionListener(this);
			
		// set layout option, etc.
		setLayout(null);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		setResizable(false);
		setVisible(true);
	}
	
	
	public void play_again()
	{				
		board.init();
		
		String input = JOptionPane.showInputDialog("Black or White?");
		
		if (input.equalsIgnoreCase("black")) {
			board.amIBlack = true;
			board.ai.isBlack = false;
		}
		else {
			board.amIBlack = false;
			board.ai.isBlack = true;
		}
		
		
		
		repaint();
		revalidate();
	}
	
	public void ready_game()
	{
		start.start.setText("Ready");
		start.repaint();
	}
	
	public void start_game(String user1, String user2)
	{
		Sound.play("/Users/jihyunlee/eclipse-workspace/Round5_Connect6(2)/Appalause.wav");
		
		start.setVisible(false);
		
		
		if (nickname.equalsIgnoreCase(user1)) {
			//thisUser_panel = new Panel_ThisUser(new User(user1), true);
			//otherUser_panel = new Panel_OtherUser(new User(user2), false);
		}
		else {
			//thisUser_panel = new Panel_ThisUser(new User(user2), false);
			//otherUser_panel = new Panel_OtherUser(new User(user1), true);
		}
		
		//add(chatroom);
				
		add(board);
		//add(otherUser_panel);
		//add(thisUser_panel);
		
		Main.frame.board.setVisible(true);
		Main.frame.board.panel_go.setVisible(false);
		//Main.frame.chatroom.setVisible(true);
		
		board.init();
		
		String input = JOptionPane.showInputDialog("Black or White?");
		
		if (input.equalsIgnoreCase("black")) {
			board.amIBlack = true;
			board.ai.isBlack = false;
		}
		else {
			board.amIBlack = false; 
			board.ai.isBlack = true;

		}
		
		
		Main.frame.start.start.setText("Start");
		
		repaint();
	}
	
	public void go_back_to_start()
	{
		//chatroom.setVisible(false);
		board.setVisible(false);
		//thisUser_panel.setVisible(false);
	//	otherUser_panel.setVisible(false);
		
		start.setVisible(true);
	}
	
	
	public void replay(Vector<Integer> history, int result, int in_startX, int in_startY, int in_endX, int in_endY) {
		board.panel_go.setVisible(false);
		board.setVisible(false);
		
		rep = new Panel_Replay(history, result, in_startX, in_startY, in_endX, in_endY);
		
		add(rep);
		
		// add "replay >>" label
		replay_info = new JLabel("Replay >>");
		replay_info.setLocation(578, 10);
		replay_info.setSize(200, 30);
		replay_info.setFont(new Font("Candara", Font.PLAIN, 20));
		
		replay_info.setBackground(new Color(4, 61, 10));
		replay_info.setForeground(Color.RED);
		
		add(replay_info);
	
		// 깜빡거리는 효과
		replay = new Timer(800, new ActionListener() {
			public void actionPerformed (ActionEvent e) {
				timer_count ++;
				
				if (timer_count % 2 == 1)
					replay_info.setText("");
				else
					replay_info.setText("Replay >>");
				
				repaint();
			}
		});
		replay.setRepeats(true);
		replay.start();
		
		repaint();
		revalidate();
	}
	
	public void end_replay()
	{
		board.init();
		board.setVisible(true);
		board.panel_go.setVisible(true);
		rep.setVisible(false);
		
		replay.stop();
		replay_info.setText("");
			
		repaint();
		revalidate();
	}
	
	
	public void actionPerformed (ActionEvent e)
	{
		if (e.getActionCommand() == "Play Again")
			play_again();
		
		else if (e.getActionCommand() == "Start" || e.getActionCommand() == "Ready") {		
			// 이전 룸 넘버 저장해놨다가 remove client 부르기
			/*if (prev_selected != -1)
				Main.client.clientSender.sendMsg("[Exit]," + (prev_selected-1) + "," + nickname);
			
			Main.client.room_no = start.selected - 1;
			Main.client.clientSender.sendMsg("[room]," + (start.selected-1) + "," + nickname);
			
			chatroom.output.setText("");*/
			
			ready_game();
			
			start_game("Guest", "Me");
			
			//prev_selected = start.selected;
		}
		
		else if (e.getActionCommand() == "Log In") {
			login = new Frame_LogIn();
			//nickname = JOptionPane.showInputDialog("Input your nickname.");
			//thisUser_panel = new Panel_ThisUser (new User(nickname), false);
		}
		
		else if (e.getActionCommand() == "Home") {
			//Main.client.clientSender.sendMsg("[Exit]," + (start.selected-1) + "," + nickname);
			start.selected = -1;
			prev_selected = -1;
			Main.frame.go_back_to_start();
		}
	}

}