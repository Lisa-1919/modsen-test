package org.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class CalculatorTest {

    @Test
    public void testCorrectInput() throws Exception {
        assertEquals("$97,68", Calculator.calculate("toDollars(737p + toRubles($85.4))"));
    }

    @Test
    public void testInputWithoutCurrency() throws Exception {
        assertEquals( "Ошибка ввода", Calculator.calculate("toDollars(60)"));
    }

    @Test
    public void testInputWithIncorrectNumbersOfBrackets() throws Exception {
        assertEquals( "Ошибка ввода", Calculator.calculate("toDollars(60p"));
    }

    @Test
    public void testWithoutConvertOperations() throws Exception {
        assertEquals("$2,00", Calculator.calculate("60p + $1"));
    }

    @Test
    public void test() throws Exception {
        assertEquals("$2,00", Calculator.calculate("toDollars 60,00p + $1"));

    }
}