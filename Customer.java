// var 3 (CallCenter)
import java.util.Random;

class Customer extends Thread {
    private String customerName;
    private boolean hasAssistance = false;
    private CallCenter callCenter;
    private int requeueInterval;

    public Customer(String name, CallCenter callCenter) {
        this.customerName = name;
        this.callCenter = callCenter;
        this.requeueInterval = new Random().nextInt(10) + 1;
        setName(customerName); // Встановлюємо ім'я потока вручну
    }

    public String getCustomerName() {
        return customerName;
    }

    public boolean hasAssistance() {
        return hasAssistance;
    }

    public void receiveAssistance(String operatorName) {
        System.out.println(customerName + " is being assisted by " + operatorName);
        hasAssistance = true;
    }

    public void requeue() {
        System.out.println(customerName + " has left the queue and will call back in " + requeueInterval + " seconds.");
        try {
            Thread.sleep(requeueInterval * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        callCenter.addCustomer(this);
    }

    @Override
    public void run() {
        callCenter.addCustomer(this);
    }
}
