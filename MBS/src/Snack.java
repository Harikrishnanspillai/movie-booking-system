public class Snack {
    private int id, quantity;
    private String name;
    private double price;

    public Snack(int id, String name, double price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity=quantity;
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
    public void setId(int id) {
        this.id = id;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setPrice(double price) {
        this.price = price;
    }
}
