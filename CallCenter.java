// var 3 (CallCenter)
import java.util.LinkedList;
import java.util.Queue;

class CallCenter {
    private Queue<Customer> customerQueue = new LinkedList<>();
    private Operator[] operators;

    public CallCenter(int operatorCount) {
        operators = new Operator[operatorCount];
        for (int i = 0; i < operatorCount; i++) {
            operators[i] = new Operator("Operator " + (i + 1), this);
            operators[i].start();
        }
    }

    public synchronized Customer getNextCustomer() {
        while (customerQueue.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return customerQueue.poll();
    }

    public synchronized void addCustomer(Customer customer) {
        customerQueue.add(customer);
        notifyAll();
    }

    public synchronized void customerCompleted(Customer customer) {
        customerQueue.remove(customer);
    }
}


