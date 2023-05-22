package com.example.weatherinfo.components;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.example.weatherinfo.components.SimpleTimer.BODY;
import static com.example.weatherinfo.components.SimpleTimer.TIMER_ID;
import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
class SimpleTimerTest {

    @Autowired
    CamelContext camelContext;

    // mocked endpoint that we add at the end of rought
    @EndpointInject("mock:result")
    protected MockEndpoint mockEndpoint;

    @BeforeEach
    void setUp() {
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void configure() {

    }

    @Test
    void simpleTimerTest() throws Exception
    {
        String expectedBody = BODY;
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);

        AdviceWith.adviceWith(camelContext, TIMER_ID, routeBuilder -> {
           routeBuilder.weaveAddLast().to(mockEndpoint);
        });

        camelContext.start();
        mockEndpoint.assertIsSatisfied();
    }
}