public class QuickSort {

    /**
     * @param arr
     *
     * Sort the array arr using quicksort with the 3-scan partition algorithm.
     * The quicksort algorithm is as follows:
     * 1. Select a pivot, partition array in place around the pivot.
     * 2. Recursively call quicksort on each subsection of the modified array.
     */
    public static int[] sort(int[] arr) {
        quickSort(arr, 0, arr.length);
        return arr;
    }

    /**
     * @param arr
     * @param start
     * @param end
     *
     * Helper method for sort: runs quicksort algorithm on array from [start:end)
     */
    private static void quickSort(int[] arr, int start, int end) {
        // TODO: Implement quicksort
        if (end - start <= 1) {
            return; // Base case: 0 or 1 element
        }

        // Partition into three regions
        int[] partitionIndices = partition(arr, start, end);
        int lt = partitionIndices[0];
        int gt = partitionIndices[1];

        // Recursively sort the < pivot and > pivot parts
        quickSort(arr, start, lt);
        quickSort(arr, gt, end);
        
    }

    /**
     * @param arr
     * @param start
     * @param end
     *
     * Partition the array in-place following the 3-scan partitioning scheme.
     * You may assume that first item is always selected as the pivot.
     * 
     * Returns a length-2 int array of indices:
     * [end index of "less than" section, start index of "greater than" section]
     *
     * Most of the code for quicksort is in this function
     */
    private static int[] partition(int[] arr, int start, int end) {
        // TODO: Implement partition
            int pivot = arr[start];

            int lt = start;      // arr[start:lt) < pivot
            int i = start + 1;   // arr[lt:i) == pivot
            int gt = end;        // arr[gt:end) > pivot

            while (i < gt) {
                if (arr[i] < pivot) {
                    swap(arr, i, lt);
                    i++;
                    lt++;
                } else if (arr[i] > pivot) {
                    gt--;
                    swap(arr, i, gt); // don’t increment i here
                } else { // arr[i] == pivot
                    i++;
                }
            }

            return new int[]{lt, gt};
    }
        private static void swap(int[] arr, int i, int j) {
            int temp = arr[i];
            arr[i] = arr[j];
            arr[j] = temp;
        }
}
