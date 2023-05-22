package com.example.weatherinfo.components;

import org.apache.camel.CamelContext;
import org.apache.camel.EndpointInject;
import org.apache.camel.ExchangePattern;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.apache.camel.test.spring.junit5.UseAdviceWith;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static com.example.weatherinfo.components.LegacyFileRoute.LEGACY_FILE_ROUTE_ID;
import static org.junit.jupiter.api.Assertions.*;

@CamelSpringBootTest
@SpringBootTest
@UseAdviceWith
//@ActiveProfiles("test")
//@SpringBootApplication
//@MockEndpoints("direct:end")
class LegacyFileRouteTest
{
    @Autowired
    CamelContext camelContext;

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:direct:end")
    protected MockEndpoint mockEndpoint;

    @AfterEach
    void tearDown()
    {
        // move "inputFile.csv" back to input folder
    }

    /**
     * Move real file from data/input to data/output
     * Don't use this test !!!
     * @throws Exception
     */
//    @Test
//    void fileMoveTest() throws Exception
//    {
//        String expectedBody = "Mocked input file content";
//        mockEndpoint.expectedBodiesReceived(expectedBody);
//        mockEndpoint.expectedMinimumMessageCount(1);
//
//        // .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}")
//        // from LegacyFileRoute will be replaced by .to(mockEndpoint)
//        RouteDefinition routeDefinition = AdviceWith.adviceWith(camelContext, LEGACY_FILE_ROUTE_ID, routeBuilder -> {
//            routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
//        });
//
//        camelContext.start();
//        mockEndpoint.assertIsSatisfied();
//    }

//    @Test
//    void fileMoveByMockingFromEndpointTest() throws Exception
//    {
//        String expectedBody = "OutBoundUserInfo{additionalInfo='additionalInfo', address=Address{windDirection='NORTH', city=' Kovacova', province=' Zvolen', zip=' 96237'}} \n " +
//                "OutBoundUserInfo{name='Baska', address=Address{windDirection='NORTH', city=' Sielnica', province=' Banska Bystrica', zip=' 96232'}}";
//        mockEndpoint.expectedBodiesReceived(expectedBody);
//        mockEndpoint.expectedMinimumMessageCount(1);
//
//        // LegacyFileRoute:  .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}") will be replaced by .to(mockEndpoint)
//        // LegacyFileRoute:  from("file:{{legacy.from.location}}?fileName={{legacy.input.file}}") will be replaced by replaceFromWith("direct:mockStartt")
//        AdviceWith.adviceWith(camelContext, LEGACY_FILE_ROUTE_ID, routeBuilder -> {
//           routeBuilder.replaceFromWith("direct:start");
//           routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
//        });
//
//        camelContext.start();
//        producerTemplate.sendBody("direct:start","name, wind_direction, city, province, zip \n Maryna, NORTH, Kovacova, Zvolen, 96237 \n " +
//                "Baska, NORTH, Sielnica, Banska Bystrica, 96232"); // https://camel.apache.org/manual/producertemplate.html
////        producerTemplate.sendBody("direct:start","additionalInfo, wind_direction, city, province, zip \n Maryna, NORTH, Kovacova, Zvolen, 96237"); // https://camel.apache.org/manual/producertemplate.html
//        mockEndpoint.assertIsSatisfied();
//    }

    @Test
    void fileMoveByMockingFromEndpointTest() throws Exception
    {
        String expectedBody = "OutBoundUserInfo{additionalInfo='Maryna', address=Address{windDirection='NORTH', city=' Kovacova', province=' Zvolen', zip=' 96237'}}";
        mockEndpoint.expectedBodiesReceived(expectedBody);
        mockEndpoint.expectedMinimumMessageCount(1);

        // LegacyFileRoute:  .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}") will be replaced by .to(mockEndpoint)
        // LegacyFileRoute:  from("file:{{legacy.from.location}}?fileName={{legacy.input.file}}") will be replaced by replaceFromWith("direct:mockStartt")
        AdviceWith.adviceWith(camelContext, LEGACY_FILE_ROUTE_ID, routeBuilder -> {
            routeBuilder.replaceFromWith("direct:start");
            routeBuilder.weaveByToUri("file:*").replace().to(mockEndpoint);
        });

        camelContext.start();
        producerTemplate.sendBody("direct:start","additionalInfo, wind_direction, city, province, zip \n additionalInfo1, NORTH, Kovacova, Zvolen, 96237"); // https://camel.apache.org/manual/producertemplate.html
        mockEndpoint.assertIsSatisfied();
    }
}