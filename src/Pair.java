/**
 * This class is a subclass of Hand and is used to represent a hand of Pair
 * 
 * @author Nazim Shoikot
 *
 */
public class Pair extends Hand {
	/**
	 * constructor for building a hand of pair 
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
		
	}

	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "Pair";
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size() == 2) {
			if (this.getCard(0).getRank() == this.getCard(1).getRank()) {
				return true;
			}		
					
		}//end of if
		
		return false;
	}
	
}
