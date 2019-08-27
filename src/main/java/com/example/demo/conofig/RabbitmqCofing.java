package com.example.demo.conofig;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitmqCofing {
	/*死信队列*/
	public final static String deadqueuename="dead_queue";
	/*死信交换机*/
	public final static String deadexchangename="dead_exchange";
	/*死信routingkey*/
	public final static String deadroutingkey="dead_routingkey";
	@Value("${spring.rabbitmq.host}")
	private String host;
	@Value("${spring.rabbitmq.port}")
	private String port;
	@Value("${spring.rabbitmq.username}")
	private String username;
	@Value("${spring.rabbitmq.password}")
	private String password;
	@Value("${spring.rabbitmq.virtual-host}")
	private String virtualhost;
	@Bean
	public ConnectionFactory connectionFactory() {
		CachingConnectionFactory connection = new CachingConnectionFactory();
		connection.setAddresses(host+":"+port);
		connection.setUsername(username);
		connection.setPassword(password);
        connection.setVirtualHost(virtualhost);
        return connection;
	}
	@Bean //connectionFactory 也是要和最上面方法名保持一致
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        return rabbitTemplate;
    }

	/*定义直连交换器*/
	@Bean
	public DirectExchange directExchange() {
		//创建一个直连的交换器,就是在rabbitmq服务器上创建一个交换器
	    return new DirectExchange("exchange1");
	}
	/*创建队列,用来存放exchange路由过来的消息*/
	 @Bean
	 public Queue queue001() {
		 //后面的true代表需要持久化
	    return new Queue("queue001", true);
	    /*绑定死信交换机*/
	    /*之前创建的队列没有绑定私信队列和死信交换机,不能做更改,需要删掉之前的重新创建*/
	    /*Map<String, Object> args=new HashMap<String, Object>(2);
	    args.put("deadroutingkey", deadroutingkey);
	    args.put("deadexchangename", deadexchangename);
	    return new Queue("queue001", true,false,false,args);*/
	 }
	 /*交换机和队列建立关系进行绑定*/
	 /*参数中队列名和交换名,要和上面bean注入的名字一样*/
	 @Bean
	 public Binding bind001(DirectExchange directExchange,Queue queue001) {
		 /*用绑定key来绑定队列和交换器*/
		 return BindingBuilder.bind(queue001).to(directExchange).with("routingKey1");
	 }
	/*定义广播交换器,不需要路由匹配*/
	@Bean
	public FanoutExchange fanoutExchange () {
	    return new FanoutExchange ("exchange2");
	}
	/*创建两个和广播交换器绑定的队列*/
	@Bean
	 public Queue queue002() {
		 //后面的true代表需要持久化
	    return new Queue("queue002", true);
	 }
	@Bean
	 public Queue queue003() {
		 //后面的true代表需要持久化
	    return new Queue("queue003", true);
	 }
	/*交换机和队列建立关系进行绑定*/
	/*参数中队列名和交换名,要和上面bean注入的名字一样*/
	 @Bean
	 public Binding bind002(FanoutExchange fanoutExchange,Queue queue002) {
		 /*不需要绑定key来绑定队列和交换器*/
		 return BindingBuilder.bind(queue002).to(fanoutExchange);
	 }
	 @Bean
	 public Binding bind003(FanoutExchange fanoutExchange,Queue queue003) {
		 /*不需要绑定key来绑定队列和交换器*/
		 return BindingBuilder.bind(queue003).to(fanoutExchange);
	 }
	/*定义通配符交换器*/
	@Bean
	public TopicExchange  topicExchange  () {
	    return new TopicExchange  ("exchange3");
	}
	/*创建两个和通配符交换器绑定的队列*/
	@Bean
	 public Queue queue004() {
		 //后面的true代表需要持久化
	    return new Queue("queue004", true);
	 }
	@Bean
	 public Queue queue005() {
		 //后面的true代表需要持久化
	    return new Queue("queue005", true);
	 }
	/*参数中队列名和交换名,要和上面bean注入的名字一样*/
	@Bean
	 public Binding bind004(TopicExchange  topicExchange,Queue queue004) {
		 /*需要绑定key来绑定队列和交换器*/
		 return BindingBuilder.bind(queue004).to(topicExchange).with("*.k4.#");
	 }
	@Bean
	 public Binding bind005(TopicExchange  topicExchange,Queue queue005) {
		 /*需要绑定key来绑定队列和交换器*/
		 return BindingBuilder.bind(queue005).to(topicExchange).with("*.k5.#");
	 }
	/*定义死信交换器*/
	@Bean
	public DirectExchange deadExchange() {
	    return new DirectExchange(deadexchangename);
	}
	/*创建死信队列*/
	 @Bean
	 public Queue deadqueue001() {
		 //后面的true代表需要持久化
	    return new Queue(deadqueuename, true);
	 }
	 @Bean
	 public Binding bind006(DirectExchange deadExchange,Queue deadqueue001) {
		 /*用绑定key来绑定死信队列和死信交换器*/
		 return BindingBuilder.bind(deadqueue001).to(deadExchange).with(deadroutingkey);
	 }
}
