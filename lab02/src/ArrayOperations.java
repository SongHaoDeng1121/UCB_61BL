/** Array Operations Class. Optional Exercise **/
public class ArrayOperations {
    /**
     * Delete the value at the given position in the argument array, shifting
     * all the subsequent elements down, and storing a 0 as the last element of
     * the array.
     */
    public static void delete(int[] values, int pos) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        for (int i = pos; i < values.length-1; i ++){
            values[i]=values[i+1];
        }
        values[values.length-1]=0;
        // TODO: fill out this function
    }

    /**
     * Insert newInt at the given position in the argument array, shifting all
     * the subsequent elements up to make room for it. The last element in the
     * argument array is lost.
     */
    public static void insert(int[] values, int pos, int newInt) {
        if (pos < 0 || pos >= values.length) {
            return;
        }
        int i = values.length -1;
        while (i>pos){
            i -= 1;
            values[i+1]=values[i];
        }
        values[pos]=newInt;
        // TODO: fill out this function
    }

    /** 
     * Returns a new array consisting of the elements of A followed by the
     *  the elements of B. 
     */
    public static int[] catenate(int[] A, int[] B) {
        // TODO: fill out this function
        int length = A.length + B.length;
        int[] values = new int[length];
        for (int i = 0; i < A.length; i++){
            values[i]=A[i];
        }
        for (int k = A.length; k < length; k++){
            values[k]=B[k-A.length];
        }
        return values;
    }

}