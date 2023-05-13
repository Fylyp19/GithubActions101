import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.stream.Stream;

@RunWith(JUnit4.class)
public class CalculatorTest {

    @ParameterizedTest
    @MethodSource("provideTestData")
    public void testAdd(int param1, int param2, int param3) {
        Calculator calculator = new Calculator();
        int result = calculator.add(param1, param2);
        Assertions.assertEquals(param3, result, "Expected result is " + param3);
    }

    private static Stream<int[]> provideTestData() {
        int param1 = Integer.parseInt(System.getenv("param1"));
        int param2 = Integer.parseInt(System.getenv("param2"));
        int param3 = Integer.parseInt(System.getenv("param3"));

        return Stream.of(new int[]{param1, param2, param3});
    }
}