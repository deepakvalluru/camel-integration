package com.deepak.camelintegration.camel.processor;

import com.deepak.camelintegration.model.Order;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

import java.time.Instant;

//@Component
public class OrderProcessor implements Processor {
    @Override
    public void process(Exchange exchange) throws Exception {
        Order order = (Order) exchange.getIn().getBody();
        System.out.println( order );
        order.setOrderNumber("UPDATED" + Instant.now() );
        exchange.getIn().setBody(order);
    }
}
