import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;



@RunWith(JUnit4.class)
public class CalculatorTest {
    @Test
    public void testAdd(int param1, int param2, int param3) {
        Calculator calculator = new Calculator();
        int result = calculator.add(param1, param2);
        Assertions.assertEquals(param3, result, "Expected result is " + param3);
    }
    }
}
