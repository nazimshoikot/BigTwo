import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JOptionPane;

/**
 * The BigTwoClient class implements the CardGame interface and NetworkGame
 * interface. It is used to model a Big Two card game that supports 4 
 * players playing over the internet
 * 
 * @author Nazim Shoikot
 *
 */
public class BigTwoClient implements CardGame, NetworkGame {
		
	/**
	 * A constructor for the BigTwoClient class
	 */
	public BigTwoClient() {
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		playerID = -1;
		
		CardGamePlayer player_1 = new CardGamePlayer();
		CardGamePlayer player_2 = new CardGamePlayer();
		CardGamePlayer player_3 = new CardGamePlayer();
		CardGamePlayer player_4 = new CardGamePlayer();
		
		playerList.add(player_1);
		playerList.add(player_2);
		playerList.add(player_3);
		playerList.add(player_4);
		
		bigTwoTable = new BigTwoTable(this);
		
		bigTwoTable.disable();
		
		playerName = (String) JOptionPane.showInputDialog("Please enter your name: ");
		
		if (playerName.trim().isEmpty() || playerName == null) {
			playerName = "Anonymous";
		}
		
		makeConnection();
		bigTwoTable.repaint();
	}
	
	//private instance variable
	private int numOfPlayers; //an integer specifying the number of players
	private Deck deck; //a deck of cards
	private ArrayList<CardGamePlayer> playerList; //a list of players
	private ArrayList<Hand> handsOnTable; //a list of hands played on the table
	private int playerID; // an integer specifying the playerID (index) of the local player
	private String playerName; //a string specifying the name of the local player
	private String serverIP; //a string specifying the IP address of the game server
	private int serverPort; // an integer specifying the TCP port of the game server
	private Socket sock; // a socket connection to the game server
	private ObjectOutputStream oos; // an ObjectOutputStream for sending messages to the server
	private ObjectInputStream ois; //an ObjectInputStream for receiving messages from the server
	private int currentIdx; //an integer specifying the index of the player for the current turn
	private BigTwoTable bigTwoTable; // a Big Two table which builds the GUI for the game and handles all the user actions
	
	
	
	
	
	
	//Methods of the class to be implemented
	
	//Methods of NetworkGame interface
	@Override
	public int getPlayerID() {
		return this.playerID;
	}
	@Override
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
		
	}
	@Override
	public String getPlayerName() {
		return this.playerName;
	}
	@Override
	public void setPlayerName(String playerName) {
		this.playerName = playerName;
		
	}
	@Override
	public String getServerIP() {
		return this.serverIP;
	}
	@Override
	public void setServerIP(String serverIP) {
		this.serverIP = serverIP;	
	}
	@Override
	public int getServerPort() {
		return this.serverPort;
	}
	@Override
	public void setServerPort(int serverPort) {
		this.serverPort = serverPort;
	}
	
	@Override
	public void makeConnection() {
		serverIP = "127.0.0.1";
		serverPort = 2396;
		
		
		try {
			sock = new Socket(serverIP, serverPort);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//upon successful connection, create an ObjectOutputStraem for sending messages to the game server
		try {
			oos = new ObjectOutputStream(sock.getOutputStream());
			ois = new ObjectInputStream(sock.getInputStream());
			System.out.println("Connection established");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		//create a thread for receiving messages from the game server
		Runnable threadJob = new ServerHandler();
		Thread inputThread = new Thread(threadJob);
		inputThread.start();
		
		//send a message of the type JOIN to the game server
		CardGameMessage messageJOIN = new CardGameMessage(1, -1, this.getPlayerName());
		sendMessage(messageJOIN);
		
		//send a message of the type READY to the game server
		CardGameMessage messageREADY = new CardGameMessage(4, -1, null);
		sendMessage(messageREADY);
	
	}
	
	@Override
	public void parseMessage(GameMessage message) {
		
		if (message.getType() == CardGameMessage.PLAYER_LIST) {
			this.playerID = message.getPlayerID();
			
			bigTwoTable.setActivePlayer(playerID);
			
			for (int i = 0;  i < 4; i++) {
				if (((String[])message.getData())[i] != null) {
					this.playerList.get(i).setName(((String[])message.getData())[i]);
					bigTwoTable.joinPlayer(i);
				}
			}
			
			bigTwoTable.repaint();
		}
		
		else if (message.getType() == CardGameMessage.JOIN) {
			//set the name of the appropriate player to the given name
			int joinIndex = message.getPlayerID();
			String joinName = (String) message.getData();
			this.playerList.get(joinIndex).setName(joinName);
			
			bigTwoTable.joinPlayer(joinIndex);
			bigTwoTable.printMsg(joinName + " joined the game!");
			bigTwoTable.repaint();
		}
		
		else if (message.getType() == CardGameMessage.FULL) {
			this.playerID = -1;
			bigTwoTable.printMsg("The server is full. Player cannot join the game.");
			bigTwoTable.repaint();
			
		}
		
		else if (message.getType() == CardGameMessage.QUIT) {
			//set the name of the player to a empty string
			int leaveIndex = message.getPlayerID();
			bigTwoTable.printMsg(playerList.get(leaveIndex).getName() + " has left the game.");
			
			//set name to empty string
			this.playerList.get(leaveIndex).setName("");
			
			//remove the player from the gamePlayers array of bigTwoTable
			bigTwoTable.removePlayer(leaveIndex);
		
			if (!this.endOfGame()) {
				//stop the game
				
				//prevent any user actions
				bigTwoTable.disable();
				
				//remove all cards of the players to stop the game
				for (int i = 0; i < 4; i++) {
					this.playerList.get(i).removeAllCards();
				}
				
				//clear the handsOnTable
				this.clearHandsOnTable();
				
				//send a message of type READY
				CardGameMessage messageREADY = new CardGameMessage(4, -1, null);
				sendMessage(messageREADY);
				
				
			}
			
			
			bigTwoTable.repaint();
		}
		
		else if (message.getType() == CardGameMessage.READY) {
			//print the message
			String playerName = this.playerList.get(message.getPlayerID()).getName();
			
			//add the player in the gamePlayers array of bigTwoTable
			bigTwoTable.joinPlayer(message.getPlayerID());
			
			//print the message to the message area
			bigTwoTable.printMsg(playerName + " is ready!");
			
			//clear the handsOnTable
			this.clearHandsOnTable();
			
			
			bigTwoTable.repaint();
		}
		
		else if (message.getType() == CardGameMessage.START) {
			//start a game with the shuffled deck of cards
			BigTwoDeck newDeck = (BigTwoDeck) message.getData();
			this.start(newDeck);
			
			bigTwoTable.printMsg("Game has started. Good Luck!\n");
			
			bigTwoTable.enable();
			
			bigTwoTable.repaint();
		}
		
		else if (message.getType() == CardGameMessage.MOVE) {
			int movePlayer = message.getPlayerID();
			int[] selCards = (int[]) message.getData();
			this.checkMove(movePlayer, selCards);
		}
		
		else if (message.getType() == CardGameMessage.MSG) {
			String msg = (String) message.getData();
			bigTwoTable.printChatMessage(msg);
		}
		
		else {
			bigTwoTable.printMsg("Message not recognized");
			
		}
		bigTwoTable.repaint();
	}
	
	@Override
	public void sendMessage(GameMessage message) {
		try {
			oos.writeObject(message);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	
	//Methods of cardGame interface
	@Override
	public int getNumOfPlayers() {
		return this.numOfPlayers;
	}
	@Override
	public Deck getDeck() {
		return this.deck;
	}
	@Override
	public ArrayList<CardGamePlayer> getPlayerList() {
		return playerList;
	}
	@Override
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
	
	@Override
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	
	/**
	 * A method to remove all the cards from the hands on table
	 */
	public void clearHandsOnTable() {
		this.handsOnTable.clear();
	}
	
	@Override
	public void start(Deck deck)
	{	
		int card_flag = 0;
		
		//empty the hands on table
		handsOnTable.clear();
		
		//empty hands of every player
		for (int i = 0; i < 4; i++) {
			playerList.get(i).removeAllCards();
		}
		
		//add cards to every player
		for(int i = 0; i < 4; i++){
			for(int j = 0; j < 13; j++){
				playerList.get(i).addCard(deck.getCard(card_flag+i+j));
			}
			card_flag += 12;
		}
		
		
		//sort hands of every player
		for(int i = 0; i < 4;i++){
			playerList.get(i).getCardsInHand().sort();
		}
		
		
		
		
		//finding player with 3 of diamonds and setting him as current player
		for(int i = 0; i < 4;i++) {
			if(playerList.get(i).getCardsInHand().contains(new Card(0,2))){
				currentIdx = i;
			}
		}
		
		bigTwoTable.setActivePlayer(playerID);
		bigTwoTable.repaint();			
	}
	
	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		CardGameMessage cardGameMessage = new CardGameMessage(6, -1, cardIdx);
		sendMessage(cardGameMessage);
		
	}
	
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		
		int prev_p;
		
		//using this to check whether a given hand is valid or not
		boolean input_validity = true; 
		
		CardList cardlist = new CardList();
	
		//find player of last hand on table on the next turn
		if (handsOnTable.isEmpty()) {
			prev_p = -1;
		}
		else {
			Hand lastHand = handsOnTable.get(handsOnTable.size()-1);
			String name_of_last_player = lastHand.getPlayer().getName();
			
			if (name_of_last_player.equals(playerList.get(0).getName())) {
				prev_p = 0;
			}
			else if (name_of_last_player.equals(playerList.get(1).getName())) {
				prev_p = 1;
			}
			else if (name_of_last_player.equals(playerList.get(2).getName())) {
				prev_p = 2;
			}
			else {
				prev_p = 3;
			}
		}
		
		
		if(cardIdx != null){
			cardlist = playerList.get(playerID).play(cardIdx);
			
			Hand hand = composeHand(playerList.get(playerID), cardlist);				
			
			//checking if the played hand has 3 of diamonds
			if(handsOnTable.isEmpty()){
				
				if (hand == null) {
					input_validity = false;
				}
				else if(hand.contains(new Card(0,2)) && !hand.isEmpty()){
					input_validity = true; 
				}
				else{
					input_validity = false;
				}
			}
			//if board is not empty
			else{	
				
				if (hand == null) {
				input_validity = false;
				}
				else if(prev_p != playerID) {
					//check if the hand beats the hand on table
					input_validity = hand.beats(handsOnTable.get(handsOnTable.size() - 1));
				}
				else {
					input_validity = true;
				}
					
			}
			//if hand beats the hand on table
			if(input_validity){
				prev_p = playerID;
				
				for(int i = 0; i < cardlist.size();i++){
					//remove cards
					playerList.get(playerID).getCardsInHand().removeCard(cardlist.getCard(i)); 

				}
				
				//print out the played hand
				bigTwoTable.printMsg(playerList.get(playerID).getName()  + "'s turn: ");
				bigTwoTable.printMsg("{" + hand.getType() + "}" + " " + hand);
				handsOnTable.add(hand);
				
				//change the player
				currentIdx = (currentIdx + 1) % 4;
			}
			
			//if not a valid move
			else{
				//if handsdOnTable is not empty
				if (!handsOnTable.isEmpty()) {
					if (hand != null) {
						bigTwoTable.printMsg(playerList.get(playerID).getName()  + "'s turn: ");
						bigTwoTable.printMsg("{" + hand.getType() + "}" + " " + hand + " <== Not a legal move");
					}
					else {
						bigTwoTable.printMsg(playerList.get(playerID).getName()  + "'s turn: ");
						bigTwoTable.printMsg("Not a legal move. The cards selected do not form a valid hand");
					}
					
				}
				//if handsOnTable is empty
				else {
					if (hand != null) {
						bigTwoTable.printMsg(playerList.get(playerID).getName()  + "'s turn: ");
						bigTwoTable.printMsg("Not a legal move. The first hand must contain 3 of Diamonds");
					}
					else {
						bigTwoTable.printMsg(playerList.get(playerID).getName() + "'s turn: ");
						bigTwoTable.printMsg("Not a legal move. The cards selected do not form a valid hand");
					}
					
				}
				
			}
			
		}
		
		//if player tries to pass
		else{	
			if(!handsOnTable.isEmpty() && prev_p != playerID) {
				
				bigTwoTable.printMsg(playerList.get(playerID).getName() + "'s turn");
				bigTwoTable.printMsg("{Pass}");  
				
				currentIdx = (currentIdx + 1) % 4;
				
				input_validity = true;
			}
			//if player is the player of last hand or it is the first turn
			else{
				bigTwoTable.printMsg(playerList.get(playerID).getName() + "'s turn");
				bigTwoTable.printMsg("Not a legal move. The player cannot pass this round");
			} 
		}
		
		//enable or disable the buttons depending on the currentIdx. 
		//logic implemented in the enable method of BigTwoTable
		bigTwoTable.enable();
		
		//when one of the players have zero cards
		if(endOfGame()){
			//show the dialog
			bigTwoTable.endGameMessage();
			
			//when the dialog is closed by pressing OK
			CardGameMessage message = new CardGameMessage(4, -1, null);
			sendMessage(message);
			
			bigTwoTable.repaint();
	}
		
	}
	
	@Override
	public boolean endOfGame() {
		
		for (int i = 0; i < playerList.size(); i++) {
			if (playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/*
	 * A method for returning a valid hand from the specified list of cards of the player.
	 */
	private static Hand composeHand(CardGamePlayer player, CardList cards) {
		//Declaring new objects of all hands 
		StraightFlush straightFlush = new StraightFlush(player,cards);
		
		Quad quad = new Quad(player,cards);
		
		FullHouse fullHouse = new FullHouse(player,cards);
		
		Flush flush = new Flush(player,cards);
		
		Straight straight = new Straight(player,cards);
		
		Triple triple = new Triple(player,cards);
		
		Pair pair = new Pair(player,cards);
		
		Single single = new Single(player,cards);
		
		//checking for validity of hands in descending order of ranking
		if(straightFlush.isValid()){
			return straightFlush;
		}
		
		if(quad.isValid()){
			return quad; 
		}
		
		if(fullHouse.isValid()){
			return fullHouse;
		}
		
		if(flush.isValid()){
			return flush;
		}
		
		if(straight.isValid()){	
			return straight;
		}
		
		if(triple.isValid()){
			return triple; 
		}
		
		if(pair.isValid()){
			return pair; 
		}
		if (single.isValid()) {
			return single;	
		}
		
		return null;

		
	}
	
	/**
	 * A method for to begin the game
	 * 
	 * @param args an array of command-line arguments for the application
	 */
	public static void main(String [] args)
	{
		BigTwoClient new_game = new BigTwoClient();
		BigTwoDeck new_deck = new BigTwoDeck();
		new_deck.shuffle(); 
		new_game.start(new_deck);
	}
	
	//inner class ServerHandler
	class ServerHandler implements Runnable{	
		public void run() {
			CardGameMessage message = null;
			
			try{
				while ((message = (CardGameMessage) ois.readObject()) != null){
					parseMessage(message);
				}
			} catch (Exception exception) {
				exception.printStackTrace();
			}
			
			bigTwoTable.repaint();
		}
	}
	
}
