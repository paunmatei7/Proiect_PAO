import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhysicalGood extends Product {

    private double weight;
    private int availableQty;

    public PhysicalGood(double price, String name, double weight, int availableQty) {
        super(price, name);
        this.weight = weight;
        this.availableQty = availableQty;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public int getAvailableQty() {
        return availableQty;
    }

    public void setAvailableQty(int availableQty) {
        this.availableQty = availableQty;
    }

    @Override
    public String toString(){
        return 1+","+price+","+name+","+weight+","+availableQty;
    }

}
