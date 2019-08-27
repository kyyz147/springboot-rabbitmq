package com.example.demo.mq;

import java.util.Date;
import java.util.UUID;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageBuilder;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ConfirmCallback;
import org.springframework.amqp.rabbit.core.RabbitTemplate.ReturnCallback;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
@Component
public class MessageSender implements ConfirmCallback,ReturnCallback{
	@Autowired
	private RabbitTemplate rabbitTemplate;
	
	public void send(String exchange,String routingKey){
		JSONObject jsonObject=new JSONObject();
		jsonObject.put("你好现在是", new Date().toString());
		String jsonstring=jsonObject.toJSONString();
		String context="你好现在是"+new Date();
		System.out.println("send content"+context);
		this.rabbitTemplate.setMandatory(true);
		this.rabbitTemplate.setConfirmCallback(this);
		this.rabbitTemplate.setReturnCallback(this);
		//加入全局id,解决幂等问题,防止重复消费
		Message message=MessageBuilder.withBody(jsonstring.getBytes())
				.setContentType(MessageProperties.CONTENT_TYPE_JSON).setContentEncoding("utf-8")
				.setMessageId(UUID.randomUUID()+"").build();
		//发消息
		this.rabbitTemplate.convertAndSend(exchange,routingKey,message);
		//构建回调返回的数据(消息id)
		/*CorrelationData correlationData=new CorrelationData(id);
		this.rabbitTemplate.convertAndSend(exchange,routingKey,message,correlationData);*/
	}
	public void send2(String exchange,String routingKey){
		String context="你好现在是"+new Date();
		System.out.println("send2 content"+context);
		this.rabbitTemplate.setMandatory(true);
		this.rabbitTemplate.setConfirmCallback(this);
		this.rabbitTemplate.setReturnCallback(this);
		//发消息
		this.rabbitTemplate.convertAndSend(exchange,routingKey,context);
	}
	public void send3(String exchange,String routingKey){
		String context="你好现在是"+new Date();
		System.out.println("send3 content"+context);
		this.rabbitTemplate.setMandatory(true);
		this.rabbitTemplate.setConfirmCallback(this);
		this.rabbitTemplate.setReturnCallback(this);
		//发消息
		this.rabbitTemplate.convertAndSend(exchange,routingKey,context);
	}
	@Override
	public void returnedMessage(Message arg0, int arg1, String arg2, String arg3, String arg4) {
		// TODO Auto-generated method stub
		
	}
	/*生产者消息确认机制,生产者往服务器发送消息的时候,采用应答机制,如果发送不成功,再走一次send方法重新发送*/
	@Override
	public void confirm(CorrelationData correlationData, boolean ack, String cause) {
		// TODO Auto-generated method stub
		/*String id=correlationData.getId();
		if(ack){
			System.out.println("消息发送成功");
		}else{
			//重试机制
			send("exchange1", "routingKey1");
			System.out.println("消息发送失败"+cause);
		}*/
		
	}

}
