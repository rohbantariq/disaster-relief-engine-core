public class Queue<T>{
    private T data;
    private Node<T> front;
    private Node<T> rear;
    public void enqueue(T data){
        Node<T> nn = new Node<>(data);
        if (isEmpty()){
            front = nn;
            rear = nn;
            return;
        }
        rear.setNext(nn);
        rear = nn;
    }
    public T dequeue()throws Exception{
        if(isEmpty()){
            rear = null;
            throw new Exception("Empty Queue");
        }
        T temp = front.getData();
        front = front.getNext();
        if(isEmpty()){
            rear = null;
        }
        return temp;
    }
    public boolean isEmpty(){
        return front == null;
    }
    public T peek()throws Exception{
        if(isEmpty()){
            throw new Exception("Empty Queue");
        }
        return front.getData();
    }
}