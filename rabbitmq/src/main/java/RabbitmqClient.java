import com.rabbitmq.client.*;
import com.rabbitmq.client.impl.recovery.AutorecoveringConnection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class RabbitmqClient {

    public static void main(String[] args) throws IOException, TimeoutException {
        ConnectionFactory factory  = new ConnectionFactory();
        factory.setHost("ip");
        factory.setPort(5672);
//        默认情况下，会恢复所有的连接信息
//        factory.setAutomaticRecoveryEnabled(true);
//        只能恢复tag不同的consumer，以下选择不恢复
//        factory.setTopologyRecoveryEnabled(false);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        ((AutorecoveringConnection)connection).addRecoveryListener(new RecoveryListener() {
            @Override
            public void handleRecovery(Recoverable recoverable) {
                //log.info("recovering connection finished");
            }
            @Override
            public void handleRecoveryStarted(Recoverable recoverable) {
                //start to recover connection
            }
        });

        String queue = "queue_name";
//        自动生成queue name
//        queue = channel.queueDeclare().getQueue();
        String exchange = "exchange_name";
        channel.exchangeDeclare(exchange, "fanout");
        channel.queueDeclare(queue, true, false, false, null);
        channel.queueBind(queue, exchange, "");
        final String[] tag = {null};
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) {
                try {
                    tag[0] = consumerTag;
                    //do something with the message -> body
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        channel.basicConsume(queue, true, consumer);
//        channel.basicCancel(tag[0]);
    }
}
