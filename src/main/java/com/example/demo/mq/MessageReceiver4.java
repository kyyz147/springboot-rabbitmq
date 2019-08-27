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
@RabbitListener(queues="queue004")  //监听这个队列,有消息就会发过来,项目启动就开始监听
public class MessageReceiver4 {
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
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
			System.out.println("接收者44 receiver success"+ hello);
		} catch (Exception e) {
			e.printStackTrace();
			/*丢弃这条消息*/
			channel.basicNack(message.getMessageProperties().getDeliveryTag(), false, false);
			System.out.println("接收者44 receiver fail");
		}
		
		
		
	}
}
