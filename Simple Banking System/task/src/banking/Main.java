package banking;

public class Main {
    public static void main(String[] args) {
      /*  Card card1 = new Card();
        card1.balance = 500;
        Card card2 = new Card();
        card2.balance = 600;
        Process process = new Process("db");
        System.out.println(card1.cardNumber);
        System.out.println(card1.pin);
        System.out.println(card2.cardNumber);
        System.out.println(card1.pin);
        process.doTransfer(card1);
        System.out.println(card1.balance);
        System.out.println(card2.balance);

*/

        //Process process = new Process("dv");
        Process process = new Process(args[1]);
        process.process();
    }
}