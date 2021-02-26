/**
 * This class is a subclass of Hand and is used to represent a hand of Flush
 * 
 * @author Nazim Shoikot
 *
 */
public class Flush extends Hand {
	/**
	 * constructor for building a hand of Flush
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */
	public Flush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "Flush";
	}
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid() {
		if (this.size() == 5) {
			int S_count = 0, H_count = 0, C_count = 0, D_count = 0;
			
			for (int i = 0; i < this.size(); i++) {
				if (this.getCard(i).getSuit() == 0) {
					D_count++;
				}
				else if (this.getCard(i).getSuit() == 1) {
					C_count++;
				}
				else if (this.getCard(i).getSuit() == 2) {
					H_count++;
				}
				else if (this.getCard(i).getSuit() == 3) {
					S_count++;
				}
			}
			if (S_count == 5 || H_count == 5 || C_count == 5 || D_count == 5) {
				return true;
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
	public boolean beats(Hand hand) {
		if (this.size() == hand.size()) {
			if (this.getType() == hand.getType()) {
				if (this.getTopCard().getSuit() != hand.getTopCard().getSuit()) {
					if ((this.getTopCard().getSuit() > hand.getTopCard().getSuit())){
						return true;
					}
					else {
						return false;
					}
				}
				else {
					if (this.getTopCard().compareTo(hand.getTopCard()) == 1){
						return true;
					} 
					else {
						return false;
					}
				}
			}
			//if any of the below hands, return false
			else if (hand.getType() == "StraightFlush" || hand.getType() == "Quad" || hand.getType() == "FullHouse") {
				return false;
			}
			else if (hand.getType() == "Straight") {
				return true;
			}
		}
		return false;
		
		
	}
	
	

}
