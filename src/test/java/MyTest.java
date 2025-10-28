import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class MyTest
{
    @Test
    void unitTest()
    {
        assertEquals(5, 5); // Simple integer equality check (PASS)
    }

    @Test
    void unitTest2()
    {
        assertEquals(5, 4); // Integer equality check (FAIL)
    }

    // --- New Test Methods ---

    @Test
    void unitTest3()
    {
        // Equality check with a custom failure message (PASS)
        assertEquals(5, 5, "Messages are equal");
    }

    @Test
    void unitTest4()
    {
        // Double equality check with a delta (tolerance) of 0.02 (PASS)
        // 5.01 is within 0.02 of 5.0
        assertEquals(5.0, 5.01, 0.02);
    }

    @Test
    void unitTest5()
    {
        // Checks if two arrays are equal (element-by-element) (PASS)
        int[] a = {1, 2, 3};
        int[] b = {1, 2, 3};
        assertArrayEquals(a, b);
    }

    @Test
    void unitTest6()
    {
        // Asserts that the condition is true (PASS)
        assertTrue(5 == 5);
    }

    @Test
    void unitTest7()
    {
        // Asserts that the condition is false (PASS)
        assertFalse(5 == 4);
    }

    @Test
    void unitTest8()
    {
        // Asserts that the object reference is null (PASS)
        assertNull(null);
    }

    @Test
    void unitTest9()
    {
        // Asserts that the object reference is not null (PASS)
        assertNotNull("Hello");
    }

    @Test
    void unitTest10()
    {
        // Asserts that executing the method throws a specific exception (PASS)
        assertThrows(NullPointerException.class, this::throwsException);
    }

    /**
     * Helper method used by unitTest10() to throw an exception.
     */
    void throwsException() throws NullPointerException
    {
        throw new NullPointerException();
    }
}