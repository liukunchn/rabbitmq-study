package com.maoyou.rabbit;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * springboot整合rabbitmq
 *      1)添加amqp启动器
 *          <dependency>
 *             <groupId>org.springframework.boot</groupId>
 *             <artifactId>spring-boot-starter-amqp</artifactId>
 *         </dependency>
 *         此时会通过RabbitAutoConfiguration自动配置向容器中添加以下bean实例
 *          CachingConnectionFactory
 *          RabbitTemplate
 *          AmqpAdmin
 *          RabbitMessagingTemplate
 *         CachingConnectionFactory通过RabbitProperties注入配置，因此，相关配置为
 *          spring.rabbitmq.xxx
 *         此外，通过@Import引入了RabbitAnnotationDrivenConfiguration
 *          只有在使用了@EnableRabbit时才会生效？
 *      2）添加配置
 *          spring.rabbitmq.host=192.168.56.10
 *          spring.rabbitmq.port=5672
 *          spring.rabbitmq.virtual-host=/
 *          spring.rabbitmq.username=guest
 *          spring.rabbitmq.password=guest
 *      3)使用AmqpAdmin创建交换机，队列和绑定
     *     @Test
     *     void testExchange() {
     *         Exchange exchange = new DirectExchange(Constant.EXCHENGE_NAME, true, false, null);
     *         amqpAdmin.declareExchange(exchange);
     *         log.info("Exchange[{}]创建完成", Constant.EXCHENGE_NAME);
     *     }
     *
     *     @Test
     *     void testQueue() {
     *         Queue queue = new Queue(Constant.QUEUE_NAME, true, false, false, null);
     *         amqpAdmin.declareQueue(queue);
     *         log.info("Queue[{}]创建完成", Constant.QUEUE_NAME);
     *     }
     *
     *     @Test
     *     void testBinding() {
     *         Binding binding = new Binding(Constant.QUEUE_NAME, Binding.DestinationType.QUEUE, Constant.EXCHENGE_NAME, Constant.ROUTING_KEY, null);
     *         amqpAdmin.declareBinding(binding);
     *         log.info("Binding[{}]创建完成", Constant.ROUTING_KEY);
     *     }
 *      4）使用RabbitTemplate发送消息
     *     @Test
     *     void testSendMessage() {
     *         User user = new User("maoyou", "maoyou");
     *         rabbitTemplate.convertAndSend(Constant.EXCHENGE_NAME, Constant.ROUTING_KEY, user);
     *         log.info("消息发送成功");
     *     }
 *          todo 发送消息的时候MessageConverter默认使用JDK的序列化方式，可以将其配置为json的序列化方式
             * @Configuration
             * public class RabbitConfiguration {
             *     @Bean
             *     public MessageConverter messageConverter() {
             *         return new Jackson2JsonMessageConverter();
             *     }
             * }
 *      5）使用@RabbitListener和@RabbitHandler监听消息
 *          首先要标注@EnableRabbit，才能使用@RabbitListener和@RabbitHandler监听消息
             * @Component
             * @RabbitListener(queues = {Constant.QUEUE_NAME})
             * @Slf4j
             * public class ReceiveService {
             *     @RabbitHandler
             *     public void receiveMessage(Message message, Channel channel, User content) throws UnsupportedEncodingException {
             *         log.info("接收到消息：{}", content);
             *         log.info("消息属性：{}", message.getMessageProperties());
             *         log.info("消息体：{}", new String(message.getBody(), "utf-8"));
             *         try {
             *             Thread.sleep(30*1000);
             *         } catch (InterruptedException e) {
             *             e.printStackTrace();
             *         }
             *         log.info("消息处理完成");
             *     }
             * }
 *      6)todo 关于消费者消息确认机制
 *          在spring-boot-starter-amqp中和amqp-client中有所不同
 *          springboot有三种确认机制，定义在AcknowledgeMode中
 *              AcknowledgeMode.none 相当于 Channel.basicConsume(consume, true)
 *              AcknowledgeMode.mannal 相当于 Channel.basicConsume(consume, false)
 *              AcknowledgeMode.auto 根据监听器是否正常返回或抛出异常来发出ack/nack。这是默认行为
 *          要对springboot的监听器进行配置
 *              spring.rabbitmq.listener.type=simple
 *              spring.rabbitmq.listener.simple.acknowledge-mode=manual
 *              首先配置listener容器类型，默认为simple
 *              然后配置SimpleMessageListenerContainer的AcknowledgeMode，默认为auto
 *      7)todo 关于消费者流量控制
 *          在amqp-client中，通过channel.basicQos(1);来进行流量控制
 *          prefetchCount配置的mq服务器在接收到ack之前可以交付的最大消息数量，和consumer或者listener没有关系,consumer或者Listener只能一个一个地处理
 *          在springboot中prefetchCount应该配置地不是1而是多个，但即便这样listener也只能一个一个地处理
 *      8）生产者消息可靠抵达
 *          ConfirmCallback回调
 *              消息从生产者到mq服务器Broker成功时回调
 *              spring.rabbitmq.publisher-confirm-type=correlated
         *         rabbitTemplate.setConfirmCallback(new RabbitTemplate.ConfirmCallback() {
         *             @Override
         *             public void confirm(CorrelationData correlationData, boolean ack, String cause) {
         *                 if (ack) {
         *                     log.info("消息{}已送达服务器。correlationData={},ack={},caouse={}", correlationData.getId(), correlationData, ack, cause);
         *                 } else {
         *                     log.info("消息{}未送达服务器。correlationData={},ack={},caouse={}", correlationData.getId(), correlationData, ack, cause);
         *                 }
         *                 // 如果回调没有发生，那么应该也是当作消息未送达
         *             }
         *         });
 *          ReturnsCallback回调
 *              消息从交换机到队列失败时回调
 *              spring.rabbitmq.publisher-returns=true
         *        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
         *             @Override
         *             public void returnedMessage(ReturnedMessage returned) {
         *                 log.info("消息{}未送达队列。returned={}", returned.getMessage().getMessageProperties().getHeader("spring_returned_message_correlation"), returned);
         *             }
         *         });
 *          todo  可以本地事务表保证消息可靠抵达：记录消息ID和消息状态。  具体怎么确保消息可靠抵达，还有待研究。
 *      9）当消息被拒绝并且重新入队，那么会一致循环消费，这种情况如何解决？
 *          开启重试
 *          spring.rabbitmq.listener.simple.retry.enabled=true
 *          spring.rabbitmq.listener.simple.retry.max-attempts=5
 *          这里的重试，是消费者端的重试。
 *              如果是acknowledge-mode=none，消息先自动确认，消费者端重试指定次数后抛出异常
 *              如果是acknowledge-mode=auto，消费者端重试指定次数后抛出异常，消息手动拒绝并且不重新入队（或进入死信队列）
 *              如果是acknowledge-mode=manual，业务逻辑应该写在try{}中，抛出异常时，消息手动拒绝并且不重新入队（或进入死信队列），不发生重试
 *          开启重试能在一定程度上解决消费者业务失败的问题，但重试耗尽之后消息依然没有被成功消费，则只能拒绝并且不重新入队（或者进入死信队列）
 *
 *
 *
 *
 */
@SpringBootApplication

public class RabbitSpringbootClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(RabbitSpringbootClientApplication.class, args);
    }

}
