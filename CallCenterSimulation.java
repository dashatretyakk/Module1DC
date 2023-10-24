// var 3 (CallCenter)
public class CallCenterSimulation {
    public static void main(String[] args) {
        CallCenter callCenter = new CallCenter(3); // 3 operators

        for (int i = 1; i <= 10; i++) {
            Customer customer = new Customer("Customer " + i, callCenter);
            customer.start();
        }
    }
}

