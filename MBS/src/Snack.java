public class Snack {
    private int id;
    private int quantity;
    String name;
    double price;

    public Snack(int id, String name, double price, int quantity) {
        this.id = id;
        this.quantity = quantity;
        this.name = name;
        this.price = price;
    }

    public int getId() {
        return id;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }
}
