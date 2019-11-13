package Model;

public class Type {
    String type;
    int quantity;
    public Type(String type, int quantity){
        this.type = type;
        this.quantity = quantity;
    }

    public Type() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
