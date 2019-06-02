public class Service extends Product {
    private String progrLanguage;

    public String getProgrLanguage() {
        return progrLanguage;

    }

    public void setProgrLanguage(String progrLanguage) {
        this.progrLanguage = progrLanguage;
    }

    public Service(double price, String name, String progrLanguage) {
        super(price, name);
        this.progrLanguage = progrLanguage;
    }

    @Override
    public String toString(){
        return 2+","+price+","+name+","+progrLanguage;
    }
}
