/**
 * This class is used to model a card used in Big Two card game
 * @author Nazim Shoikot
 *
 */
public class BigTwoCard extends Card {
	/**
	 * A constructor for building a card with specified suit and rank
	 * 
	 * @param suit an integer between 0 and 3
	 * @param rank an integer between 0 and 12
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit,rank);
	}
	
	
	/**
	 * A method for comparing the order of this card with the specified card
	 * 
	 * @param card the specified card for comparison
	 * 
	 * @return A negative integer, zero or a positive integer as this card is less than, equal to,
	 * or greater than the specified card
	 */
	public int compareTo(Card card) {
		
		if (this.rank >= 2 && card.rank >= 2) {
			if (this.rank > card.rank) {
				return 1;
			} else if (this.rank < card.rank) {
				return -1;
			} else if (this.suit > card.suit) {
				return 1;
			} else if (this.suit < card.suit) {
				return -1;
			} else {
				return 0;
			}
		} else {//special ranking of BigTwo handling
			int new_this_rank;
			int new_card_rank;
			
			//setting a new this.rank variable for comparison
			if (this.rank == 0 || this.rank ==1) {
				new_this_rank = this.rank + 13;
			} else {
				new_this_rank = this.rank;
			}
			
			//setting a new card.rank variable for comparison
			if (card.rank == 0 || card.rank == 1){
				new_card_rank = card.rank + 13;
			} else {
				new_card_rank = card.rank;
			}
			
			//comparison of new ranks
			if (new_this_rank > new_card_rank) {
				return 1;
			} else if (new_this_rank < new_card_rank) {
				return -1;
			} else if (this.suit > card.suit) {
				return 1;
			} else if (this.suit < card.suit) {
				return -1;
			} else {
				return 0;
			}
				
		}
		
	}
	
	
}
