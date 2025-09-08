import deque.ArrayDeque61B;

import deque.Deque61B;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertThrows;

public class ArrayDeque61BTest {

    @Test
    @DisplayName("ArrayDeque61B has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque61B.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }


    @Test
    public void addFirstTestBasic() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        deque.addFirst("back");   // ["back"]
        assertThat(deque.toList()).containsExactly("back").inOrder();

        deque.addFirst("middle"); // ["middle", "back"]
        assertThat(deque.toList()).containsExactly("middle", "back").inOrder();

        deque.addFirst("front");  // ["front", "middle", "back"]
        assertThat(deque.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    public void addLastTestBasic() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        deque.addLast("front");
        deque.addLast("middle");
        deque.addLast("back");

        assertThat(deque.toList()).containsExactly("front", "middle", "back").inOrder();
    }

    @Test
    public void addFirstAndAddLastTest() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();

        deque.addLast(0);   // [0]
        deque.addLast(1);   // [0, 1]
        deque.addFirst(-1); // [-1, 0, 1]
        deque.addLast(2);   // [-1, 0, 1, 2]
        deque.addFirst(-2); // [-2, -1, 0, 1, 2]

        assertThat(deque.toList()).containsExactly(-2, -1, 0, 1, 2).inOrder();
    }

    @Test
    public void isEmptyTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        assertThat(deque.isEmpty()).isTrue();

        deque.addFirst("hello");
        assertThat(deque.isEmpty()).isFalse();
    }

    @Test
    public void sizeTest() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();

        assertThat(deque.size()).isEqualTo(0);

        deque.addLast(1);
        deque.addFirst(0);
        deque.addLast(2);

        assertThat(deque.size()).isEqualTo(3);
    }

    @Test
    public void removeFirstTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");

        deque.removeFirst(); // removes "a"
        assertThat(deque.toList()).containsExactly("b", "c").inOrder();
    }

    @Test
    public void removeLastTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        deque.addLast("a");
        deque.addLast("b");
        deque.addLast("c");

        deque.removeLast(); // removes "c"
        assertThat(deque.toList()).containsExactly("a", "b").inOrder();
    }

    @Test
    public void getTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        deque.addLast("x");
        deque.addLast("y");
        deque.addLast("z");

        assertThat(deque.get(0)).isEqualTo("x");
        assertThat(deque.get(1)).isEqualTo("y");
        assertThat(deque.get(2)).isEqualTo("z");
    }

    @Test
    public void getRecursiveThrowsTest() {
        Deque61B<String> deque = new ArrayDeque61B<>();

        // 期望 getRecursive 调用时抛出 UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> {
            deque.getRecursive(0);
        });
    }

    @Test
    public void resizeTest(){
        Deque61B<Integer> deque = new ArrayDeque61B<>();

        for (int i = 0; i < 20; i++) {
            deque.addLast(i);
        }

        assertThat(deque.size()).isEqualTo(20);

        for (int i = 0; i < 20; i++) {
            assertThat(deque.get(i)).isEqualTo(i);
        }

        for (int i = 0; i < 15; i++) {
            deque.removeFirst();
        }

        assertThat(deque.size()).isEqualTo(5);
        for (int i = 0; i < 5; i++) {
            assertThat(deque.get(i)).isEqualTo(i + 15);
        }
    }

    @Test
    public void addLastTestBasicWithoutToList() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();

        lld1.addLast("front"); // after this call we expect: ["front"]
        lld1.addLast("middle"); // after this call we expect: ["front", "middle"]
        lld1.addLast("back"); // after this call we expect: ["front", "middle", "back"]
        assertThat(lld1).containsExactly("front", "middle", "back");
    }

    @Test
    public void testEqualDeques61B() {
        Deque61B<String> lld1 = new ArrayDeque61B<>();
        Deque61B<String> lld2 = new ArrayDeque61B<>();

        lld1.addLast("front");
        lld1.addLast("middle");
        lld1.addLast("back");

        lld2.addLast("front");
        lld2.addLast("middle");
        lld2.addLast("back");

        assertThat(lld1).isEqualTo(lld2);
    }

    @Test
    void testIterator() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.addLast(2);
        deque.addLast(3);

        int expected = 1;
        for (int value : deque) {
            assertThat(value).isEqualTo(expected);
            expected++;
        }

        assertThat(expected).isEqualTo(4);


    }


    @Test
    public void testEqualArrayDeques61B() {
        Deque61B<String> d1 = new ArrayDeque61B<>();
        Deque61B<String> d2 = new ArrayDeque61B<>();

        d1.addLast("front");
        d1.addLast("middle");
        d1.addLast("back");

        d2.addLast("front");
        d2.addLast("middle");
        d2.addLast("back");

        assertThat(d1).isEqualTo(d2);
    }

    @Test
    public void testRemoveFirstToOneAndEmpty() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addLast(1);
        deque.addLast(2);

        deque.removeFirst();
        deque.removeFirst();

        assertThat(deque.size()).isEqualTo(0);
        assertThat(deque.removeFirst()).isNull();
    }


    @Test
    public void testRemoveLastToOneAndEmpty() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();
        deque.addFirst(1);
        deque.addFirst(2);

        deque.removeLast();
        deque.removeLast();

        assertThat(deque.size()).isEqualTo(0);
        assertThat(deque.removeLast()).isNull();
    }

    @Test
    public void testGetEdgeCases() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        deque.addLast("a");
        deque.addLast("b");

        assertThat(deque.get(-1)).isNull();
        assertThat(deque.get(100)).isNull();
        assertThat(deque.get(1)).isEqualTo("b");
    }


    @Test
    public void testSizeAfterRemoveFromEmpty() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        deque.removeFirst(); // should be null
        assertThat(deque.size()).isEqualTo(0);

        deque.addLast("a");
        deque.removeLast();
        assertThat(deque.size()).isEqualTo(0);
    }

    @Test
    public void testAddAfterRemoveToEmpty() {
        Deque61B<Integer> deque = new ArrayDeque61B<>();

        deque.addLast(1);
        deque.removeLast();
        deque.addFirst(2);
        assertThat(deque.get(0)).isEqualTo(2);
        deque.removeFirst();
        deque.addLast(3);
        assertThat(deque.get(0)).isEqualTo(3);
    }

    @Test
    public void testToListEmpty() {
        Deque61B<String> deque = new ArrayDeque61B<>();
        List<String> result = deque.toList();
        assertThat(result).isEmpty();
    }


}
