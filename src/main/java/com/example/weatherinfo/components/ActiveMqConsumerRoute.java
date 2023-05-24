package com.example.weatherinfo.components;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class ActiveMqConsumerRoute extends RouteBuilder
{
    // https://camel.apache.org/components/3.20.x/activemq-component.html
    // to receive data from ActiveMQ need to have activemq.packages.trust-all:true
    // or activemq.packages.trust: com.example.weatherinfo.output.entity.OutBoundWeatherInfo
    @Override
    public void configure() throws Exception {
        from("activemq:queue:{{activemq.weather.queue}}")
                .routeId("activeMQWeatherReceiverId")
                .log("Message received from {{activemq.weather.queue}} queue: ${body}");
    }
}
