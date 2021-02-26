/**
 * This class is a subclass of Hand and is used to represent a hand of Fullhouse
 * 
 * @author Nazim Shoikot
 *
 */
public class FullHouse extends Hand {
	/**
	 * constructor for building a hand of Fullhouse
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */
	public FullHouse(CardGamePlayer player, CardList cards) {
		super(player,cards);
	}
	
	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "FullHouse";
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			//sort the cards
			Hand hand = this;
			hand.sort();
			//look for 3 same cards
			for (int i =0; i <= hand.size() - 3; i++){
				if ((hand.getCard(i).getRank() == hand.getCard(i+1).getRank()) && (hand.getCard(i).getRank() == hand.getCard(i+2).getRank())){
					//then look for 2 cards
					for (int j=0; j <= hand.size()-2; j++) {
						if ((hand.getCard(j).getRank() == hand.getCard(j+1).getRank()) && (hand.getCard(j).getRank() != hand.getCard(i).getRank())) {
							return true;
						}
					}
				}
			}
		}
		return false;
		
	}
	
	/**
	 * A method for checking if this hand beats a specified hand
	 * 
	 * @param hand a specified hand that needs to be compared
	 * 
	 * @return True if this hand beats the specified hand, False otherwise
	 */
	public boolean beats (Hand hand) {
		
		if (this.size() == hand.size()) {						
					
				if (this.getType() == hand.getType()) {
					this.sort();
					//getting the card of the triplet
					if (this.getCard(2).compareTo(hand.getCard(2)) == 1){
						return true;
					} else {
						return false;
					}
				}
				//if any of the below hands, it should return false.
				else if (hand.getType() == "StraightFlush" || hand.getType() == "Quad") {
					return false;
				}
				else if (hand.getType() == "Flush" || hand.getType() == "Straight") {
					return true;
				}
			
			
		} //end of if for this.size == hand.size
		return false;
		
	}
	
}
