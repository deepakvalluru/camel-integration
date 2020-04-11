package com.deepak.camelintegration.camel.routes;

import com.deepak.camelintegration.camel.processor.OrderProcessor;
import com.deepak.camelintegration.model.Order;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestOrderToRabbitMQRoute extends RouteBuilder
{
    private static String FROM_RABBIT_MQ = "rabbitmq://localhost:5672/orders-exchange?" +
            "queue=orders-queue-priority&exchangeType=topic&routingKey=orders.priority&" +
            "deadLetterExchange=orders.exchange.dle&deadLetterExchangeType=topic&" +
            "deadLetterQueue=orders.dlq&deadLetterRoutingKey=orders.priority&autoDelete=false";

    private static String TO_RABBIT_MQ = "rabbitmq://localhost:5672/orders-exchange?" +
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
                .log("Payload sent to RabbitMQ: ${body}")
                .to(ExchangePattern.InOnly, TO_RABBIT_MQ)
                .end()
                .unmarshal()
                .json(JsonLibrary.Jackson);

        from(FROM_RABBIT_MQ)
                .id("rabbitMQToLogRoute")
                .log("Payload from RabbitMQ: ${body}");

    }
}
