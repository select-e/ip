package aerith.test;

import aerith.task.Todo;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {
    @Test
    public void testFromSaveFormat(){
        assertEquals(Todo.fromSaveFormat(" 0 | buy bread"), new Todo("buy bread"));

        Todo markedTodo = new Todo("buy bread");
        markedTodo.markDone(false);
        assertEquals(Todo.fromSaveFormat(" 1 | buy bread"), markedTodo);
    }
}