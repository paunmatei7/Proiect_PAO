import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
public class Main {
    static Manager manager;

    public static void main(String[] args) {
        manager=Manager.getInstance();
        manager.setStoreName("Ze best CRM on ze interwebz");
        Scanner in=new Scanner(System.in);
        manager.createStartWindow();
        manager.updateClientsTable();
        manager.updatePhysicalTable();
        manager.updateInvoiceTable();



//        do {
//            System.out.println(manager.getStoreName());
//            System.out.println("====Menu====\n1.Read all from files\n" +
//                    "2.Save all to files\n" +
//                    "3.Print all\n" +
//                    "4.Add new\n" +
//                    "5.Find\n"+
//                    "\n0.Exit");
//            int op=in.nextInt();
//            switch(op){
//                case 1:{
//                    in.nextLine();
//                    System.out.println("Invoice file name(invoice.csv) :");String invoice=in.nextLine();
//                    System.out.println("Client file name(client.csv)  :");String client=in.nextLine();
//                    System.out.println("Product file name(product.csv) :");String product=in.nextLine();
//                    manager.readInvoiceCSV(invoice);
//                    manager.readClientCSV(client);
//                    manager.readProductsCSV(product);
//                    System.out.println("Loaded");
//                    break;
//                }
//                case 2:{
//                    manager.printProductsCSV("productout.csv");
//                    manager.printClientsCSV("clientout.csv");
//                    manager.printInvoicesCSV("invoicesout.csv");
//                    System.out.println("Saved");
//                    break;
//                }
//                case 3:{
//                    System.out.println("====Print====\n" +
//                            "1.Clients\n" +
//                            "2.Invoices\n" +
//                            "3.Products\n" +
//                            "0.Exit");
//                    int p=in.nextInt();
//                    switch(p){
//                        case 1:{
//                            manager.printClients();
//                            break;
//                        }
//                        case 2:{
//                            manager.printInvoices();
//                            break;
//                        }
//                        case 3:{
//                            manager.printProducts();
//                            break;
//                        }
//                        case 0:{
//                            break;
//                        }
//                    }
//                    break;
//                }
//                case 4:{
//                    int p;
//                    do {
//                        System.out.println("====Add====\n" +
//                                "1.Client\n" +
//                                "2.Invoice\n" +
//                                "3.Product\n" +
//                                "0.Exit");
//                        p = in.nextInt();
//                        switch(p){
//                            case 1:{
//                                in.nextLine();
//                                System.out.println("Client name:");
//                                String name=in.nextLine();
//                                System.out.println("Client addres:");
//                                String addres=in.nextLine();
//                                System.out.println("Client phone:");
//                                String phone=in.nextLine();
//                                manager.addClient(new Client(name,phone,addres));
//                                break;
//                            }
//                            case 2:{
//                                in.nextLine();
//                                System.out.println("Invoice client id:");
//                                int id=in.nextInt();
//                                Client client=manager.getClientById(id);
//                                System.out.println("Products list(ids separated by spaces):");
//                                String list=in.nextLine();
//                                ArrayList<Product> prodList=new ArrayList<>();
//                                for(String pid:list.split(" ")){
//                                    prodList.add(manager.getProductById(Integer.parseInt(pid)));
//                                }
//                                System.out.println("Paid(True/False):");
//                                boolean paid=in.nextBoolean();
//                                manager.addInvoice(new Invoice(prodList,client,paid));
//                                break;
//                            }
//                            case 3:{
//
//                                in.nextLine();
//                                System.out.println("Product type(1-Physical 2-Service)");
//                                int type=in.nextInt();
//                                System.out.println("Price:");
//                                double price=in.nextDouble();
//                                in.nextLine();
//                                System.out.println("Name:");
//                                String name=in.nextLine();
//                                switch(type){
//                                    case 1:{
//                                        System.out.println("Weight:");
//                                        double w=in.nextDouble();
//                                        System.out.println("Available qty:");
//                                        int qty=in.nextInt();
//                                        manager.addProduct(new PhysicalGood(price,name,w,qty));
//                                        break;
//                                    }
//                                    case 2:{
//                                        System.out.println("Programming language:");
//                                        String lang=in.nextLine();
//                                        manager.addProduct(new Service(price,name,lang));
//                                        break;
//                                    }
//                                    default:{
//                                        break;
//                                    }
//                                }
//
//                                break;
//                            }
//
//                        }
//                    }while(p>0);
//                    break;
//                }
//                case 5:{
//                    int p;
//                    do {
//                        in.nextLine();
//                        System.out.println("====Find====\n" +
//                                "1.Client\n" +
//                                "2.Invoice\n" +
//                                "3.Product\n" +
//                                "0.Exit");
//                        p = in.nextInt();
//                        switch(p){
//                            case 1:{
//                                System.out.println("Id:");
//                                System.out.println(manager.getClientById(in.nextInt()).toString());
//                            }
//                            case 2:{
//                                System.out.println("Id:");
//                                System.out.println(manager.getInvoiceById(in.nextInt()).toString());
//                            }
//                            case 3:{
//                                System.out.println("Id:");
//                                System.out.println(manager.getProductById(in.nextInt()).toString());
//                            }
//                        }
//                    }while(p>0);
//                }
//                case 0:{
//                    return;
//                }
//                default:{
//                    return;
//                }
//            }
//        }while(true);

    }
}
