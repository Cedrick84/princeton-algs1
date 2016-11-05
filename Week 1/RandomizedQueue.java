import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private int itemCount = 0;
    private Item items[] = (Item[]) new Object[2];
    
    public RandomizedQueue() {
        
    } // construct an empty randomized queue
    
    public boolean isEmpty() {
        return itemCount == 0;
    } // is the queue empty?
    
    public int size() {
        return itemCount;
    } // return the number of items on the queue
    
    public void enqueue(Item item) {
        if (item == null) { 
            throw new NullPointerException();   
        }
        
        Boolean shouldResize = itemCount == items.length;
        if (shouldResize) {
            doubleArray();
        }
        
        int i = getOpenIndex();
        items[i] = item;
        itemCount++;
    } // add the item
    
    private int getOpenIndex() {
        int rand = StdRandom.uniform(items.length);
        while (items[rand] != null) {
            rand = StdRandom.uniform(items.length);
        }
        
        return rand;
    }
    
    private int getRandomItemIndex() {
        int rand = StdRandom.uniform(items.length);
        while (items[rand] == null) {
            rand = StdRandom.uniform(items.length);
        }
        
        return rand;
    }
    
    private void doubleArray() {
        int newLength = items.length * 2;
        Item newItems[] = (Item[]) new Object[newLength];
        
        for (int i = 0; i < items.length; i++) {
            newItems[i] = items[i];
        }
        
        items = newItems;
    }
    
    private void halfArray() {
        int newLength = items.length / 2;
        Item newItems[] = (Item[]) new Object[newLength];
        
        int insertIndex = 0;
        for (int i = 0; i < items.length; i++) {
            Item item = items[i];
            if (item != null) {
                newItems[insertIndex] = item;
                insertIndex++;
            }
        }
        
        items = newItems;
    }
    
    public Item dequeue() {
        if (isEmpty()) { 
            throw new NoSuchElementException(); 
        }
        
        int i = getRandomItemIndex();
        Item removed = items[i];
        items[i] = null;
        
        itemCount--;
        
        Boolean shouldResize = itemCount < items.length / 4;
        if (shouldResize) {
            halfArray();
        }
        
        return removed;
    } // remove and return a random item
    
   public Item sample() {
       if (isEmpty()) { 
            throw new NoSuchElementException(); 
       }
       
       int i = getRandomItemIndex(); 
       return items[i];
    } // return (but do not remove) a random item
   
   public Iterator<Item> iterator()  {
       return new RandomizedQueueIterator();
   } // return an independent iterator over items in random order
   
   public static void main(String[] args) {
        /*RandomizedQueue<Integer> randomQueue = new RandomizedQueue<Integer>();
 
        randomQueue.enqueue(1);
        randomQueue.enqueue(2);
        randomQueue.enqueue(3);
        randomQueue.enqueue(4);
        randomQueue.enqueue(5);
        randomQueue.enqueue(6);
        randomQueue.enqueue(7);
        randomQueue.enqueue(8);
        randomQueue.enqueue(9);
        
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        System.out.println("removed: = " + randomQueue.dequeue());
        
        
        Iterator itr = randomQueue.iterator();
        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());
        System.out.println(itr.next());
        
        
        RandomizedQueue<String> rq = new RandomizedQueue<String>();
        rq.enqueue("KEMIEPTDFS");
            
         System.out.println("removed: KEMIEPTDFS = " + rq.dequeue());
         rq.enqueue("DJPXPUDLIH");
             System.out.println("removed: DJPXPUDLIH = " + rq.dequeue());
  
         rq.enqueue("RHSDAFTCMM");
         
         RandomizedQueue<Integer> dq = new RandomizedQueue<Integer>();

        final int N = 1200000;
        for(int i = 0; i < N; i++) {
            
            if (StdRandom.uniform(5) % 2 == 0) {
                
                dq.enqueue(StdRandom.uniform(100000));
            } else if (dq.size() > 0) {
                dq.dequeue();
            }
            
        }
     */
    } // unit testing
   
   private class RandomizedQueueIterator implements Iterator<Item> {
        private int itemsLeft;
        private Item copiedItems[];

        public RandomizedQueueIterator() {
            itemsLeft = itemCount;
            
            copiedItems = (Item[]) new Object[items.length];
            for (int i = 0; i < items.length; i++) {
                copiedItems[i] = items[i];
            }
        }

        public boolean hasNext() {
            return itemsLeft != 0;                      
        }
        
        public void remove() { 
            throw new UnsupportedOperationException();  
        }

        public Item next() {
            if (!this.hasNext()) { 
                throw new NoSuchElementException();          
            }
            
            int rand = StdRandom.uniform(copiedItems.length);
            while (copiedItems[rand] == null) {
                rand = StdRandom.uniform(copiedItems.length);
            }
            
            Item item = copiedItems[rand];
            copiedItems[rand] = null;
            itemsLeft--;
            
            return item;
        }
    }
}