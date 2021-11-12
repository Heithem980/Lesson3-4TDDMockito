public class PaymentService {
    boolean paid = false;

    private static double SEK_TO_EURO = 0.10;

    public static double getSekToEuro(double sek) {
        return sek * SEK_TO_EURO;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean pay(int amount, int cost) {
        if (amount < cost) {
            return false;
        }
        paid = true;
        return true;
    }

    public void pay(int cost) {
        System.out.println("Paid.");
        if (5 > cost) {
            throw new IllegalStateException();
        }
    }
}
