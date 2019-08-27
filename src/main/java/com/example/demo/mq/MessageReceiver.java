package com.example.demo.mq;

import java.io.IOError;
import java.io.IOException;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;

@Component
/*@RabbitListener底层使用aop拦截,如果程序没有抛出异常,会自动提交事务
 * 如果aop使用异常通知拦截获取异常信息的话,自动实现补偿机制,该消息会一直缓存在rabbitmq服务器中,会重试到不抛异常为止
 * */
@RabbitListener(queues="queue002")  //监听这个队列,有消息就会发过来,项目启动就开始监听
public class MessageReceiver {
	/*rabbit默认情况下,如果消费者程序出现异常的情况下,会自动实现补偿机制
	补偿(重试机制)队列服务器发送补偿请求*/
	/*如果调用第三方接口,可根据返回值,手动抛出异常,实现自动重试*/
	@RabbitHandler
	public void process(String hello,Channel channel,Message message) throws IOException{
		try {
			Thread.sleep(3000);
			System.out.println("睡眠3s");
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			/*告诉服务器收到这条消息 已经被我消费了,可以在队列删掉,否则消息服务器以为这条消息没处理掉,后续还会再发*/
			/*channel.basicAck();

			deliveryTag:该消息的index
			multiple：是否批量.true:将一次性ack所有小于deliveryTag的消息*/
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			System.out.println("receiver success"+ hello);
		} catch (Exception e) {
			e.printStackTrace();
			/*丢弃这条消息*/
			/*channel.basicNack(delivery.getEnvelope().getDeliveryTag(), false, true);

			deliveryTag:该消息的index
			multiple：是否批量.true:将一次性拒绝所有小于deliveryTag的消息。
			requeue：被拒绝的是否重新入队列*/
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			System.out.println("receiver fail");
		}
		
		
		
	}
}
