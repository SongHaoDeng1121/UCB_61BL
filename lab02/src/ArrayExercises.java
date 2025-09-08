public class ArrayExercises {

    /** Returns an array [1, 2, 3, 4, 5, 6] */
    public static int[] makeDice() {
        // TODO: Fill in this function.
        int[] newarray = new int[] {1, 2, 3, 4, 5, 6};
        return newarray;
    }

    /** Returns the positive difference between the maximum element and minimum element of the given array.
     *  Assumes array is nonempty. */
    public static int findMinMax(int[] array) {
        // TODO: Fill in this function.
        int maximum = 0;
        int minimum = array[0];
        for (int i = 0; i < array.length; i += 1) {
            System.out.println(array[i]);
            if (maximum<array[i]){
                maximum = array[i];
            }
            System.out.println("maximum"+maximum);
            if (minimum > array[i]){
                minimum = array[i];
            }
            System.out.println("minimum"+minimum);
        }
        System.out.println(maximum-minimum);
        return maximum-minimum;
    }

}
