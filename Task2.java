import java.util.*;

class Stock {
    String name;
    double price;
    public Stock(String name, double price) {
        this.name = name;
        this.price = price;
    }
    //to update the price
    public void updatePrice(double newPrice) {
        this.price = newPrice;
    }
}
class Portfolio {
    Map<String, Integer> holdings = new HashMap<>();
    double balance;
    
    public Portfolio(double balance) {
        this.balance = balance;
    }
    //to buy a stock
    public void buyStock(String stockName, int quantity, double price) {
        double cost = quantity * price;
        if (cost <= balance) {
            holdings.put(stockName, holdings.getOrDefault(stockName, 0) + quantity);
            balance -= cost;
            System.out.println("Bought " + quantity + " shares of " + stockName);
        } else {
            System.out.println("Insufficient funds to buy " + stockName);
        }
    }
    //to sell a stock
    public void sellStock(String stockName, int quantity, double price) {
        if (holdings.getOrDefault(stockName, 0) >= quantity) {
            holdings.put(stockName, holdings.get(stockName) - quantity);
            balance += quantity * price;
            System.out.println("Sold " + quantity + " shares of " + stockName);
        } else {
            System.out.println("Not enough shares to sell");
        }
    }
    //to display thr portfolio
    public void displayPortfolio() {
        System.out.println("Portfolio:");
        for (Map.Entry<String, Integer> entry : holdings.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " shares");
        }
        System.out.println("Balance: $" + balance);
    }
}
//main function
public class StockTradingPlatform {
    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        Map<String, Stock> mk = new HashMap<>();//mk is market
        mk.put("AAPL", new Stock("AAPL", 150.0));
        mk.put("GOOGL", new Stock("GOOGL", 2800.0));
        mk.put("TSLA", new Stock("TSLA", 700.0));
        mk.put("NVDA",new Stock("NVDA",155.0));
        
        Portfolio pf = new Portfolio(50000.0);
        
        while (true) {
            System.out.println("1. Buy Stock  2. Sell Stock  3. View Portfolio  4. Exit");
            int choice = scan.nextInt();
            
            if (choice == 1) {
                System.out.println("Enter stock symbol:");
                String stock = scan.next();
                if (mk.containsKey(stock)) {
                    System.out.println("Enter quantity:");
                    int qty = scan.nextInt();
                    pf.buyStock(stock, qty, mk.get(stock).price);
                } else {
                    System.out.println("Stock not available");
                }
            } else if (choice == 2) {
                System.out.println("Enter stock symbol:");
                String stock = scan.next();
                if (mk.containsKey(stock)) {
                    System.out.println("Enter quantity:");
                    int qty = scan.nextInt();
                    pf.sellStock(stock, qty, mk.get(stock).price);
                } else {
                    System.out.println("Stock not available");
                }
            } else if (choice == 3) {
                pf.displayPortfolio();
            } else if (choice == 4) {
                System.out.println("Exiting...");
                break;
            } else {
                System.out.println("Invalid choice");
            }
        }
        scan.close();
    }
}
