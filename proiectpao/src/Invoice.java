import java.util.ArrayList;

public class Invoice {
    private ArrayList<Product> productsList=new ArrayList<Product>();
    private Client client;
    private boolean paid;


    public Invoice(ArrayList<Product> productsList, Client client, boolean paid) {
        this.productsList = productsList;
        this.client = client;
        this.paid = paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isPaid() {
        return paid;
    }

    public ArrayList<Product> getProductsList() {
        return productsList;
    }

    public Client getClient() {
        return client;
    }

    public double getTotalPrice(){
        double price=0;
        for(Product p:productsList){
            price+=p.getPrice();
        }
        return price;
    }
    @Override
    public String toString(){
        StringBuilder s=new StringBuilder();
        Manager manager=Manager.getInstance();
        for(Product product :productsList) {
            s.append(manager.getIdOfProduct(product));
            s.append(" ");
        }

        return s.toString()+","+manager.getIdOfClient(client)+","+paid;

    }




}
