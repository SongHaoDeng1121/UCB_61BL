import org.junit.Test;
import static com.google.common.truth.Truth.assertWithMessage;
import static com.google.common.truth.Truth.assertThat;


public class ListSetTest {

    @Test
    public void testBasics() {
        ListSet aSet = new ListSet();
        assertWithMessage("Size is not zero upon instantiation").that(aSet.size()).isEqualTo(0);
        for (int i = -50; i < 50; i += 2) {
            aSet.add(i);//add
            assertWithMessage("aSet should contain " + i).that(aSet.contains(i));
        }

        assertWithMessage("Size is not 50 after 50 calls to add").that(aSet.size()).isEqualTo(50);
        for (int i = -50; i < 50; i += 2) {
            aSet.remove(i);//remove
            assertWithMessage("aSet should not contain " + i).that(!aSet.contains(i));
        }

        assertWithMessage("aSet is not empty after removing all elements").that(aSet.isEmpty());
        //isEmpty
        assertWithMessage("Size is not zero after removing all elements").that(aSet.size()).isEqualTo(0);

        ListSet bset = new ListSet();
        ListSet cset = new ListSet();
        ListSet dset = new ListSet();

        for (int i = 0; i < 50; i += 2) {
            bset.add(i);
        }
        for (int i = 0; i < 50; i += 2) {
            cset.add(i);
        }
        for (int i = 10; i < 50; i += 2) {
            dset.add(i);
            assertThat(bset.toIntArray()).isEqualTo(cset.toIntArray());
            assertThat(bset.toIntArray()).isNotEqualTo(dset.toIntArray());
        }
    }
}
