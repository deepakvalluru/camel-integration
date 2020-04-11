package com.deepak.camelintegration.camel.routes;

import com.deepak.camelintegration.camel.processor.OrderProcessor;
import com.deepak.camelintegration.model.Order;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestOrderToRabbitMQRoute extends RouteBuilder
{
    private static String mqAddress = "rabbitmq://localhost:5672/orders-exchange?" +
            "queue=orders-queue-priority&exchangeType=topic&routingKey=orders.priority&" +
            "deadLetterExchange=orders.exchange.dle&deadLetterExchangeType=topic&" +
            "deadLetterQueue=orders.dlq&deadLetterRoutingKey=#&autoDelete=false";

    private static String mqAddress1 = "rabbitmq://localhost:5672/orders-exchange?" +
            "skipQueueDeclare=true&exchangeType=topic&routingKey=orders.priority&autoDelete=false";

    @Override
    public void configure() throws Exception
    {
        restConfiguration().component("servlet").bindingMode(RestBindingMode.json).dataFormatProperty("prettyPrint", "true");

        JacksonDataFormat jsonDataFormat = new JacksonDataFormat(Order.class);

        rest("/orders")
                .id("restOrderToRabbitMQRoute")
                .post("/new")
                .consumes("application/json")
                .type(Order.class )
                .route()
                .process( new OrderProcessor() )
                .marshal( jsonDataFormat )
                .log("Response: {body}")
                .to(ExchangePattern.InOnly, mqAddress1)
                .to("stream:out")
                .end();

    }
}
