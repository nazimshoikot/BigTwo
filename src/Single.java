/**
 * This class is a subclass of Hand and is used to represent a hand of single
 * 
 * @author Nazim Shoikot
 *
 */
public class Single extends Hand {

	/**
	 * constructor for building a hand of pair 
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
		
	}

	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "Single";
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size() == 1) {
			return true;
		}
		return false;
		
	}

}
