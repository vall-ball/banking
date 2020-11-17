package banking;

import java.util.Scanner;

public class Process {

    Scanner scanner = new Scanner(System.in);
    DBWorking dbWorking = new DBWorking();
    String dbName;

    Process(String dbName) {
        this.dbName = dbName;
    }

    public void process() {
        dbWorking.createNewDatabase(dbName);
        dbWorking.createTable(dbName);
        boolean flag = true;
        while (flag) {
        System.out.println("1. Create an account");
        System.out.println("2. Log into account");
        System.out.println("0. Exit");
        int choice = Integer.parseInt(scanner.nextLine());
        switch (choice) {
            case 1:
                createCard();
                break;
            case 2:
                flag = logIntoAccount();
                break;
            case 0:
                System.out.println("Bye");
                return;
            }
        }
    }

    public void createCard() {
        Card card = new Card();
        dbWorking.addCard(card, dbName);
        System.out.println("Your card has been created");
        System.out.println("Your card number:");
        System.out.println(card.cardNumber);
        System.out.println("Your card PIN:");
        System.out.println(card.pin);
    }

    public boolean logIntoAccount() {
        System.out.println("Enter your card number:");
        String number = scanner.nextLine();
        Card card = dbWorking.getCard(dbName, number);
        if (card != null) {
            System.out.println("Enter your PIN:");
            String pin = scanner.nextLine();
            if (pin.equals(card.pin)) {
                System.out.println("You have successfully logged in!");
                return actionWithAccount(card.cardNumber);
            } else {
                System.out.println("Wrong card number or PIN!");
                return true;
            }
        } else {
            System.out.println("Wrong card number or PIN!");
            return true;
        }
    }

    public boolean actionWithAccount(String cardNumber) {

        while (true) {
            Card card = dbWorking.getCard(dbName, cardNumber);
            System.out.println("1. Balance");
            System.out.println("2. Add income");
            System.out.println("3. Do transfer");
            System.out.println("4. Close account");
            System.out.println("5. Log out");
            System.out.println("0. Exit");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    System.out.println("Balance: " + card.balance);
                    //System.out.println("pin: " + card.pin);
                    //System.out.println("number: " + card.cardNumber);
                    //System.out.println("id: " + card.id);
                    break;
                case 2:
                    addIncome(card.cardNumber);
                    break;
                case 3:
                    doTransfer(card.cardNumber);
                    break;
                case 4:
                    closeAccount(card.cardNumber);
                    return true;
                case 5:
                    System.out.println("You have successfully logged out!");
                    return true;
                case 0:
                    System.out.println("Bye");
                    return false;
                default:
                    break;
            }
        }
    }

    public void addIncome(String cardNumber) {
        Card card = dbWorking.getCard(dbName, cardNumber);
        System.out.println("Enter income:");
        int income = Integer.parseInt(scanner.nextLine());
        dbWorking.updateBalance(card, dbName, income);
        System.out.println("Income was added!");
    }

    public void doTransfer(String cardNumber) {
        Card card = dbWorking.getCard(dbName, cardNumber);
        System.out.println("Transfer");
        System.out.println("Enter card number:");
        String destinationCardNumber = scanner.nextLine();
        if (!Card.controlNumber(destinationCardNumber)) {
            System.out.println("Probably you made mistake in the card number. Please try again!");
            return;
        }
        Card destinationCard = dbWorking.getCard(dbName, destinationCardNumber);
        if (destinationCard == null) {
            System.out.println("Such a card does not exist.");
            return;
        }
        if (destinationCardNumber.equals(card.cardNumber)) {
            System.out.println("You can't transfer money to the same account!");
            return;
        }
        System.out.println("Enter how much money you want to transfer:");
        int money = Integer.parseInt(scanner.nextLine());
        if (money > card.balance) {
            System.out.println("Not enough money!");
            return;
        }
        dbWorking.transfer(dbName, card, destinationCard, money);
        System.out.println("Success!");
    }

    public void closeAccount(String cardNumber) {
        Card card = dbWorking.getCard(dbName, cardNumber);
        dbWorking.closeAccount(dbName, card);
        System.out.println("The account has been closed!");
    }
}
