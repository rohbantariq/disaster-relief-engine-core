public class PriorityQueue<T> {
    private PriorityNode<T> front;
    private PriorityNode<T> rear;

    public void enqueuePriority(T data, int priority) {
        PriorityNode<T> newNode = new PriorityNode<>(data, priority);
        if (front == null) {
            front = rear = newNode;
            return;
        }
        if (newNode.getPriority() > front.getPriority()) {
            newNode.setNext(front);
            front = newNode;
            return;
        }
        PriorityNode<T> temp = front;
        while (temp.getNext() != null && temp.getNext().getPriority() >= newNode.getPriority()) {
            temp = temp.getNext();
        }
        newNode.setNext(temp.getNext());
        temp.setNext(newNode);
        if (newNode.getNext() == null) {
            rear = newNode;
        }
    }

    public T dequeue() throws Exception {
        if (isEmpty()) {
            rear = null;
            throw new Exception("Empty Priority Queue");
        }

        T data = front.getData();
        front = front.getNext();

        if (front == null) {
            rear = null;
        }
        return data;
    }

    public T peek() throws Exception {
        if (isEmpty()) {
            throw new Exception("Empty Priority Queue");
        }
        return front.getData();
    }
    public boolean isEmpty() {
        return front == null;
    }
}