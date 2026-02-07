package aerith.task;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class TodoTest {
    @Test
    public void testFromSaveFormat() {
        assertEquals(new Todo("buy bread"), Todo.fromSaveFormat(" 0 | buy bread"));

        Todo markedTodo = new Todo("buy bread");
        markedTodo.setIsDone(false);
        assertEquals(Todo.fromSaveFormat(" 1 | buy bread"), markedTodo);
    }
}
