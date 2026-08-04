public class Stack<T> {
    private Node<T> Top;
    public void push(T data){
        Node<T> nn = new Node<>(data);
        if (isEmpty()) {
            Top = nn;
            return;
        }
        nn.setNext(Top);
        Top = nn;
    }
    public T pop()throws Exception{
        if (isEmpty()){
            throw new Exception("Stack Underflow");
        }
        T temp = Top.getData();
        Top = Top.getNext();
        return temp;
    }
    public T peek()throws Exception{
        if(isEmpty()){
            throw new Exception("Stack Underflow");
        }
        return Top.getData();
    }
    public boolean isEmpty(){
        return Top == null;
    }
}