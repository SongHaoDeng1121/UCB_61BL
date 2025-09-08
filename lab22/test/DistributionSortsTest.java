import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;

public class DistributionSortsTest {

    @Test
    public void testBasic() {
        // TODO: test it out!
    }
    @Test
    public void testCountingSort() {
        int[] input = {3, 1, 4, 1, 5, 9, 2, 6};
        int[] expected = {1, 1, 2, 3, 4, 5, 6, 9};

        DistributionSorts.countingSort(input);

        assertThat(input).isEqualTo(expected);
    }

    @Test
    public void testLsdRadixSort() {
        int[] input = {209, 356, 112, 294};
        int[] expected = {112, 209, 294, 356};

        DistributionSorts.lsdRadixSort(input);

        assertThat(input).isEqualTo(expected);
    }
}

