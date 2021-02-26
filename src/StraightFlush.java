import java.util.Arrays;

/**
 * This class is a subclass of Hand and is used to represent a hand of StraightFlush
 * 
 * @author Nazim Shoikot
 *
 */
public class StraightFlush extends Hand {
	/**
	 * constructor for building a hand of StraightFlush
	 * 
	 * @param player CardGamePlayer object representing the player who plays the hand
	 * @param cards CardList object representing the cards list of cards of this player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}
	
	/**
	 * A method for returning a string specifying the type of this hand
	 * 
	 * @return string value representing the type of this hand
	 */
	public String getType() {
		return "StraightFlush";
	}
	
	//overriding method
	/**
	 * A method for retrieving the top card of this hand
	 * 
	 * @return the top card of the hand
	 */
	public Card getTopCard(){
		int [] rank_list = {this.getCard(0).rank, this.getCard(1).rank, this.getCard(2).rank, this.getCard(3).rank, this.getCard(4).rank};
		int ind = 0;
		//maintaining rank
		for(int i = 0; i < 5;i++){
			if(rank_list[i] == 0 || rank_list[i] == 1){
				rank_list[i] += 13;
			}
			
		}
		Arrays.sort(rank_list);
		
		
		for(int i = 0; i < rank_list.length; i++){
			if(this.getCard(i).rank == rank_list[4]){
				ind = i;
			}
		}
		
		return this.getCard(ind);
		
	}
	
	
	/**
	 * A method for checking if this is a valid hand
	 * 
	 * @return boolean value - true if it is valid, false otherwise
	 */
	public boolean isValid(){
		if(this.size() == 5){
			
			int [] rank_list = {this.getCard(0).rank, this.getCard(1).rank, this.getCard(2).rank, this.getCard(3).rank, this.getCard(4).rank};
			
			//maintaining rank
			for(int i = 0; i < 5;i++){
				if(rank_list[i] == 0 || rank_list[i] == 1){
					rank_list[i] += 13;
				}
				
			}
			
			
			Arrays.sort(rank_list);
			
			
			boolean flag = false;
			
			for (int i = 1; i < rank_list.length; i++) {
				if(this.getCard(0).suit == this.getCard(1).suit && this.getCard(1).suit == this.getCard(2).suit && this.getCard(2).suit == this.getCard(3).suit && this.getCard(3).suit == this.getCard(4).suit){
					if (rank_list[i] == rank_list[i-1] + 1) {
						flag = true;
					}
					else{
						return false;
					}
				}
			}
			
			return flag;
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
				else {
					return true;
				}
			
		} //end of if for this.size == hand.size
		return false;
		
	}
}
