package com.deepak.camelintegration.camel.routes;

import org.apache.camel.builder.RouteBuilder;

public class RabbitMQToLogRoute extends RouteBuilder
{
    private static String mqAddress = "rabbitmq://localhost:5672/orders-exchange?" +
            "queue=orders-queue-priority&exchangeType=topic&routingKey=orders.priority&" +
            "deadLetterExchange=orders.exchange.dle&deadLetterExchangeType=topic&" +
            "deadLetterQueue=orders.dlq&deadLetterRoutingKey=#&autoDelete=false";

    @Override
    public void configure() throws Exception
    {
        from("rabbitmq://localhost:5672/orders-queue-priority")
                .log("stream:out");
    }
}
