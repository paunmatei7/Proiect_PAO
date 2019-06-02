
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;

public class Manager {
    private static Manager ourInstance = new Manager();
    public static Manager getInstance() {
        return ourInstance;
    }
    private Manager() {
    }



    private String storeName;


    private ArrayList<Invoice> invoices = new ArrayList<Invoice>();
    private ArrayList<Product> products = new ArrayList<Product>();
    private ArrayList<Client>  clients  = new ArrayList<Client>();

    private JTable clientTable,physicalTable,invoiceTable,detailsTable;
    private DefaultTableModel modelClient,modelPhysical,modelInvoice,modelDetails;

    private JLabel info;



    public String getStoreName() {
        Logger.log("Manager.getStoreName");
        return storeName;
    }

    public void setStoreName(String storeName) {
        Logger.log("Manager.setStoreName");
        this.storeName = storeName;
    }

    public void addInvoice(Invoice invoice){
        Logger.log("Manager.addInvoice");
        invoices.add(invoice);
    }

    public Invoice getInvoiceById(int id){
        Logger.log("Manager.getInvoiceById");
        if(id<0 || id>=invoices.size())
            return null;
        return invoices.get(id);
    }

    public void addProduct(Product product){
        Logger.log("Manager.addProduct");
        products.add(product);
    }

    public void addProduct(Product[] product){
        Logger.log("Manager.addProduct[]");
        for (Product p:product) {
            products.add(p);
        }
    }

    public Product getProductById(int id){
        Logger.log("Manager.getProductById");
        if(id<0 || id>=products.size())
            return null;
        return products.get(id);
    }

    public int getIdOfProduct(Product p){
        Logger.log("Manager.getIdOfProduct");
        return products.indexOf(p);
    }

    public void addClient(Client client){
        Logger.log("Manager.addClient");
        clients.add(client);
    }

    public Client getClientById(int id){
        Logger.log("Manager.getClientById");
        if(id<0 || id>=clients.size())
            return null;
        return clients.get(id);
    }

    public int getIdOfClient(Client c){
        Logger.log("Manager.getIdOfClient");
        return clients.indexOf(c);
    }

    public void printClients(){
        Logger.log("Manager.printClients");
        System.out.println("===Clients===");
        for (Client client:clients) {
            System.out.printf("[%d]Name:%-20s Ph:%-20s Adr:%s\n",clients.indexOf(client),client.getName(),client.getPhone(),
                                                        client.getAddress());
        }
    }

    public void printProducts(){
        Logger.log("Manager.printProducts");
        System.out.println("===Products===");
        for (Product product:products) {
            if(product instanceof PhysicalGood) {
                System.out.printf("[%d]Name:%-15s Pr:%-10.2f We:%-10.2f Qty:%d\n",products.indexOf(product),
                                                                        product.getName(),
                                                                        product.getPrice(),
                        (                                               (PhysicalGood)product).getWeight(),
                                                                        ((PhysicalGood)product).getAvailableQty());
            } else{
                System.out.printf("[%d]Name:%-15s Pr:%-10.2f Lang:%-10s\n",products.indexOf(product),
                                                                        product.getName(),
                                                                        product.getPrice(),
                                                                        ((Service)product).getProgrLanguage());
            }

        }
    }

    public void printInvoices(){
        Logger.log("Manager.printInvoices");
        System.out.println("===Invoices===");
        for (Invoice invoice:invoices) {
            System.out.printf("[%d]Name:%-20s Paid:%-5s Price:%-5s\n",invoices.indexOf(invoice),
                    invoice.getClient().getName(),invoice.isPaid()?"Yes":"No",invoice.getTotalPrice());
        }
    }

    private ArrayList<String[]> readCSV(String fileName){
        Logger.log("Manager.readCSV");
        ArrayList<String[]> list = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                list.add(values);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void printClientsCSV(String fileName){
        Logger.log("Manager.printClientCSV");
        try(FileWriter fw=new FileWriter(fileName)){
            for(Client c:clients){
                fw.append(c.toString());
                fw.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();

        }
    }

    public void printInvoicesCSV(String fileName){
        Logger.log("Manager.printInvoicesCSV");
        try(FileWriter fw=new FileWriter(fileName)){
            for(Invoice i:invoices){
                fw.append(i.toString());
                fw.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();

        }
    }

    public void printProductsCSV(String fileName){
        Logger.log("Manager.printProductsCSV");
        try(FileWriter fw=new FileWriter(fileName)){
            for(Product p:products){
                fw.append(p.toString());
                fw.append("\n");
            }
        }catch(IOException e){
            e.printStackTrace();

        }
    }

    public void readProductsCSV(String fileName){
        Logger.log("Manager.readProductsCSV");
        ArrayList<String[]> list = this.readCSV(fileName);
        for(String[] s: list){
            if(Integer.parseInt(s[0])==1) {
                Product newProduct=new PhysicalGood(Float.parseFloat(s[1]),s[2],
                                                        Float.parseFloat(s[3]),
                                                        Integer.parseInt(s[4]));
                products.add(newProduct);

            } else if(Integer.parseInt(s[0])==2) {
                Product newProduct=new Service(Float.parseFloat(s[1]),s[2],s[3]);
                products.add(newProduct);

            }

        }

    }

    public void readClientCSV(String fileName){
        Logger.log("Manager.readClientCSV");
        ArrayList<String[]> list = readCSV(fileName);
        for(String[] s: list){
            Client newClient=new Client(s[0],s[1],s[2]);
            clients.add(newClient);
        }
    }

    public void readInvoiceCSV(String fileName){
        Logger.log("Manager.readInvoiceCSV");
        ArrayList<String[]> list = readCSV(fileName);
        for(String[] s: list){
            Client client=getClientById(Integer.parseInt(s[1]));
            String[] values = s[0].split(" ");
            ArrayList<Product> invProduct=new ArrayList<>();
            for(String id : values) {
                invProduct.add(getProductById(Integer.parseInt(id)));
            }
            invoices.add(new Invoice(invProduct,client,Boolean.parseBoolean(s[2])));
        }
    }





    //GUI STUFF

    public void createStartWindow(){
        JFrame frameApp                 = new JFrame();
        JTabbedPane tabbedPane          = new JTabbedPane();
        JPanel panelClienti             = new JPanel();
        JPanel panelPhysical            = new JPanel();
        JPanel panelInvoice             = new JPanel();
        JPanel panelInvoiceDetails      = new JPanel();
        JSplitPane splitPaneInvoice     = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,panelInvoice,panelInvoiceDetails);

        splitPaneInvoice.getLeftComponent().setSize(new Dimension(200,300));
        splitPaneInvoice.setOneTouchExpandable(true);
        splitPaneInvoice.setContinuousLayout(true);
        splitPaneInvoice.setDividerSize(1);
        panelInvoice.setLayout(new BoxLayout(panelInvoice, BoxLayout.PAGE_AXIS));

        // creaza tabela clienti
        clientTableCreate(panelClienti);
        // creaza tabela physical
        physicalTableCreate(panelPhysical);
        // creaza tabela invoice
        invoiceTableCreate(panelInvoice,panelInvoiceDetails);
        frameApp.add(tabbedPane);

        tabbedPane.addTab("Clienti",panelClienti);
        tabbedPane.addTab("Produse",panelPhysical);
        tabbedPane.addTab("Comenzi",splitPaneInvoice);
        // Frame Size
        frameApp.setSize(700, 350);
        frameApp.setResizable(false);
        // Frame Visible = true
        frameApp.setVisible(true);

    }

    private void invoiceTableCreate(JPanel panelInvoice, JPanel panelInvoiceDetails) {
        modelInvoice = new DefaultTableModel();
        modelInvoice.addColumn("Id");
        modelInvoice.addColumn("Nume Client");
        modelInvoice.addColumn("Platit");

        invoiceTable = new JTable(modelInvoice);
        JScrollPane scrollPaneInvoice = new JScrollPane(invoiceTable);
        scrollPaneInvoice.setPreferredSize(new Dimension(200,200));

        invoiceTable.setDefaultEditor(Object.class, null);
        invoiceTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        panelInvoice.add(scrollPaneInvoice);
        invoiceTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent event) {
                if(!event.getValueIsAdjusting()) {
                    if (invoiceTable.getSelectedRow() > -1) {
                        invoiceDetailsCreate(panelInvoiceDetails);
                    }
                }
            }
        });
        JButton buttonAddInvoice = new JButton("New Invoice");
        buttonAddInvoice.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newInvoiceWindow();
            }
        });

        JButton buttonMarkPay = new JButton("Pay");
        buttonMarkPay.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ev) {
                String sql = "update invoice set paid = 1 where id = ?";
                int idInvoice      = Integer.parseInt(invoiceTable.getValueAt(invoiceTable.getSelectedRow(), 0).toString());

                try (Connection connection = ConnectionManager.getInstance().getConnection();
                     PreparedStatement stmt = connection.prepareStatement(sql)) {
                    stmt.setInt(1,idInvoice);
                    stmt.executeUpdate();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
                updateInvoiceTable();
            }
        });

        panelInvoice.add(buttonAddInvoice);
        panelInvoice.add(buttonMarkPay);





        modelDetails = new DefaultTableModel();
        modelDetails.addColumn("Id");
        modelDetails.addColumn("Produs");
        modelDetails.addColumn("Pret");
        detailsTable = new JTable(modelDetails);
        info = new JLabel();

    }

    private void invoiceDetailsCreate(JPanel panelInvoiceDetails) {
        int idInvoice      = Integer.parseInt(invoiceTable.getValueAt(invoiceTable.getSelectedRow(), 0).toString());
        String numeUser    = invoiceTable.getValueAt(invoiceTable.getSelectedRow(), 1).toString();

        double pret = 0;

        JScrollPane scrollPane = new JScrollPane(detailsTable);


        String sql = "Select p.id,p.nume,p.price from physical p " +
                "       join physical_invoice pi on pi.id_physical=p.id" +
                "       where pi.id_invoice = ?";
        modelDetails.setRowCount(0);
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,idInvoice);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                pret += Double.parseDouble(rs.getString("p.price"));
                modelDetails.addRow(new Object[] {
                        rs.getString("p.id"),
                        rs.getString("p.nume"),
                        rs.getString("p.price")

                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        info.setText("<html>Userul " + numeUser + " are o comanda in valoare de "+pret+"<br/>Urmatoarele produse sunt in comanda</html>");
        panelInvoiceDetails.add(info);
        panelInvoiceDetails.add(scrollPane);
    }

    private void physicalTableCreate(JPanel panelPhysical) {
        modelPhysical = new DefaultTableModel();
        modelPhysical.addColumn("Id");
        modelPhysical.addColumn("Nume");
        modelPhysical.addColumn("Cantitate");
        modelPhysical.addColumn("Greutate");
        modelPhysical.addColumn("Pret");

        physicalTable = new JTable(modelPhysical);
        JScrollPane scrollPanePhysical = new JScrollPane(physicalTable);
        scrollPanePhysical.setPreferredSize(new Dimension(600,200));

        JButton buttonAddProduct = new JButton("New Product");
        buttonAddProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPhysicalWindow();
            }
        });

        JButton buttonRemoveProduct = new JButton("Remove Product");
        buttonRemoveProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPhysicalRemoveWindow();

            }
        });


        JButton buttonEditProduct = new JButton("Edit Product");
        buttonEditProduct.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newPhysicalEditWindow();
            }
        });
        panelPhysical.add(scrollPanePhysical);
        panelPhysical.add(buttonAddProduct);
        panelPhysical.add(buttonEditProduct);
        panelPhysical.add(buttonRemoveProduct);
        physicalTable.setDefaultEditor(Object.class, null);
        physicalTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }

    private void clientTableCreate(JPanel panelClienti) {
        modelClient = new DefaultTableModel();
        modelClient.addColumn("Id");
        modelClient.addColumn("Nume");
        modelClient.addColumn("Telefon");
        modelClient.addColumn("Adresa");

        clientTable = new JTable(modelClient);

        JScrollPane scrollPaneClienti = new JScrollPane(clientTable);
        scrollPaneClienti.setPreferredSize(new Dimension(600,200));

        JButton buttonAddClient = new JButton("New Client");
        buttonAddClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newClientWindow();
                                Logger.log("new client");

            }
        });

        JButton buttonRemoveClient = new JButton("Remove Client");
        buttonRemoveClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newClientRemoveWindow();
            }
        });


        JButton buttonEditClient = new JButton("Edit Client");
        buttonEditClient.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newClientEditWindow();
            }
        });
        panelClienti.add(scrollPaneClienti);
        panelClienti.add(buttonAddClient);
        panelClienti.add(buttonEditClient);
        panelClienti.add(buttonRemoveClient);
        clientTable.setDefaultEditor(Object.class, null);
        clientTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    }


    // Client Stuff

    public void updateClientsTable(){

        String sql = "Select * from Client";
        modelClient.setRowCount(0);
        clients.clear();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                Client temp = new Client(
                        rs.getString("nume"),
                        rs.getString("phone"),
                        rs.getString("address"));
                modelClient.addRow(new Object[] {
                        rs.getString("id"),
                        temp.getName(),
                        temp.getPhone(),
                        temp.getAddress()
                });
                clients.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void newClientWindow(){
        JFrame frame = new JFrame("Add Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        JTextField nume = new JTextField(10);
        JTextField telefon = new JTextField(10);
        JTextField adresa = new JTextField(10);
        JLabel labelNume = new JLabel("Nume");
        labelNume.setLabelFor(nume);
        JLabel labelTelefon = new JLabel("Telefon");
        labelNume.setLabelFor(telefon);
        JLabel labelAdresa = new JLabel("Adresa");
        labelNume.setLabelFor(adresa);
        JButton button = new JButton("Enter");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addNewClient(nume.getText(),telefon.getText(),adresa.getText());
                frame.dispose();
                updateClientsTable();

            }
        });

        panel.add(labelNume);
        panel.add(nume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelTelefon);
        panel.add(telefon);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelAdresa);
        panel.add(adresa);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(button);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void newClientEditWindow(){
        JFrame frame = new JFrame("Edit Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));


        JTextField nume         = new JTextField(10);
        JTextField telefon      = new JTextField(10);
        JTextField adresa       = new JTextField(10);
        JLabel labelNume        = new JLabel("Nume");
        JLabel labelTelefon     = new JLabel("Telefon");
        JLabel labelAdresa      = new JLabel("Adresa");
        JButton button          = new JButton("Edit");

        labelNume.setLabelFor(nume);
        labelNume.setLabelFor(adresa);
        labelNume.setLabelFor(telefon);

        int idx= clientTable.getSelectedRow();

        Client temp= getClientById(idx);


        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                editClient(idx,nume.getText(),telefon.getText(),adresa.getText());
                frame.dispose();
                updateClientsTable();

            }
        });
        nume.setText(temp.getName());
        telefon.setText(temp.getPhone());
        adresa.setText(temp.getAddress());
        panel.add(labelNume);
        panel.add(nume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelTelefon);
        panel.add(telefon);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelAdresa);
        panel.add(adresa);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(button);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void newClientRemoveWindow(){
        JFrame frame = new JFrame("Edit Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel labelNume        = new JLabel("Nume");
        JButton buttonRemove    = new JButton("Remove");
        JButton buttonCancel    = new JButton("Cancel");


        int idx= clientTable.getSelectedRow();

        Client temp= getClientById(idx);
        labelNume.setText("Sunteti sigur ca vreti sa stergeti clientul "+temp.getName()+"?");

        buttonRemove.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                deleteClient(idx);
                frame.dispose();
                updateClientsTable();

            }
        });

        panel.add(labelNume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(buttonRemove);
        panel.add(buttonCancel);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void addNewClient(String nume,String telefon, String adresa){
        String sql = "insert into Client(nume,phone,address)" +
                "values(?,?,?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1,nume);
            stmt.setString(2,telefon);
            stmt.setString(3,adresa);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editClient(int idx, String nume,String telefon, String adresa){
        int id = Integer.parseInt(clientTable.getValueAt(idx,0).toString());
        String sql = "update client set " +
                "nume=?," +
                "phone=?," +
                "address=?" +
                "where id=?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1,nume);
            stmt.setString(2,telefon);
            stmt.setString(3,adresa);
            stmt.setInt(4,id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteClient(int idx){
        int id = Integer.parseInt(clientTable.getValueAt(idx,0).toString());
            String sql = "delete from client " +
                "where id=?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,id);
            System.out.println(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    // Product stuff

    public void updatePhysicalTable(){
        String sql = "Select * from Physical";
        modelPhysical.setRowCount(0);
        products.clear();
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                PhysicalGood temp = new PhysicalGood(
                        rs.getDouble("price"),
                        rs.getString("nume"),
                        rs.getDouble("weight"),
                        rs.getInt("quantity")
                );

                modelPhysical.addRow(new Object[] {
                        rs.getInt("id"),
                        temp.getName(),
                        temp.getAvailableQty(),
                        temp.getWeight(),
                        temp.getPrice()
                });
                products.add(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addNewProduct(String nume,String pret, String greutate, String cantitate){
        String sql = "insert into Physical(nume,price,weight,quantity)" +
                "values(?,?,?,?)";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1,nume);
            stmt.setInt(2, Integer.parseInt(pret));
            stmt.setInt(3,Integer.parseInt(greutate));
            stmt.setInt(4, Integer.parseInt(cantitate));
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void editProduct(int idx, String nume,String pret, String greutate, String cantitate){
        int id = Integer.parseInt(physicalTable.getValueAt(idx,0).toString());
        String sql = "update physical set " +
                "nume=?," +
                "price=?," +
                "weight=?," +
                "quantity=? " +
                "where id=?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1,nume);
            stmt.setInt(2, Integer.parseInt(pret));
            stmt.setInt(3,Integer.parseInt(greutate));
            stmt.setInt(4, Integer.parseInt(cantitate));
            stmt.setInt(5 ,id);
            System.out.println(stmt);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteProduct(int idx){
        int id = Integer.parseInt(physicalTable.getValueAt(idx,0).toString());
        String sql = "delete from physical " +
                "where id=?";

        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,id);
            System.out.println(stmt.toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void newPhysicalWindow(){
        JFrame frame = new JFrame("Add product");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        JTextField nume = new JTextField(10);
        JTextField pret = new JTextField(10);
        JTextField cantitate = new JTextField(10);
        JTextField greutate = new JTextField(10);

        JLabel labelNume = new JLabel("Nume");
        JLabel labelPret = new JLabel("Pret");
        JLabel labelCantitate = new JLabel("Cantitate");
        JLabel labelGreutate = new JLabel("Greutate");

        JButton button = new JButton("Enter");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addNewProduct(nume.getText(),pret.getText(),greutate.getText(),cantitate.getText());
                frame.dispose();
                updatePhysicalTable();

            }
        });

        panel.add(labelNume);
        panel.add(nume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelPret);
        panel.add(pret);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelCantitate);
        panel.add(cantitate);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelGreutate);
        panel.add(greutate);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(button);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void newPhysicalEditWindow(){
        JFrame frame = new JFrame("Edit Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));


        JTextField nume = new JTextField(10);
        JTextField pret = new JTextField(10);
        JTextField cantitate = new JTextField(10);
        JTextField greutate = new JTextField(10);

        JLabel labelNume = new JLabel("Nume");
        JLabel labelPret = new JLabel("Pret");
        JLabel labelCantitate = new JLabel("Cantitate");
        JLabel labelGreutate = new JLabel("Greutate");


        int idx= physicalTable.getSelectedRow();
        PhysicalGood temp= (PhysicalGood)getProductById(idx);

        JButton button = new JButton("Enter");


        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                editProduct(idx,nume.getText(),pret.getText(),greutate.getText(),cantitate.getText());
                frame.dispose();
                updatePhysicalTable();

            }
        });
        nume.setText(temp.getName());
        pret.setText(Integer.toString((int)temp.getPrice()));
        cantitate.setText(Integer.toString(temp.getAvailableQty()));
        greutate.setText(Integer.toString((int)temp.getWeight()));
        panel.add(labelNume);
        panel.add(nume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelPret);
        panel.add(pret);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelCantitate);
        panel.add(cantitate);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelGreutate);
        panel.add(greutate);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(button);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void newPhysicalRemoveWindow(){
        JFrame frame = new JFrame("Edit Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));

        JLabel labelNume        = new JLabel("Nume");
        JButton buttonRemove    = new JButton("Remove");
        JButton buttonCancel    = new JButton("Cancel");


        int idx= physicalTable.getSelectedRow();

        Product temp= getProductById(idx);
        labelNume.setText("Sunteti sigur ca vreti sa stergeti clientul "+temp.getName()+"?");

        buttonRemove.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                deleteProduct(idx);
                frame.dispose();
                updatePhysicalTable();

            }
        });

        panel.add(labelNume);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(buttonRemove);
        panel.add(buttonCancel);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    // Invoice stuff

    public void updateInvoiceTable(){

        String sql = "Select i.id,c.nume,i.paid from Invoice i " +
                "       join client c on c.id=i.id_client";
        modelInvoice.setRowCount(0);
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                modelInvoice.addRow(new Object[] {
                        rs.getString("i.id"),
                        rs.getString("c.nume"),
                        rs.getBoolean("i.paid")?"Da":"Nu"
                });
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void newInvoiceWindow(){
        JFrame frame = new JFrame("Add Client");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        try
        {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(10,10,10,10));
        JTextField client = new JTextField(10);
        JTextField produse = new JTextField(10);
        JLabel labelClient = new JLabel("Id Client");
        labelClient.setLabelFor(client);
        JLabel labelProduse = new JLabel("Id Produse(separate prin virgula)");
        labelProduse.setLabelFor(produse);

        JButton button = new JButton("Enter");
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                addNewInvoice(client.getText(),produse.getText());
                frame.dispose();
                updateInvoiceTable();

            }
        });
        panel.add(labelClient);
        panel.add(client);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(labelProduse);
        panel.add(produse);
        panel.add(Box.createRigidArea(new Dimension(0,5)));
        panel.add(button);
        frame.getContentPane().add(panel);
        frame.pack();
        frame.setLocationByPlatform(true);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.requestFocus();
    }

    public void addNewInvoice(String client,String produse){
        String sql = "insert into invoice(id_client,paid)" +
                "values(?,?)";
        int idx=0;
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1,Integer.parseInt(client));
            stmt.setInt(2,0);
            stmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        sql = "select max(id) id from invoice";
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs= stmt.executeQuery();
            if (rs.next()) {
                idx = rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        sql = "insert into Physical_invoice(id_invoice,id_physical)" +
                "values(?,?)";
        String[] produseSplit = produse.split(",");
        try (Connection connection = ConnectionManager.getInstance().getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            for(String produs:produseSplit) {
                stmt.setInt(1, idx);
                stmt.setInt(2, Integer.parseInt(produs));
                stmt.addBatch();
            }
            stmt.executeBatch();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }



}
