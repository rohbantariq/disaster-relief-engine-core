public class PriorityNode<T> {
    PriorityNode<T> next;
    private T data;
    private int priority;
    public PriorityNode(T data, int priority) {
        this.data = data;
        this.priority = priority;
    }

    public PriorityNode<T> getNext() {
        return next;
    }

    public void setNext(PriorityNode<T> next) {
        this.next = next;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}