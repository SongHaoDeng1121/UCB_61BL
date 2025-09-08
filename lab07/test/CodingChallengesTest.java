import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CodingChallengesTest {

    @Test
    public void testMissingNumber() {
	    // TODO
        int[] arr = {0,1,2,3,4,6,7};
        assertThat(CodingChallenges.missingNumber(arr)).isEqualTo(5);
    }

    @Test
    public void testIsPermutation() {
	    // TODO
        assertTrue(CodingChallenges.isPermutation("abcd","cdab"));
        assertFalse(CodingChallenges.isPermutation("abcd","cdb"));
        assertFalse(CodingChallenges.isPermutation("abba","aabbaba"));
        assertFalse(CodingChallenges.isPermutation("abbb","aaab"));
    }
}
