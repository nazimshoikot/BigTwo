import java.util.ArrayList;
/**
 * This class is used to model a big Two card game.
 * 
 * @author Nazim Shoikot
 *
 */
public class BigTwo implements CardGame {
	/**
	 * A constructor for creating a Big Two card Game.
	 */
	public BigTwo() {
		
		playerList = new ArrayList<CardGamePlayer>();
		handsOnTable = new ArrayList<Hand>();
		
		
		CardGamePlayer player_1 = new CardGamePlayer();
		CardGamePlayer player_2 = new CardGamePlayer();
		CardGamePlayer player_3 = new CardGamePlayer();
		CardGamePlayer player_4 = new CardGamePlayer();
		
		playerList.add(player_1);
		playerList.add(player_2);
		playerList.add(player_3);
		playerList.add(player_4);
		
		bigTwoTable = new BigTwoTable(this);
		
		
	}
	
	//declaring the private variables
	private Deck deck;
	private ArrayList<CardGamePlayer> playerList;
	private ArrayList<Hand> handsOnTable;
	private int currentIdx;
	private BigTwoTable bigTwoTable;
	
	/**
	 * A method for retrieving the deck of cards being used
	 * 
	 * @return deck object representing the deck of cards being used
	 */
	public Deck getDeck() {
		return this.deck;
	}
	
	/**
	 * A method for retrieving the list of players
	 * 
	 * @return a ArrayList<CardGamePlayer> object representing the list of players
	 */
	public ArrayList<CardGamePlayer> getPlayerList() {
		return this.playerList;
	}
	
	/**
	 * A method for retrieving the list of hands played on the table
	 * 
	 * @return a ArrayList<Hand> object representing the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable() {
		return this.handsOnTable;
	}
	
	/**
	 * A method for retrieving the index of the current player
	 * 
	 * @return int representing the index of the current player
	 */
	public int getCurrentIdx() {
		return this.currentIdx;
	}
	
	
	
	/**
	 * A method for starting the game with a shuffled deck of cards supplied as the argument
	 * 
	 * @param deck Deck object representing a shuffled deck of cards
	 */
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
				bigTwoTable.setActivePlayer(i);
				currentIdx = i;
			}
		}
		
		bigTwoTable.repaint();
		
			
		
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

	@Override
	public int getNumOfPlayers() {
		return getPlayerList().size();
	}



	@Override
	public void makeMove(int playerID, int[] cardIdx) {
		//calls the checkMove function to make the move
		checkMove(playerID, cardIdx);
		
	}
	
	@Override
	public void checkMove(int playerID, int[] cardIdx) {
		
		int prev_p;
		
		boolean input_validity = true; //using this to check whether a given hand is valid or not
		
		CardList cardlist = new CardList();
	
		
		
		
		//find player of last hand on table on the next turn
		if (handsOnTable.isEmpty()) {
			prev_p = -1;
		}
		else {
			Hand lastHand = handsOnTable.get(handsOnTable.size()-1);
			String name_of_last_player = lastHand.getPlayer().getName();
			
			if (name_of_last_player.equals("Player 0")) {
				prev_p = 0;
			}
			else if (name_of_last_player.equals("Player 1")) {
				prev_p = 1;
			}
			else if (name_of_last_player.equals("Player 2")) {
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
				bigTwoTable.printMsg("Player " + playerID  + "'s turn: ");
				bigTwoTable.printMsg("{" + hand.getType() + "}" + " " + hand);
				handsOnTable.add(hand);
				
				//change the player
				playerID = (playerID + 1) % 4;
				bigTwoTable.setActivePlayer(playerID);
			}
			//if not a valid move
			else{
				//if handsdOnTable is not empty
				if (!handsOnTable.isEmpty()) {
					if (hand != null) {
						bigTwoTable.printMsg("Player " + playerID  + "'s turn: ");
						bigTwoTable.printMsg("{" + hand.getType() + "}" + " " + hand + " <== Not a legal move");
					}
					else {
						bigTwoTable.printMsg("Not a legal move. The cards selected do not form a valid hand");
					}
					
				}
				//if handsOnTable is empty
				else {
					if (hand != null) {
						bigTwoTable.printMsg("Player " + playerID  + "'s turn: ");
						bigTwoTable.printMsg("Not a legal move. The first hand must contain 3 of Diamonds");
					}
					else {
						bigTwoTable.printMsg("Player " + playerID  + "'s turn: ");
						bigTwoTable.printMsg("Not a legal move. The cards selected do not form a valid hand");
					}
					
				}
				
			}
			
		}
		
		//if player tries to pass
		else{	
			if(!handsOnTable.isEmpty() && prev_p != playerID) {
				
				bigTwoTable.printMsg("Player " + playerID + "'s turn");
				bigTwoTable.printMsg("{Pass}");  
				
				playerID = (playerID + 1) % 4;
				
				bigTwoTable.setActivePlayer(playerID);
				
				input_validity = true;
			}
			//if player is the player of last hand or it is the first turn
			else{
				bigTwoTable.printMsg("Player " + playerID + "'s turn");
				bigTwoTable.printMsg("Not a legal move. The player who played the last hand cannot pass the round");
			} 
		}
		
	//when one of the players have zero cards
	if(endOfGame()){
		bigTwoTable.setActivePlayer(-1);
		
		for(int i = 0; i < playerList.size();i++){	
			//message for the winner
			if(playerList.get(i).getCardsInHand().size() == 0){
				bigTwoTable.printMsg("Player " + i + " wins the game"); // declare that player as winner
			}
			//message for the losers
			else{
				bigTwoTable.printMsg("Player " + i + " has " + playerList.get(i).getCardsInHand().size() + " cards in hand"); 
				
			}
		}
		
		bigTwoTable.repaint();
		
		bigTwoTable.printMsg("");
		bigTwoTable.printMsg("Game ends");
		
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

	
	
//	/**
//	 * A method for to begin the game
//	 * 
//	 * @param args an array of command-line arguments for the application
//	 */
//	public static void main(String [] args)
//	{
//		BigTwo new_game = new BigTwo();
//		BigTwoDeck new_deck = new BigTwoDeck();
//		new_deck.shuffle(); 
//		new_game.start(new_deck);
//	}

	
	
	

}