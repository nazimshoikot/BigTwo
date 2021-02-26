/**
 * This class is a subclass of Hand and is used to represent a hand of Quad
 * 
 * @author Nazim Shoikot
 *
 */
public class Quad extends Hand{
	/**
	 * constructor for building a hand of Quad 
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */

	public Quad (CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "Quad";
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			Hand hand = this;
		
			hand.sort();
			
			for (int i=0; i <= hand.size()-4; i++ ) {
				if ((hand.getCard(i).getRank() == hand.getCard(i+1).getRank()) && (hand.getCard(i).getRank() == hand.getCard(i+2).getRank()) && (hand.getCard(i).getRank() == hand.getCard(i+3).getRank())){
					return true;
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
					if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
						return true;
					} else {
						return false;
					}
				}
				//if any of the below hands, it should return false.
				else if (hand.getType() == "StraightFlush") {
					return false;
				}
				else if (hand.getType() == "FullHouse" || hand.getType() == "Flush" || hand.getType() == "Straight") {
					return true;
				}
			
			
		} //end of if for this.size == hand.size
		return false;
		
	}
	
}
