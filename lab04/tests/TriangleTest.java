import org.junit.Rule;
import org.junit.Test;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;
public abstract class TriangleTest {


    // source: https://truth.dev/
    /** For autograding purposes; do not change this line. */
    abstract Triangle getNewTriangle();

    /* ***** TESTS ***** */

    // FIXME: Add additional tests for Triangle.java here that pass on a
    //  correct Triangle implementation and fail on buggy Triangle implementations.

    @Test
    public void test1() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        // true case
        assertThat(t.sidesFormTriangle(3,4,5)).isTrue();
        assertThat(t.sidesFormTriangle(6,4,9)).isTrue();
        assertThat(t.sidesFormTriangle(6,6,1)).isTrue();
        // false case
        assertThat(t.sidesFormTriangle(1,2,3)).isFalse();
        assertThat(t.sidesFormTriangle(2,2,10)).isFalse();
        assertThat(t.sidesFormTriangle(4,7,15)).isFalse();


    }

    @Test
    public void test2() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        // False case
        assertThat(t.pointsFormTriangle(1,2,3,6,9,18)).isFalse();
        assertThat(t.pointsFormTriangle(1,0,2,0,3,0)).isFalse();
        assertThat(t.pointsFormTriangle(0,1,0,2,0,3)).isFalse();
        assertThat(t.pointsFormTriangle(0,0,0,0,0,0)).isFalse();
        // Ture case
        assertThat(t.pointsFormTriangle(1,4,10,3,5,9)).isTrue();


    }
    @Test
    public void test3() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        assertThat(t.triangleType(2,3,4)).isEqualTo("Scalene");
        assertThat(t.triangleType(3,3,4)).isNotEqualTo("Scalene");
        assertThat(t.triangleType(3,3,3)).isNotEqualTo("Scalene");
        assertThat(t.triangleType(3,3,4)).isEqualTo("Isosceles");
        assertThat(t.triangleType(2,3,4)).isNotEqualTo("Isosceles");
        assertThat(t.triangleType(3,3,3)).isNotEqualTo("Isosceles");
        assertThat(t.triangleType(3,3,3)).isEqualTo("Equilateral");
        assertThat(t.triangleType(2,3,4)).isNotEqualTo("Equilateral");
        assertThat(t.triangleType(3,3,4)).isNotEqualTo("Equilateral");
    }
    @Test
    public void test4() {
        // TODO: stub for first test
        Triangle t = getNewTriangle();
        // remember that you'll have to call on Triangle methods like
        // t.functionName(arguments), where t is a Triangle object
        assertThat(t.squaredHypotenuse(3,4)).isEqualTo(25);
        assertThat(t.squaredHypotenuse(6,8)).isEqualTo(100);
        assertThat(t.squaredHypotenuse(12,16)).isEqualTo(400);
        assertThat(t.squaredHypotenuse(3,4)).isNotEqualTo(20);
        assertThat(t.squaredHypotenuse(6,8)).isNotEqualTo(90);
        assertThat(t.squaredHypotenuse(12,16)).isNotEqualTo(390);
    }
}
