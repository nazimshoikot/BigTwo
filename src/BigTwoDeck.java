/**
 * This cass is a subclass of the Deck class and is used to model 
 * a deck of cards used in a Big Two card game
 * 
 * @author Nazim Shoikot
 *
 */
public class BigTwoDeck extends Deck {
	public void initialize() {
		removeAllCards();
		for (int i = 0; i < 4; i++) {
			for (int j = 0; j < 13; j++) {
				BigTwoCard b_card = new BigTwoCard(i, j);
				this.addCard(b_card);
				}
			}
		
	}

}
