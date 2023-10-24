// var 3 (CallCenter)
class Operator extends Thread {
    private String name;
    private CallCenter callCenter;

    public Operator(String name, CallCenter callCenter) {
        this.name = name;
        this.callCenter = callCenter;
    }

    @Override
    public void run() {
        while (true) {
            Customer customer = callCenter.getNextCustomer();
            if (customer != null) {
                System.out.println(name + " is assisting customer: " + customer.getName());
                customer.receiveAssistance(name);
                callCenter.customerCompleted(customer);
                if (!customer.hasAssistance()) {
                    customer.requeue();
                }
            }
        }
    }
}
