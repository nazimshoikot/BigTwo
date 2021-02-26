import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.*;
import javax.swing.text.DefaultCaret;


/**
 * The BigTwoTable class implements the CardGameTable interface. 
 * It is used to build a GUI for the Big Two card game and handle 
 * all user actions
 * 
 * @author Nazim Shoikot
 *
 */
public class BigTwoTable implements CardGameTable {
	
	//Private instance variables
	private BigTwoClient game; // a card game associates with this table
	private boolean[] selected; // a boolean array indicating which cards are being selected
	private int activePlayer; // an integer specifying the index of the active player
	private JFrame frame; // the main window of the application
	private JPanel bigTwoPanel; // a panel for showing the cards of each player and the cards played on the table
	private JButton playButton; // a Play button for the active player to play the selected cards
	private JButton passButton; // a Pass button for the active player to pass his/her turn to the next player
	private JTextArea msgArea; // a text area for showing the current game status as well as end of game messages
	private JTextArea msgArea2; //a text area for showing the chat messages sent by the players
	private Image[][] cardImages; //a 2D array storing the images for the faces of the cards
	private Image cardBackImage; //an image for the back of the cards
	private Image[] avatars; //an array storing the images for the avatars
	private Image Board;
	int[][] imageX;
	int[][] imageY;
	
	private JTextField msg_input; 
	private boolean[] gamePlayers; // an array to know which players are playing the game
	private JPanel msgInputArea; // panel to have the text field to send messages
	
	private int NUM_OF_SUITS = 4;
	private int NUM_OF_RANKS = 13;
	private int imageWidth = 150;
	private int imageHeight = 150;
	
	/**
	 * A constructor for creating a BigTwoTable.
	 * 
	 * @param game a reference to a card game associates with this table
	 */
	public BigTwoTable(BigTwoClient game){
		this.game = game;
		selected = new boolean[NUM_OF_RANKS];
		cardImages = new Image[NUM_OF_SUITS][NUM_OF_RANKS];
		imageX = new int[NUM_OF_SUITS][NUM_OF_RANKS];
		imageY = new int[NUM_OF_SUITS][NUM_OF_RANKS];
		avatars = new Image[4];
		
		gamePlayers = new boolean[4];
		for (int i = 0; i < 4; i++) {
			gamePlayers[i] = false;
		}
		
		this.createFrame();
		msgArea.setEditable(false);
		
	}
	
	/*
	 * A method to create the frame for the game
	 */
	private void createFrame() {
		
		frame = new JFrame("BigTwo");
		
		//Create a panel for drawing the images
		bigTwoPanel = new BigTwoPanel();
				
		//Create the left panel
		JPanel leftPanel = new JPanel(new BorderLayout());
		leftPanel.add(bigTwoPanel, BorderLayout.CENTER);
			
		//loading avatar images 
		for (int i = 0; i < 4; i++) {
			avatars[i] = new ImageIcon("avatars/" + (i) + ".png").getImage();
		}
		
		//loading the images onto the array
		char[] suit = {'d','c','h','s'};
		char[] rank = {'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k'};
		
		for (int i = 0; i < NUM_OF_SUITS; i++) {
			
			for (int j = 0; j < NUM_OF_RANKS; j++) {
				cardImages[i][j] = new ImageIcon("images/" + rank[j] + suit[i] + ".gif").getImage();
			}
		}
		
		//Create buttons add them to a panel 
		playButton = new JButton("Play");
		passButton = new JButton("Pass");
		JPanel buttonPanel = new JPanel();
		
		playButton.addActionListener(new PlayButtonListener());
		passButton.addActionListener(new PassButtonListener());
		
		buttonPanel.add(playButton, BorderLayout.SOUTH);
		buttonPanel.add(passButton, BorderLayout.SOUTH);
		
		
		//Add the button panel to the leftPanel
		leftPanel.add(buttonPanel,BorderLayout.SOUTH);
		
		//Create text area for game messages
		msgArea = new JTextArea(50,40);
		msgArea.setEditable(false);
		msgArea.append("Game Messages\n");
		msgArea.setLineWrap(true);
		DefaultCaret caret = (DefaultCaret)msgArea.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		//Dimension for the text areas
		Dimension textAreaDimension = new Dimension(400,445);
		
		//Scrollbar for msgArea
		JScrollPane scroll = new JScrollPane (msgArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.setMinimumSize(textAreaDimension);
		scroll.setPreferredSize(textAreaDimension);
		
		

		//Create text area for chat messages
		msgArea2 = new JTextArea(50,40);
		DefaultCaret caret2 = (DefaultCaret) msgArea2.getCaret();
		msgArea2.setEditable(false);
		caret2.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea2.append("Chat Box\n");
		msgArea2.setLineWrap(true);
		
		
		//create scrollbar for msgArea2
		JScrollPane scroll2 = new JScrollPane (msgArea2, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll2.setPreferredSize(textAreaDimension);
		scroll2.setMinimumSize(textAreaDimension);
		
		
		//Label for the Textfield to send chat messages
		JLabel messageLabel = new JLabel("Message: ");
		
		//Textfield for inputting the messages
		msg_input = new ChatField(40);
		
		//Panel to have the label and text field
		msgInputArea = new JPanel();
		msgInputArea.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		//add the label and textfield to the panel
		msgInputArea.add(messageLabel);
		msgInputArea.add(msg_input);
		msgInputArea.setMinimumSize(new Dimension(300,80));
		
		//JSplitPane for the two text areas
		JSplitPane textAreaPanel = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scroll, scroll2);
		textAreaPanel.setOneTouchExpandable(false);
		textAreaPanel.setDividerLocation(440);
		textAreaPanel.setMinimumSize(new Dimension(400,890));
		
		//JSplitPane for the text areas and the text field
		JSplitPane rightPanel = new JSplitPane (JSplitPane.VERTICAL_SPLIT, textAreaPanel, msgInputArea);

		//Add the right panel to the frame
		frame.add(rightPanel, BorderLayout.EAST);
			
		//Create Menu
		JMenuBar menuBar = new JMenuBar();
		JMenu game_menu = new JMenu("Game");
		JMenu messageMenu = new JMenu("Message");
		
		//Create menu items
		JMenuItem connect = new JMenuItem("Connect");
		connect.addActionListener(new ConnectMenuItemListener());
		
		JMenuItem quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		
		//add menu items
		game_menu.add(connect);
		game_menu.add(quit);
				
		
		//create and add item to clear game messages
		JMenuItem eraseGameMessages = new JMenuItem("Clear Game Messages");
		eraseGameMessages.addActionListener(new eraseGameMessagesListener());
		
		//create and add item to clear chat messages
		JMenuItem eraseChatMessages = new JMenuItem("Clear Chat Board Messages");
		eraseChatMessages.addActionListener(new eraseChatMessagesListener());
		
		//add the menus to the menu bar
		messageMenu.add(eraseGameMessages);
		messageMenu.add(eraseChatMessages);
		
		//add the menus in the menu bar
		menuBar.add(game_menu);
		menuBar.add(messageMenu);
		
		//Add the leftPanel to the frame
		frame.add(leftPanel, BorderLayout.CENTER);
		
		//Add the menubar to the frame
		frame.setJMenuBar(menuBar);
		
		//Enable or disable the buttons
		if (game.getCurrentIdx() == activePlayer) {
			enable();
		}
		else {
			disable();
		}
		
		//Create a window frame
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(1300,1000);
		frame.setVisible(true);		
	}
	
	//inner Class ChatField is a subclass of JTextField and implements ActionListener
	class ChatField extends JTextField implements ActionListener {
		
		private static final long serialVersionUID = 1L;
		/**
		 * Constructor for a Chatfield object
		 * 
		 * @param i integer representing the width of the CharField
		 */
		public ChatField(int i) {
			super(i);
			addActionListener(this);
		}
		
		@Override
		public void actionPerformed(ActionEvent event) {
			String chatText = getText();
			
			if ((!chatText.trim().isEmpty()) && chatText != null) {
				CardGameMessage message = new CardGameMessage(7, activePlayer, chatText);
				game.sendMessage(message);
				
			}
			
			this.setText("");
		}
		
	}
	
	/**
	 * A method to add a player to the list of game players present during a game
	 * 
	 * @param i integer representing the index of the player (from the gamePlayers array)
	 */
	public void joinPlayer(int i) {
		this.gamePlayers[i] = true;
	}
	
	/**
	 * A method to remove a player from the list of game players present during a game (from the gamePlayers array)
	 * 
	 * @param i index of the player to be removed
	 */
	public void removePlayer(int i) {
		this.gamePlayers[i] = false;
	}


	@Override
	public void setActivePlayer(int activePlayer) {
		this.activePlayer = activePlayer;
		
	}

	@Override
	public int[] getSelected() {
		int num_selected = 0;
		int new_index = 0;
		int[] sel_cards = null;
		
		//check which cards are selected
		for (int i = 0; i < NUM_OF_RANKS; i++) {
			if (selected[i] == true) {
				num_selected++;
			}
		}
		
		//put the indexes of the selected cards in an array
		if (num_selected != 0) {
			sel_cards = new int[num_selected];
			
			for (int i = 0; i < NUM_OF_RANKS; i++) {
				if (selected[i] == true) {
					sel_cards[new_index] = i;
					new_index++;
				}
			}
		}
		
		return sel_cards;
	}

	@Override
	public void resetSelected() {
		//set all elements of selected to false
		for (int i=0; i < NUM_OF_RANKS; i++) {
			selected[i] = false;
		}
		
	}

	@Override
	public void repaint() {
		bigTwoPanel.repaint();
		
	}

	@Override
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
		
	}

	@Override
	public void clearMsgArea() {
		msgArea.setText("Game Messages\n");
		
	}

	@Override
	public void reset() {

		resetSelected();
		clearMsgArea();
		enable();
		
	}

	/**
	 * A method to enable or disable the button of the panel depending on the player's turn.
	 * It enables the button when it is the player's turn and disables them otherwise.
	 * 
	 */
	public void enable() {
		if (activePlayer == game.getCurrentIdx()) {
			playButton.setEnabled(true);
			passButton.setEnabled(true);
			bigTwoPanel.setEnabled(true);
			msgInputArea.setEnabled(true);
		}
		else {
			this.disable();
		}
		
		
	}

	@Override
	public void disable() {
		
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
		msgInputArea.setEnabled(false);
	}
	
	
	private class BigTwoPanel extends JPanel implements MouseListener{
		
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for a BigTwoPanel class
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		
		/**
		 * A method for drawing the components of the GUI
		 */
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
		
			//Drawing the board
			Board = new ImageIcon("avatars/Board.png").getImage();
			g.drawImage(Board, 0, 0 , 2000,2000, null);
			
			cardBackImage = new ImageIcon("images/gray_back.png").getImage();
			
			//Drawing the cards
			for (int i = 0; i < 4; i++) {
				//find the number of cards of the player
				int hand_cards_num = game.getPlayerList().get(i).getNumOfCards();
				
				
				if (gamePlayers[i]) {
					if (i == activePlayer) {
						g.setColor(Color.BLUE);
					}
					if (game.getCurrentIdx() == i) {
						if (gamePlayers[0] && gamePlayers[1] && gamePlayers[2] && gamePlayers[3]) {
							g.setColor(Color.GREEN);
						}
						
					}
					
					if (activePlayer == i) {
						Font font = new Font("Serif", Font.BOLD, 20);
					    g.setFont(font);
						g.drawString("You", 40, (i * 180 +  17));
					}
					else {
						Font font = new Font("Serif", Font.BOLD, 20);
					    g.setFont(font);
						g.drawString(game.getPlayerList().get(i).getName(), 40, (i * 180 +  17));
					}
					
					//Draw player avatar
					g.drawImage(avatars[i], 10, (i * 180 +  20), 130, 150, null);
					
					//if all four players are present
					if (gamePlayers[0] && gamePlayers[1] && gamePlayers[2] && gamePlayers[3]) {
						//if the player is active, show card faces
						//printMsg("Number of players is: " + game.getPlayerList().size());
						
						if (activePlayer == i) {
							
							for(int j = 0; j < hand_cards_num; j++) {
								//if card not selected, show in normal position
								if (!selected[j]) {
									int card_i = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
									int card_j = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
									g.drawImage(cardImages[card_i][card_j], imageWidth + 20 + 25 * j, 20 + (imageWidth +30) * i, imageWidth, imageHeight, null);
									
								}
								//otherwise, show the card in the elevated position
								else {
									int card_i = game.getPlayerList().get(i).getCardsInHand().getCard(j).getSuit();
									int card_j = game.getPlayerList().get(i).getCardsInHand().getCard(j).getRank();
									g.drawImage(cardImages[card_i][card_j], imageWidth + 20 + 25 * j, 20 + (imageWidth +30) * i - 15, imageWidth, imageHeight, null);
									
								}
								
							}
						}
						//if player is inactive, show the back of the cards
						else {
							for(int j = 0; j < hand_cards_num; j++) {
								g.drawImage(cardBackImage, imageWidth + 20 + 25 * j, 20 + (imageHeight + 30 )* i, imageWidth, imageHeight, null);
								
							}
						}
					}
					
					
					
				
				}
							
				

			    //Draw Ending line
				g.setColor(Color.black);
				g.fillRect(0, (i * 180 +  175), 2000, 2);
				
				
				
				
			}
			
			//Show the last hand on table
			if (!game.getHandsOnTable().isEmpty()){
				
					Hand lastHandOnTable = game.getHandsOnTable().get(game.getHandsOnTable().size()-1);
			    	
						//find the suit and rank of each card and show the card
			    		for (int i = 0; i < lastHandOnTable.size(); i++)
			    		{	
			    			int card_i = lastHandOnTable.getCard(i).getSuit();
			    			int card_j = lastHandOnTable.getCard(i).getRank();
			    			
			    			g.drawImage(cardImages[card_i][card_j], 20 + 25 * i , 742, imageWidth, imageHeight, null);
			    		}
			    		
			    		//Text to show the player of the last hand
			    		g.drawString("Played by " + game.getHandsOnTable().get(game.getHandsOnTable().size()-1).getPlayer().getName(), 10, 735);
			    		
			    	
		    }
				
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			
			//Get coordinates of the clicked point
			int mouseX = e.getX();
			int mouseY = e.getY();

			//for each player
			for (int i = 0; i < 4; i++) {
				//only check the clicks for the active player
				if (i == activePlayer) {
					int start = game.getPlayerList().get(i).getNumOfCards();
					//start the loop from the end to maintain the selected order
					for (int j = (start - 1); j >= 0; j--) {
						
						//if the card is not selected, check normal position for mouseClick
						if (!selected[j]) {
							if (mouseX >= (imageWidth + 20 + 25 * j) && mouseX <= ((imageWidth + 20 + 25 * j) + imageWidth)) {
								
								if (mouseY >= (20 + (imageHeight +30) * i) && mouseY <= ((20 + (imageHeight +30) * i) + imageHeight)) {
									//if condition satisfied, change the boolean value of selected
									selected[j] = !selected[j];
									frame.repaint();
									break;
								}
								
							}
						}
						//if card is selected, check elevated position for mouseClick
						else {
							if (mouseX >= (imageWidth + 20 + 25 * j) && mouseX <= ((imageWidth + 20 + 25 * j) + imageWidth)) {
							
								if (mouseY >= (20 + (imageHeight +30) * i - 15) && mouseY <= ((20 + (imageHeight +30) * i - 15) + imageHeight)) {
									//if condition satisfied, change the boolean value of selected
									selected[j] = !selected[j];
									frame.repaint();
									break;
								}
								
							}
						}
						
					}
				}
				
				
	
			}
			
			
		}
		
		//Methods that do not need any implementation
		@Override
		public void mouseEntered(MouseEvent arg0) {}

		@Override
		public void mouseExited(MouseEvent arg0) {}

		@Override
		public void mousePressed(MouseEvent arg0) {}

		@Override
		public void mouseReleased(MouseEvent arg0) {}
		
	}
	
	//calls the makeMove to allow the player to play the selected cards
	private class PlayButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			
			if (getSelected() == null) {
				printMsg(game.getPlayerList().get(activePlayer).getName() + "'s turn: ");
				printMsg("Not a legal move. No cards have been selected.");
				
			}
			else {
				game.makeMove(activePlayer, getSelected());
				
				//reset the selected array
				resetSelected();
				
				repaint();
			}
		}
		
	}
	
	
	//calls the makeMove to allow the player to play the selected cards
	private class PassButtonListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, null);
			
			//reset the selected array
			resetSelected();	
			
			repaint();
			
		}
		
	}
	
	//restarts the game by starting game with a new shuffled deck
	private class ConnectMenuItemListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			//check whether the player is already connected
			if (game.getPlayerID() >= 0 && game.getPlayerID() < 4) {
				printMsg("The player is already connected!");
			}
			//if the player is not connected, make the connection
			else if (game.getPlayerID() == -1) {
				game.makeConnection();
			}
			
		}
		
	}
	
	//quits the GUI and game
	private class QuitMenuItemListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent arg0) {
			//quit the game by calling quitGame()
			quitGame();
			
		}
		
	}
	
	//quits the GUI and game
	private void quitGame() {
		System.exit(0);
	}
	
	private class eraseGameMessagesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			clearMsgArea();
			
		}
		
	}
	
	private class eraseChatMessagesListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			
			msgArea2.setText("Chat Messages\n");
			
		}
		
	}
	
	/**
	 * A method to append a given chat message to the chat message Area
	 * 
	 * @param chatMessage a chat message written in the JTextField
	 */
	public void printChatMessage (String chatMessage) {
		msgArea2.append(chatMessage + "\n");
	}
	
	/**
	 * A method to create a pop-up window showing the messages to the user at the end of the game
	 */
	public void endGameMessage() {
		
		String final_message = "Game Ends!\n";
		
		for(int i = 0; i < game.getPlayerList().size(); i++){	
			//message for the winner
			if(game.getPlayerList().get(i).getCardsInHand().size() == 0){
				final_message += game.getPlayerList().get(i).getName() + " wins the game\n";
			}
			//message for the losers
			else{
				final_message += game.getPlayerList().get(i).getName() + " has " + game.getPlayerList().get(i).getCardsInHand().size() + " cards in hand\n"; 
				
			}
		}
		
		//remove all players
		for (int i = 0; i < 4; i++) {
			removePlayer(i);
		}
		//empty the hands on table
		game.clearHandsOnTable();
		
		repaint();
		
		 JOptionPane.showMessageDialog(null, final_message);
		  
		  
	}


}
