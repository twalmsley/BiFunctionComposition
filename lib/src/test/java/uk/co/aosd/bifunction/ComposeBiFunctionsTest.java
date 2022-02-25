/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package uk.co.aosd.bifunction;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.jupiter.api.Test;

/**
 * @author Tony Walmsley, AOSD Ltd
 */
class ComposeBiFunctionsTest {
    @Test
    void someLibraryMethodReturnsTrue() throws MalformedURLException {
        //
        // Create a ComposableBiFunction to convert a String to a Message using an Environment object.
        //
        final ComposableBiFunction<Env, String, Message> makeMessage = (e, s) -> new Message(
                s + " " + e.greeting() + " " + e.name());

        //
        // Create a ComposableBiFunction to send a message to a URL defined in the Environment object and return a
        // SendMessageResult. (This doesn't actually send any messages as it's just an example.)
        //
        final ComposableBiFunction<Env, Message, SendMessageResult> sendMessage = (e, m) -> new SendMessageResult(true,
                "Sent to: " + e.msgDestination()
                        .toString());

        //
        // Compose the ComposableBiFunction so they use the same Environment object.
        //
        final var makeAndSendMessage = sendMessage.biCombine(makeMessage);
        //
        // Alternative composition that some people will find more intuitive since the two BiFunction parameters are
        // in order of execution.
        //
        final var makeAndSendMessage2 = makeMessage.biAndThen(sendMessage);

        //
        // Create an Environment object and invoke both versions of the combined ComposableBiFunction
        //
        final var env     = new Env("Hello", "Dolly", new URL("http://example.com/handleMessage"));
        final var result  = makeAndSendMessage.apply(env, "Say: ");
        final var result2 = makeAndSendMessage2.apply(env, "Say: ");

        //
        // Confirm that both BiFunction combinations produce the same result.
        //
        assertTrue(result.success());
        assertTrue(result2.success());
        assertEquals("Sent to: http://example.com/handleMessage", result.logMessage());
        assertEquals("Sent to: http://example.com/handleMessage", result2.logMessage());
    }
}

/**
 * A simple immutable environment object
 */
record Env(String greeting, String name, URL msgDestination) {
}

/**
 * A simple immutable message
 */
record Message(String msg) {
}

/**
 * A simple immutable send result
 */
record SendMessageResult(boolean success, String logMessage) {
}
