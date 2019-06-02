abstract class Product {
    protected double price;
    protected String name;


    public Product(double price, String name) {
        this.price = price;
        this.name = name;
    }


    public double getPrice() {
        Logger.log("Product.getPrice");
        return price;
    }

    public void setPrice(double price) {
        Logger.log("Product.setPrice");
        this.price = price;
    }

    public String getName() {
        Logger.log("Product.getName");
        return name;
    }

    public void setName(String name) {
        Logger.log("Product.setName");
        this.name = name;
    }
}
