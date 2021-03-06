import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubIO;
import org.apache.beam.sdk.io.gcp.pubsub.PubsubMessage;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.ParDo;

import java.util.logging.Logger;

public class App {
    public static final Logger LOGGER = Logger.getAnonymousLogger();

    private static class SampleDoFn extends DoFn<PubsubMessage, String> {
        @ProcessElement
        public void process(ProcessContext c) {
            String message = new String(c.element().getPayload());
            LOGGER.info(String.format("Receive Message : %s", message));
            c.output(message.toUpperCase());
        }
    }

    public static void main(String[] args) {
        Pipeline p = Pipeline.create();

        p.apply(PubsubIO.readMessagesWithAttributes().fromSubscription(""))
         .apply("Sample pardo", ParDo.of(new SampleDoFn()));

        p.run();
    }
}
