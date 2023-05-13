import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;



@RunWith(JUnit4.class)
public class CalculatorTest {
    @Test
    public void testAdd() {
        String param1 = System.getProperty("param1");
        String param2 = System.getProperty("param2");
        String param3 = System.getProperty("param3");
        Calculator calculator = new Calculator();
        int result = calculator.add(Integer.parseInt(param1), Integer.parseInt(param2));
        Assertions.assertEquals(Integer.parseInt(param3), result, "Expected result is " + param3);
    }
}
