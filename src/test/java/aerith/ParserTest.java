package aerith;

import aerith.exception.InvalidInputException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void handleInput_missingTaskNumber_exceptionThrown() {
        Aerith aerith = new Aerith();
        Parser parser = aerith.getParser();

        InvalidInputException markException = Assertions.assertThrows(InvalidInputException.class, () -> parser.handleInput("mark"));
        Assertions.assertEquals("Please provide the task number you want to mark as done.", markException.getMessage());

        InvalidInputException unmarkException = Assertions.assertThrows(InvalidInputException.class, () -> parser.handleInput("unmark"));
        Assertions.assertEquals("Please provide the task number you want to mark as not done yet.", unmarkException.getMessage());
    }
}