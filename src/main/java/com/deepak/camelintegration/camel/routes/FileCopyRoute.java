package com.deepak.camelintegration.camel.routes;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class FileCopyRoute extends RouteBuilder
{

    @Override
    public void configure() throws Exception {
        from("file:C:/Users//Deepu//Downloads//test?noop=true")
                .id( "fileCopyRoute")
                .to("file:C://Users//Deepu//Downloads//testNew");
    }
}
