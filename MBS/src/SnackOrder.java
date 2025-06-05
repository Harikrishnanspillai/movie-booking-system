public class SnackOrder {
    private Snack snack;
    private int quantity;

    public SnackOrder(Snack snack, int quantity) {
        this.snack = snack;
        this.quantity = quantity;
    }

    public Snack getSnack() {
        return snack;
    }

    public void setSnack(Snack snack) {
        this.snack = snack;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
