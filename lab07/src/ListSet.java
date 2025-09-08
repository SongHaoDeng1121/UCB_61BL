import java.util.ArrayList;
import java.util.List;

/**
 * Represents a set of ints. A simple implementation of a set using a list.
 */
public class ListSet implements SimpleSet {

    List<Integer> elems;

    public ListSet() {
        elems = new ArrayList<Integer>();
    }

    /** Adds k to the set. */
    @Override
    public void add(int k) {
        // TODO: Implement this method.
        if (!elems.contains(k)){
            elems.add(k);
        }
    }

    /** Removes k from the set. */
    @Override
    public void remove(int k) {
        Integer toRemove = k;
        // TODO - use the above variable with an appropriate List method.
        // The reason is beyond the scope of this lab, but involves
        // method resolution.
        if (elems.contains(k)){
            elems.remove(toRemove);
        }






    }

    /** Return true if k is in this set, false otherwise. */
    @Override
    public boolean contains(int k) {
        // TODO: Implement this method.
        if (elems.contains(k)){
            return true;
        }
        return false;
    }

    /** Return true if this set is empty, false otherwise. */
    @Override
    public boolean isEmpty() {
        if (elems.isEmpty()){
            return this.size() == 0;
        }
        return this.size() != 0;
    }

    /** Returns the number of items in the set. */
    @Override
    public int size() {
        // TODO: Implement this method.
        return elems.size();
    }

    /** Returns an array containing all of the elements in this collection. */
    @Override
    public int[] toIntArray() {
        // TODO - use a for loop!
        int[] result = new int[elems.size()];
        for (int i = 0; i < elems.size(); i++){
            result[i] = elems.get(i);
        }
        return result;
    }
}
