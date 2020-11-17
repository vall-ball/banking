package banking;

import java.util.Random;

public class Card {
    private static int lastId = 0;
    int id;
    String cardNumber;
    int balance;
    String pin;
    Random random = new Random();

    Card() {
        pin = generatePin();
        cardNumber = generateCardNumber();
        balance = 0;
        id = generateId();
    }


    public String generateCardNumber() {
        long prepareNumber = 4000000000000000L + (long)(random.nextInt(10000)) * 10 +  ((long)(random.nextInt(100000)) * 100000);
        return Long.toString(prepareNumber + generateCheckSum(prepareNumber, 16));
    }

    public String generatePin() {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            builder.append(Integer.toString(random.nextInt(10)));
        }
        return builder.toString();
    }

    public static long generateCheckSum(long number, int length) {
        long[] arr = new long[length];

        //make the array of numbers
        for (int i = length - 1; i >= 0; i--) {
            arr[i] = number % 10;
            number /= 10;
        }
        // multiply even numbers by 2
        for (int i = 0; i <= length - 1; i++) {
            if (i % 2 == 0) {
                arr[i] = arr[i] * 2;
            }
        }

        //subtract 9 to numbers over 9
        for (int i = 0; i <= length - 1; i++) {
            if (arr[i] > 9) {
                arr[i] = arr[i] - 9;
            }
        }

        //getting sum
        int sum = 0;
        for (int i = 0; i < length - 1; i++) {
            sum += arr[i];
        }
        return 10 - sum % 10;
    }

    public int generateId() {
        int answer = lastId;
        lastId++;
        return answer;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", cardNumber='" + cardNumber + '\'' +
                ", balance=" + balance +
                ", pin='" + pin + '\'' +
                '}';
    }

    public static boolean controlNumber(String number) {
        long n1 = Long.parseLong(number);
        long n2 = generateCheckSum(n1 - n1 % 10, 16);
        //System.out.println("n1=" + (n1 % 10));
        //System.out.println("n2=" + n2);
        return n1 % 10 == n2;
    }
}
