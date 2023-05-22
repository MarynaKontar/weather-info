package com.example.weatherinfo.components;

import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleTimer extends RouteBuilder {

    public static final String TIMER_ID = "simpleTimerId";
    public static final String BODY = "Hello";
    @Override
    public void configure() throws Exception {
        from("timer:simpletimer?period={{simple.timer.period}}") // https://camel.apache.org/components/next/timer-component.html
                .routeId(TIMER_ID)
                .setBody(constant(BODY))
                .log(LoggingLevel.INFO, "Timer: ${body}");
    }
}
