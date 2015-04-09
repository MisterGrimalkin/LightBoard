package lightboard.util;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class MessageQueueTest {

    private MessageQueue messageQueue;

    @Test
    public void test() {

        messageQueue = new MessageQueue();

        assertEquals("* * *", messageQueue.getCurrentMessage().getMessage());
        assertEquals(0, messageQueue.countMessages());

        messageQueue.setMaxMessages(1, 2);
        messageQueue.addMessage(1, "Do");
        messageQueue.addMessage(1, "Re");
        messageQueue.addMessage(1, "La");
        assertEquals(3, messageQueue.countMessages(1));

        messageQueue.setMaxMessages(2, 3);
        messageQueue.addMessage(2, "Mi");
        messageQueue.addMessage(2, "Fa");
        messageQueue.addMessage(2, "So");
        messageQueue.addMessage(2, "Ti");
        messageQueue.addMessage(2, "DOH!");
        assertEquals(5, messageQueue.countMessages(2));

        assertEquals(8, messageQueue.countMessages());

        expectMessages("Do","Re","Mi","Fa","So","La","Ti","DOH!");

    }

    private void expectMessages(String... messages) {
        for ( String message : messages ) {
            messageQueue.advanceMessage();
            assertEquals(message, messageQueue.getCurrentMessage().getMessage());
        }
        messageQueue.advanceMessage();
        assertEquals(messages[0], messageQueue.getCurrentMessage().getMessage());
    }

}
