import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node first;
    private Node last;
    private int itemCount = 0;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        Node(Item item) {
            this.item = item;
        }
    }
    
    private class DequeIterator implements Iterator<Item> {
        private Node current;

        public DequeIterator() {
            current = first;
        }

        public boolean hasNext() {
            return current != null;                      
        }
        
        public void remove() { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!this.hasNext()) { 
                throw new NoSuchElementException();          
            }
            
            Node node = current;
            current = current.prev;
            return node.item;
        }
    }
    
   public Deque() {
   
   } // construct an empty deque
   
   public boolean isEmpty() {
      return size() == 0; 
   }                 // is the deque empty?
   public int size() {
       return itemCount;
   }     
   

// return the number of items on the deque
   public void addFirst(Item item) {
        if (item == null) { 
            throw new NullPointerException();   
        }
        
        if (isEmpty()) {
            first = new Node(item);
            last = first;
        } else {
            Node oldFirst = first;
            first = new Node(item);
            oldFirst.next = first;
            first.prev = oldFirst;
        }
        
        itemCount++;
   
   }          // add the item to the front
   
   
   
   public void addLast(Item item) {
        if (item == null) { 
            throw new NullPointerException();   
        }
        
        if (isEmpty()) {
            first = new Node(item);
            last = first;
        } else {
            Node oldLast = last;
            last = new Node(item);
            oldLast.prev = last;
            last.next = oldLast;
        }
        
        itemCount++;
   
   }       // add the item to the end
   
   public Item removeFirst() {
        if (isEmpty()) { 
            throw new NoSuchElementException(); 
        }

        Node node = first;
        
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            first.prev.next = null;
            first = first.prev;
        }
        
        itemCount--;
        
        node.next = null;
        return node.item;
    }    // remove and return the item from the front
   
   
   public Item removeLast() {
        if (isEmpty()) { 
            throw new NoSuchElementException(); 
        }

        Node node = last;
        
        if (size() == 1) {
            first = null;
            last = null;
        } else {
            last.next.prev = null;
            last = last.next;
        }
        
        itemCount--;
        
        node.next = null;
        return node.item;
    } // remove and return the item from the end
   
   
   
   
   public Iterator<Item> iterator() {
       return new DequeIterator();
   } // return an iterator over items in order from front to end
   
   
   
   public static void main(String[] args) {
       
        Deque<Integer> deq = new Deque<Integer>();

        System.out.println("size: (0) = " + deq.size());

        deq.addFirst(1);
        deq.addFirst(2);
        deq.addFirst(3);
        deq.addFirst(4);
        
        
        System.out.println("size: (4) = " + deq.size());

        deq.removeLast();
        deq.removeFirst();
        
        System.out.println("size: (2) = " + deq.size());
        
        deq.removeLast();
        deq.removeLast();
        
        System.out.println("size: (0) = " + deq.size());

        deq.addFirst(3);
        deq.addLast(2);
        
        System.out.println("size: (2) = " + deq.size());

        deq.addFirst(4);
        deq.addLast(1);
        
        System.out.println("size: (4) = " + deq.size());
        System.out.println("ierating: ");

        Iterator itr = deq.iterator();
        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());
    }
}