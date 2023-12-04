package org.cis1200.snakeTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class GameTest {
    @Test
    public void test() {
        assertNotEquals("CIS 120", "CIS 160");
    }

}
