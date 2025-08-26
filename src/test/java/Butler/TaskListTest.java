package Butler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskListTest {

    @Test
    void addAndGet_multipleTasks_preservesInsertionOrder() {
        TaskList list = new TaskList();
        Task t1 = new Todo("alpha");
        Task t2 = new Todo("beta");
        Task t3 = new Todo("gamma");

        list.add(t1);
        list.add(t2);
        list.add(t3);

        assertEquals(3, list.size());
        assertSame(t1, list.get(0));
        assertSame(t2, list.get(1));
        assertSame(t3, list.get(2));
    }

    @Test
    void remove_middleTask_shiftsRemainingTasksAndReducesSize() {
        TaskList list = new TaskList();
        Task t1 = new Todo("first");
        Task t2 = new Todo("second");
        Task t3 = new Todo("third");

        list.add(t1);
        list.add(t2);
        list.add(t3);

        Task removed = list.remove(1);
        assertSame(t2, removed);
        assertEquals(2, list.size());
        assertSame(t1, list.get(0));
        assertSame(t3, list.get(1));
    }

    @Test
    void size_addAndRemoveTasks_reflectsCorrectCount() {
        TaskList list = new TaskList();
        assertEquals(0, list.size());

        list.add(new Todo("a"));
        assertEquals(1, list.size());

        list.add(new Todo("b"));
        list.add(new Todo("c"));
        assertEquals(3, list.size());

        list.remove(0);
        assertEquals(2, list.size());
    }

    @Test
    void get_existingTask_returnsSameInstance() {
        TaskList list = new TaskList();
        Todo t = new Todo("unique");
        list.add(t);

        assertSame(t, list.get(0));
        t.mark();
        assertTrue(list.get(0).toString().contains("[X]")); // same object
    }
}

