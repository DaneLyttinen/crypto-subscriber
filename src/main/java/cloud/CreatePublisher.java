package cloud;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import com.google.pubsub.v1.TopicName;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CreatePublisher {
    private static TopicName topicName;
    public static Publisher publisher;

    public static Publisher publisher(String projectId, String topicId)
            throws IOException, ExecutionException, InterruptedException {
        topicName = TopicName.of(projectId, topicId);

        publisher = Publisher.newBuilder(topicName).build();
        try {
            // Create a publisher instance with default settings bound to the topic


            String message = "Publisher started";
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();

            // Once published, returns a server-assigned message id (unique within the topic)
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
            String messageId = messageIdFuture.get();
            System.out.println("Published message ID: " + messageId);
        } finally {
            return publisher;
        }
    }
}