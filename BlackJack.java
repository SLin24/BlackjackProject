import java.util.*;
import java.io.*;


/*
Pre Program Notes:

Few Possible Ideas to be aware of for User Input:
- Duplicate Names don't result in errors, but look confusing on the win records
- Too many players causing the deck to run out of cards
    - Could be easily fixed through the reset method, but this case will "never happen" (by assignment instructions)

Possible Improvements:
- Sort the win statistics
- Adjust time for each part to the number of players
- Does not keep track of the stats of people who leave

 */


public class BlackJack {

    public static void sleep(int time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            System.out.println("INTERRUPTED");
        }
    }
    public static class Game {
        ArrayList<Player> players = new ArrayList<Player>();
        Deck deck = new Deck();
        boolean initialize = false;
        Dealer dealer = new Dealer(deck);
        public void preRound() {
            deck.reset();
            dealer.resetHand();
            for (int i = 0; i < players.size(); i++) {
                boolean firstIn = true;
                String ans = "";
                while (!ans.equals("y") && !ans.equals("n") && !ans.equals("yes") && !ans.equals("no")) {
                    if (firstIn) {
                        firstIn = false;
                    } else {
                        System.out.println("Please enter a valid response");
                    }
                    System.out.print(players.get(i).name + ", would you like to play another round? Y/N: ");
                    ans = s.next().strip().toLowerCase();
                }
                if (ans.equals("n") || ans.equals("no")) {
                    players.remove(i);
                    i--;
                } else {
                    players.get(i).resetHand();
                    players.get(i).reset();
                }
            }
            System.out.println();
            if (!this.initialize) {
                this.players.add(new Player(this.deck));
                this.initialize = true;
            }
            
            while (true) {
                boolean firstIn = true;
                String ans = "";
                while (!ans.equals("y") && !ans.equals("n") && !ans.equals("yes") && !ans.equals("no")) {
                    if (firstIn) {
                        firstIn = false;
                    } else {
                        System.out.println("Please enter a valid response");
                    }
                    System.out.println("Would another player like to join? Y/N: ");
                    ans = s.next().strip().toLowerCase();
                }
                if (ans.equals("n") || ans.equals("no")) {
                    break;
                } else {
                    this.players.add(new Player(this.deck));
                }
            }
            if (this.players.size() == 0) {
                return;
            }
            boolean firstIn = true;
            String ans = "";
            while (!ans.equals("y") && !ans.equals("n") && !ans.equals("yes") && !ans.equals("no")) {
                if (firstIn) {
                    firstIn = false;
                } else {
                    System.out.println("Please enter a valid response");
                }
                System.out.println("Would You Like to View Current Win Statistics? Y/N: ");
                ans = s.next().strip().toLowerCase();
            }
            if (ans.equals("y") || ans.equals("yes")) {
                this.displayStats();
            }
        }

        public void displayStats() {
            System.out.println("\nWINS:\n");
            for (int i = 0; i < this.players.size(); i++) {
                System.out.println(this.players.get(i).name + " - " + this.players.get(i).wins);
            }
            System.out.println();
        }

        public void play() {
            for (int it = 0; it < 2; it++) {
                for (int i = 0; i < this.players.size(); i++) {
                    this.players.get(i).hand.hit();
                }
                this.dealer.hand.hit();
            }
            sleep(1000);
            System.out.println("\nInitial Draw:");
            sleep(2000);
            System.out.print("Dealer - ");
            this.dealer.hand.dealerDisplay();
            for (int i = 0; i < this.players.size(); i++) {
                System.out.print(this.players.get(i).name + " - ");
                this.players.get(i).hand.playerDisplay();
            }
            sleep(4000);
            boolean anotherRound = true;
            int roundNum = 1;
            while (anotherRound) {
                System.out.println();
                sleep(1000);
                System.out.println("Start of Round " + roundNum);
                sleep(1000);
                System.out.println();
                anotherRound = false;
                for (int i = 0; i < this.players.size(); i++) {
                    if (this.players.get(i).lose || this.players.get(i).stopped) continue;
                    this.players.get(i).getMove();
                    if (!this.players.get(i).lose && !this.players.get(i).stopped) anotherRound = true;
                    if (this.players.get(i).lose) {
                        System.out.println("Busted. " + this.players.get(i).name + " loses.");
                    }
                    sleep(2000);
                }
                // display all hands
                // more time granted to read the results
                sleep(1000);
                System.out.println("\nEnd of Round " + roundNum + " Summary: ");
                sleep(2000);
                System.out.print("Dealer - ");
                this.dealer.hand.dealerDisplay();
                for (int i = 0; i < this.players.size(); i++) {
                    System.out.print(this.players.get(i).name + " - ");
                    this.players.get(i).hand.playerDisplay();
                }
                roundNum++;
                sleep(6000);
            }
            System.out.println("\nDealer's Turn\n");
            this.dealer.DealerMoves();
            System.out.println("Dealer Final Hand");
            this.dealer.hand.playerDisplay();

            System.out.println("\nEnd of Game Summary: ");
            System.out.print("Dealer - ");
            this.dealer.hand.playerDisplay();
            for (int i = 0; i < this.players.size(); i++) {
                System.out.print(this.players.get(i).name + " - ");
                this.players.get(i).hand.playerDisplay();
            }
            sleep(6000);
            System.out.println("\nResults:\n");
            for (int i = 0; i < this.players.size(); i++) {
                compareHands(this.players.get(i), this.dealer);
                sleep(1000);
            }

        }
    }

    public static void compareHands(Player p, Dealer d) {
        // the way the logic checks work checks for tie first, eliminating the possibility later on that user has an initial blackjack and can lose
        // then checks for player wins which happens, and the remaining choices result in losses
        if (d.hand.value == 21 && d.hand.cards.size() == 2 && p.hand.value == 21 && p.hand.cards.size() == 2) {
            System.out.println("Tie\n");
        } else if (!p.lose && ((d.hand.value > 21 && p.hand.value <= 21) || (p.hand.value <= 21 && p.hand.value > d.hand.value) || (p.hand.value == 21 && p.hand.cards.size() == 2))) {
            p.wins++;
            System.out.println(p.name + " Wins\n"); 
        } else {
            System.out.println(p.name + " Loses\n");
        }
    }

    public static class Hand {
        ArrayList<Card> cards = new ArrayList<Card>();
        int value = 0;
        int ones = 0;
        Deck deck;
        public Hand(Deck d) {
            this.deck = d;
        }
        public void reset() {
            cards.clear();
            this.value = 0;
        }
        // drawing a card and fixing adjusting values of possible aces according to the total card value.
        public void hit() {
            Card temp = deck.deal();
            if (temp.val == 1) {
                ones++;
                this.value += 11;
            } else {
                this.value += Math.min(temp.val, 10);
            }
            this.cards.add(temp);
            if (cards.size() > 2) {
                sleep(1000);
                System.out.println("Received " + temp.name + '\n');
                sleep(1000);
            }
        }
        public void exchange() {
            this.ones--;
            this.value -= 10;
        }
        // displays the player's cards
        public void playerDisplay() {
            if (this.cards.size() == 0) {
                System.out.println("Empty");
            } else {
                for (int i = 0; i < this.cards.size() - 1; i++) {
                    System.out.print(this.cards.get(i).name + ", ");
                }
                System.out.print(this.cards.get(this.cards.size() - 1).name);
            }
            System.out.print("   Total Value: " + this.value + "\n");
        }
        // hides one card that the dealer has
        public void dealerDisplay() {
            if (this.cards.size() == 0) {
                System.out.println("Empty");
            } else {
                for (int i = 1; i < this.cards.size(); i++) {
                    System.out.print(this.cards.get(i).name + ", ");
                }
                System.out.println("???");
            }
        }
    }
    public static class Person {
        Deck deck;
        int wins = 0;
        Hand hand;
        public Person(Deck d) {
            this.deck = d;
            this.hand = new Hand(this.deck);
        }
        public void resetHand() {
            this.hand.reset();
        }
    }

    public static class Player extends Person {
        String name;
        boolean lose = false;
        boolean stopped = false;
        public Player(Deck d) {
            super(d);
            // Not sure if hand is inherited or not
            System.out.print("Welcome! Please input your name: ");
            this.name = s.next().strip();
            System.out.println();
        }

        public void reset() {
            this.lose= false;
            this.stopped = false;
        }
        
        // processes user's move
        public void getMove() {
            if (this.hand.value > 21) {
                if (this.hand.ones >= 1) {
                    this.hand.exchange();
                } else {
                    this.lose = true;
                }
            }
            String move = "";
            boolean first = true;
            // checking that user input is in fact "hit" or "stand" and acting accordingly
            while (!move.equals("hit") && !move.equals("stand")) {
                if (first) {
                    first = false;
                } else {
                    System.out.println("Please correctly input one of the two choices\n");
                }
                System.out.println('\n'+this.name + "'s Turn. Current Hand:");
                this.hand.playerDisplay();
                
                System.out.print("\nPlease choose to either hit or stand: ");
                move = s.next().strip().toLowerCase();
                System.out.println();
            }
            if (move.equals("hit")) {
                this.hand.hit();
                System.out.println("New Hand:");
                if (this.hand.value > 21) {
                    if (this.hand.ones >= 1) {
                        this.hand.exchange();
                    } else {
                        this.lose = true;
                    }
                }
                this.hand.playerDisplay();
            } else {
                System.out.println(this.name + " has elected to stand. " + this.name + "'s hand for the game is sealed.");
                this.hand.playerDisplay();
                System.out.println();
                this.stopped = true;
            }

        }
        
    }

    public static class Dealer extends Person {
        public Dealer(Deck d) {
            super(d);
        }
        public void DealerMoves() {
            while (this.hand.value < 17) {
                this.hand.hit();
                System.out.print("Dealer's hand - ");
                this.hand.dealerDisplay();
                if (this.hand.value > 21) {
                    if (this.hand.ones > 0) {
                        this.hand.exchange();
                    }
                }
                sleep(1500);
            }
            if (this.hand.value <= 21) {
                System.out.println("Dealer Stands\n");
            } else {
                System.out.println("Dealer Busts\n");
            }
            sleep(1500);
        }
        


        
    }
    // converting the two integers for numerical value and suit to a string name
    public static String getName(int val, int suit) {
        String name = "";
        if (val == 1) {
            name += "Ace";
        } else if (val == 11) {
            name += "Jack";
        } else if (val == 12) {
            name += "Queen";
        } else if (val == 13) {
            name += "King";
        } else {
            name += String.valueOf(val);
        }
        name += " of ";
        String suitName = "";
        switch(suit) {
            case 0:
                suitName = "Clubs";
                break;
            case 1:
                suitName = "Diamonds";
                break;
            case 2:
                suitName = "Hearts";
                break;
            case 3:
                suitName = "Spades";
                break;
            default:
                suitName = "Broken";
        }
        name += suitName;
        return name;
    }

    //defining a card which has a numerical value, a suit, and a string name
    public static class Card {
        int val, suit;
        String name;
        public Card(int value, int suit) {
            this.val = value;
            this.suit = suit;
            this.name = getName(this.val, this.suit);
        }
    }

    public static class Deck {
        ArrayList<Card> cards = new ArrayList<Card>();
        public void reset() {
            this.cards.clear();
            //generating all cards in order from 1 - 13, Club - Spades
            for (int suit = 0; suit < 4; suit++) {
                for (int num = 1; num <= 13; num++) {
                    this.cards.add(new Card(num, suit));
                }
            }
            this.shuffle();
        }
        public void shuffle() {
            // random function when multiplied by N and cast to an int, generates a number from (0 to N - 1)
            int sz = this.cards.size();
            for (int i = 0; i < 1000; i++) {
                int idx1 = (int)(sz * Math.random());
                int idx2 = (int)(sz * Math.random());
                Collections.swap(this.cards, idx1, idx2);
            }
        }
        public Card deal() {
            //return the first card in the arraylist, and delete it from the deck
            Card result = this.cards.get(0);
            this.cards.remove(0);
            return result;
        }
    }

    
    public static final Scanner s = new Scanner(System.in);
    public static void main(String[] args) throws Exception {
        Game game = new Game();
        while (true) {
            game.preRound();
            if (game.players.size() == 0) {
                break;
            }
            game.play();
        }
        s.close();
    }

}
