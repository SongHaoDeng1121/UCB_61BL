import java.util.Arrays;

public class DistributionSorts {

    /* Destructively sorts ARR using counting sort. Assumes that ARR contains
       only 0, 1, ..., 9. */
    public static void countingSort(int[] arr) {
        // TODO: YOUR CODE HERE
        // Step 1: Create a counts array of size 10 (for digits 0 through 9)
        int[] counts = new int[10];

        // Count the occurrences of each number in the input array
        for (int num : arr) {
            counts[num]++;
        }

        // Step 2: Overwrite the original array with the sorted values
        int index = 0; // Index to write into the original array
        for (int i = 0; i < counts.length; i++) {
            // Write the value 'i' counts[i] times into the array
            for (int j = 0; j < counts[i]; j++) {
                arr[index] = i;
                index++;
            }
        }
    }

    /* Destructively sorts ARR using LSD radix sort. */
    public static void lsdRadixSort(int[] arr) {
        int maxDigit = mostDigitsIn(arr);
        for (int d = 0; d < maxDigit; d++) {
            countingSortOnDigit(arr, d);
        }
    }

    /* A helper method for radix sort. Modifies ARR to be sorted according to
       DIGIT-th digit. When DIGIT is equal to 0, sort the numbers by the
       rightmost digit of each number. */
    private static void countingSortOnDigit(int[] arr, int digit) {
        // TODO: YOUR CODE HERE
        int[] counts = new int[10]; // 0–9 possible digit values

        // Step 1: Count occurrences of each digit at the specified place
        for (int num : arr) {
            int digitValue = getDigit(num, digit);
            counts[digitValue]++;
        }

        // Step 2: Compute starting positions (prefix sum)
        int[] starts = new int[10];
        int pos = 0;
        for (int i = 0; i < 10; i++) {
            starts[i] = pos;
            pos += counts[i];
        }

        // Step 3: Place elements into a new sorted array, maintaining stability
        int[] sorted = new int[arr.length];
        for (int num : arr) {
            int digitValue = getDigit(num, digit);
            int insertPos = starts[digitValue];
            sorted[insertPos] = num;
            starts[digitValue]++; // move start index forward for the next same-digit value
        }

        // Step 4: Copy sorted result back into original array
        System.arraycopy(sorted, 0, arr, 0, arr.length);
    }

    /* Returns the largest number of digits that any integer in ARR has. */
    private static int mostDigitsIn(int[] arr) {
        int maxDigitsSoFar = 0;
        for (int num : arr) {
            int numDigits = (int) (Math.log10(num) + 1);
            if (numDigits > maxDigitsSoFar) {
                maxDigitsSoFar = numDigits;
            }
        }
        return maxDigitsSoFar;
    }

    /**
     * Digit 0 is the 1s digit, digit 1 is the 10s digit, etc.
     */
    private static int getDigit(int num, int digit) {
        return (int) (num / (Math.pow(10, digit))) % 10;
    }

    /* Returns a random integer between 0 and 9999. */
    private static int randomInt() {
        return (int) (10000 * Math.random());
    }

    /* Returns a random integer between 0 and 9. */
    private static int randomDigit() {
        return (int) (10 * Math.random());
    }

    private static void runCountingSort(int len) {
        int[] arr1 = new int[len];
        for (int i = 0; i < arr1.length; i++) {
            arr1[i] = randomDigit();
        }
        System.out.println("Original array: " + Arrays.toString(arr1));
        countingSort(arr1);
        if (arr1 != null) {
            System.out.println("Should be sorted: " + Arrays.toString(arr1));
        }
    }

    private static void runLSDRadixSort(int len) {
        int[] arr2 = new int[len];
        for (int i = 0; i < arr2.length; i++) {
            arr2[i] = randomInt();
        }
        System.out.println("Original array: " + Arrays.toString(arr2));
        lsdRadixSort(arr2);
        System.out.println("Should be sorted: " + Arrays.toString(arr2));

    }

    public static void main(String[] args) {
        runCountingSort(20);
        runLSDRadixSort(3);
        runLSDRadixSort(30);
    }
}