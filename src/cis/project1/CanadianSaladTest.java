package cis.project1;

import org.junit.jupiter.api.Test;
import java.util.LinkedList;
import org.junit.Assert;


public class CanadianSaladTest {
	
	public CanadianSaladModel game() {
		Player john = new Player("john");
		Player chris = new Player("chris");
		Player andrew = new Player("andrew");
		LinkedList<Player> newPlayers = new LinkedList<>();
		newPlayers.push(john);
		newPlayers.push(chris);
		newPlayers.push(andrew);
		CanadianSaladModel game = new CanadianSaladModel(newPlayers);
		john.getHand().clear();
		chris.getHand().clear();
		andrew.getHand().clear();
		andrew.getHand().add(new Card(Suit.Diamonds, Rank.Two));
		andrew.getHand().add(new Card(Suit.Diamonds, Rank.Five));
		andrew.getHand().add(new Card(Suit.Diamonds, Rank.Eight));
		andrew.getHand().add(new Card(Suit.Diamonds, Rank.Jack));
		andrew.getHand().add(new Card(Suit.Hearts, Rank.Two));
		andrew.getHand().add(new Card(Suit.Hearts, Rank.Five));
		andrew.getHand().add(new Card(Suit.Hearts, Rank.Eight));
		andrew.getHand().add(new Card(Suit.Hearts, Rank.Jack));
		andrew.getHand().add(new Card(Suit.Spades, Rank.Two));
		andrew.getHand().add(new Card(Suit.Spades, Rank.Five));
		chris.getHand().add(new Card(Suit.Diamonds, Rank.Three));
		chris.getHand().add(new Card(Suit.Diamonds, Rank.Six));
		chris.getHand().add(new Card(Suit.Diamonds, Rank.Nine));
		chris.getHand().add(new Card(Suit.Diamonds, Rank.Queen));
		chris.getHand().add(new Card(Suit.Hearts, Rank.Three));
		chris.getHand().add(new Card(Suit.Hearts, Rank.Six));
		chris.getHand().add(new Card(Suit.Hearts, Rank.Nine));
		chris.getHand().add(new Card(Suit.Hearts, Rank.Queen));
		chris.getHand().add(new Card(Suit.Hearts, Rank.Ace));
		chris.getHand().add(new Card(Suit.Clubs, Rank.Queen));
		john.getHand().add(new Card(Suit.Diamonds, Rank.Four));
		john.getHand().add(new Card(Suit.Diamonds, Rank.Seven));
		john.getHand().add(new Card(Suit.Diamonds, Rank.Ten));
		john.getHand().add(new Card(Suit.Diamonds, Rank.King));
		john.getHand().add(new Card(Suit.Hearts, Rank.Four));
		john.getHand().add(new Card(Suit.Hearts, Rank.Seven));
		john.getHand().add(new Card(Suit.Hearts, Rank.Ten));
		john.getHand().add(new Card(Suit.Hearts, Rank.King));
		john.getHand().add(new Card(Suit.Spades, Rank.Four));
		john.getHand().add(new Card(Suit.Spades, Rank.Seven));
		return game;
	}

	@Test
	public void deckOf4() {
		Deck four = new Deck(4);
		Assert.assertEquals(52, four.getDeck().size());
	}

	@Test
	public void deckOf3() {
		Deck three = new Deck(3);
		Assert.assertEquals(51, three.getDeck().size());
	}

	@Test
	public void deckOf5() {
		Deck five = new Deck(5);
		Assert.assertEquals(50, five.getDeck().size());
	}

	@Test
	public void deckOf6() {
		Deck six = new Deck(6);
		Assert.assertEquals(48, six.getDeck().size());
	}

	@Test
	public void testDeckCreation() {
		Deck test = new Deck(4);
		Assert.assertTrue(test.getDeck() instanceof LinkedList);
	}

	@Test
	public void empty() {
		Deck test = new Deck(4);
		while (test.getDeck().size() > 0)
			test.getDeck().remove(0);
		Assert.assertTrue(test.getDeck().isEmpty());
	}

	@Test
	public void testTaker() {
		Player john = new Player("john");
		Trick t = new Trick(john, new Card(Suit.Clubs, Rank.Ace));
		Assert.assertEquals(t.getTaker(), john);
	}

	@Test
	public void testAddAndConstructor() {
		Player boy = new Player("boy");
		Trick t = new Trick(boy, new Card(Suit.Clubs, Rank.Ace));
		Assert.assertTrue(t.getTrick().size() > 0);
	}

	@Test
	public void testTrickGetter() {
		Player boy = new Player("boy");
		Trick t = new Trick(boy, new Card(Suit.Clubs, Rank.Ace));
		Assert.assertTrue(t.getTrick() instanceof LinkedList<?>);
	}

	@Test
	public void testTrickToString() {
		Player boy = new Player("boy");
		Trick t = new Trick(boy, new Card(Suit.Clubs, Rank.Ace));
		String test = "Trick{trick=[Ace of Clubs\n], led=Ace of Clubs\n, losing=Ace of Clubs\n, taker=boy}";
		Assert.assertTrue(test.contentEquals(t.toString()));
	}

	@Test
	public void testDeckToString() {
		Deck test = new Deck(4);
		while (test.getDeck().size() > 2)
			test.getDeck().remove(0);
		String string = "" + test.getDeck().get(0) + test.getDeck().get(1);
		String string2 = "" + test.getDeck().get(1) + test.getDeck().get(0);
		Assert.assertTrue(string.equals(test.toString()) || string2.equals(test.toString()));
	}

	@Test
	public void testDeal() {
		CanadianSaladModel game = game();
		Assert.assertFalse(game.getCurrentPlayer().getHand().isEmpty());
	}

	@Test
	public void testTrickAdd() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		Assert.assertTrue(game.getTricks().size() == 2);
	}

	@Test
	public void testGameOver() {
		CanadianSaladModel game = game();
		while (game.getCurrentPlayer().getHand().size() > 0)
			game.computerPlay();
		Assert.assertTrue(game.isGameComplete());
	}

	@Test
	public void testGameNotOver() {
		CanadianSaladModel game = game();
		game.computerPlay();
		Assert.assertFalse(game.isGameComplete());
	}

	@Test
	public void testTrickComplete() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		Assert.assertTrue(game.isTrickComplete());
	}

	@Test
	public void testTrickNotComplete() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		Assert.assertFalse(game.isTrickComplete());
	}

	@Test
	public void testIsFirstTurn() {
		CanadianSaladModel game = game();
		Assert.assertTrue(game.isFirstTurn());
	}

	@Test
	public void testGetThrowOffSuit() {
		CanadianSaladModel game = game();
		Assert.assertTrue(game.getThrowOffSuit().equals(new Card(Suit.Diamonds, Rank.Two)));
	}

	@Test
	public void testGetUser() {
		CanadianSaladModel game = game();
		Assert.assertTrue(game.getUserPlayer().equals(game.getPlayers().get(2)));
	}

	@Test
	public void testGetUsersHand() {
		CanadianSaladModel game = game();
		Assert.assertTrue(game.getUsersHand().equals(game.getPlayers().get(2).getHand()));
	}
	
	@Test
	public void testScorehandNoTricks() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		game.scoreHandNoTricks();
		Assert.assertTrue(game.getCurrentTrick().getTaker().getScore() == 10);
	}

	@Test
	public void testCanFollowSuit() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		Assert.assertTrue(game.canFollowSuit());
	}

	@Test
	public void testCreatesNewTrick() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		Assert.assertTrue(game.trickCards().size() == 1);
	}

	@Test
	public void testIsUsersTurn() {
		CanadianSaladModel game = game();
		game.setCurrentPlayer(game.getPlayers().get(2));
		Assert.assertTrue(game.isUsersTurn());
	}

	@Test
	public void testUserPlayCard() {
		CanadianSaladModel game = game();
		game.computerPlay();
		game.computerPlay();
		game.computerPlay();
		game.setCurrentPlayer(game.getUserPlayer());
		game.playCard(game.getUserPlayer().getHand().get(0));
		Assert.assertTrue(game.getTricks().size() == 2);
	}

	@Test
	public void testSetLoserLeads() {
		CanadianSaladModel game = game();
		game.playCard(game.getThrowOffSuit());
		game.playCard(game.getThrowOffSuit());
		game.playCard(game.getThrowOffSuit());
		game.setLoserLeadsNextTrick();
		Assert.assertTrue(game.getCurrentPlayer().equals(game.getPlayers().get(2)));
	}

	@Test
	public void testNextTurn() {
		CanadianSaladModel game = game();
		game.nextTurn();
		Assert.assertTrue(game.trickCards().size() == 1);
	}

	@Test
	public void testNotNextTurn() {
		CanadianSaladModel game = game();
		game.setCurrentPlayer(game.getUserPlayer());
		game.nextTurn();
		Assert.assertTrue(game.getTricks().size() == 0);
	}

	@Test
	public void testNextTurnGameComplete() {
		CanadianSaladModel game = game();
		while (!game.isGameComplete())
			game.computerPlay();
		game.nextTurn();
		Assert.assertTrue(game.trickCards().size() == 3);
	}

	@Test
	public void testcomputerPlayThrowOffSuit() {
		CanadianSaladModel game = game();
		game.playCard(new Card(Suit.Spades, Rank.Two));
		game.computerPlay();
		Assert.assertTrue(game.trickCards().get(0).getSuit() != game.trickCards().get(1).getSuit());
	}
	
	@Test
	public void testGetStarter() {
		CanadianSaladModel game = game();
		game.computerPlay();
		Assert.assertTrue(game.getCurrentTrick().getStarter().equals(game.getPlayers().get(0)));
	}

	@Test
	public void testPlayerToString() {
		Player chris = new Player("chris");
		chris.getHand().clear();
		chris.getHand().add(new Card(Suit.Hearts, Rank.Jack));
		String test = "Player: chris\ngetHand() Size: 1\n1. Jack of Hearts\n";
		//Assert.assertTrue(test.equals(chris.toString()));
	}

	@Test
	public void testPlayerScore() {
		Player chris = new Player("chris");
		chris.setScore(25);
		String test = "chris's score is: 25";
		//Assert.assertTrue(test.equals(chris.printScore()));
	}
	
	@Test
	public void testGetRank() {
		Assert.assertTrue(new Card(Suit.Hearts, Rank.Nine).getRank().equals(Rank.Nine));
	}

	@SuppressWarnings("unlikely-arg-type")
	@Test
	public void testCardNotEquals() {
		Player chris = new Player("chris");
		Assert.assertFalse(new Card(Suit.Hearts, Rank.Jack).equals(chris));
	}
}
