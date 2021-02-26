import java.util.ArrayList;

/**
 * This is a subclass of the CardList class and is used to model a hand of cards
 * 
 * @author Nazim Shoikot
 *
 */
public abstract class Hand extends CardList {
	/**
	 * A constructor for building a hand with specified player and list of cards 
	 * (although instances of abstract classes cannot be created)
	 * 
	 * @param player the player who plays this hand
	 * @param cards list of cards of this player
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player; 
		
		
		for (int i = 0; i < cards.size(); i++) {
			this.addCard(cards.getCard(i));
		}
		
		
	}
	
	private CardGamePlayer player;
	
	/**
	 * A method for retrieving the player of this hand
	 * 
	 * @return the player of this hand
	 */
	public CardGamePlayer getPlayer() {
		return this.player;
		}
	
	/**
	 * A method for retrieving the top card of this hand
	 * 
	 * @return the top card of the hand
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	
	/**
	 * A method for checking if this hand beats a specified hand
	 * 
	 * @param hand a specified hand that needs to be compared
	 * 
	 * @return True if this hand beats the specified hand, False otherwise
	 */
	public boolean beats (Hand hand) {
		//size has to be equal
		if (this.size() == hand.size()) {
				if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
					return true;
				} else {
					return false;
				}
			//if size is 5, it is handled by the overridden method in the subclasses
		} //end of if for this.size == hand.size
		return false;
		
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return True if the hand is valid, False otherwise
	 */
	public abstract boolean isValid();
	
	/**
	 * A methood for returning a string specifying the type of this hand
	 * 
	 * @return a string that specifies the type of this hand
	 */
	public abstract String getType();
	
	
	
	
}
