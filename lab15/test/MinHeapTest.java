import org.junit.Test;
import static com.google.common.truth.Truth.*;

public class MinHeapTest {

    @Test
    public void testInsertAndFindMin() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(5);
        heap.insert(3);
        heap.insert(7);

        assertThat(heap.findMin()).isEqualTo(3);
    }

    @Test
    public void testRemoveMin() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(10);
        heap.insert(4);
        heap.insert(15);
        heap.insert(2);

        assertThat(heap.removeMin()).isEqualTo(2);
        assertThat(heap.removeMin()).isEqualTo(4);
        assertThat(heap.removeMin()).isEqualTo(10);
        assertThat(heap.removeMin()).isEqualTo(15);
        assertThat(heap.removeMin()).isNull();
    }

    @Test
    public void testSize() {
        MinHeap<Integer> heap = new MinHeap<>();
        assertThat(heap.size()).isEqualTo(0);

        heap.insert(8);
        heap.insert(3);
        heap.insert(6);
        assertThat(heap.size()).isEqualTo(3);

        heap.removeMin();
        assertThat(heap.size()).isEqualTo(2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInsertDuplicateThrows() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(1);
        heap.insert(2);
        heap.insert(1);
    }

    @Test
    public void testHeapPropertyAfterInsertions() {
        MinHeap<Integer> heap = new MinHeap<>();
        heap.insert(20);
        heap.insert(15);
        heap.insert(10);
        heap.insert(5);
        heap.insert(2);

        assertThat(heap.findMin()).isEqualTo(2);
    }
}
