package aerith;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import aerith.exception.InvalidInputException;

public class ParserTest {
    @Test
    public void parse_missingTaskNumber_exceptionThrown() {
        Aerith aerith = new Aerith();
        Parser parser = aerith.getParser();

        InvalidInputException markException = Assertions.assertThrows(
                InvalidInputException.class, () -> parser.parse("mark"));
        Assertions.assertEquals("Please provide the task number you want to mark as done.",
                markException.getMessage());

        InvalidInputException unmarkException = Assertions.assertThrows(
                InvalidInputException.class, () -> parser.parse("unmark"));
        Assertions.assertEquals("Please provide the task number you want to mark as not done yet.",
                unmarkException.getMessage());
    }
}
