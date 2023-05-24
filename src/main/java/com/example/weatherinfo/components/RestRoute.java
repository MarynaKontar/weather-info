package com.example.weatherinfo.components;

import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import com.example.weatherinfo.output.entity.Weather;
import com.example.weatherinfo.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.endpoint.EndpointRouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;

// The DSL can be accessed in several ways, but the main one is to switch to using an EndpointRouteBuilder instead of the usual RouteBuilder.
// This builder provides access to all of Camel endpoint builders which are defined through inheritance on the org.apache.camel.builder.endpoint.EndpointRouteBuilder.
/**
 * Consume Rest post request, produce ir to DB, to ActiveMQ queue and topic
 */
@Component
public class RestRoute extends EndpointRouteBuilder
{

    // rest consumer endpoint and an jpa producer (+file producer):
    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("jetty")
                .host("localhost")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        onException(JMSException.class)
                .routeId("jmsExceptionId")
                .handled(true)
                .log(LoggingLevel.WARN, "JMS Exception has occurred. It could be because of ActiveMQ connection lost.");


        // POST request => map json from post to Weather => InboundMessageProcessor (map to OutBoundWeatherInfo)
        // => save to db => send to ActivaMQ queue and topic
        rest("api/weather")
                .id("postWeatherId")
                .produces("application/json")
                .post("weatherInfo").type(Weather.class).route()
                .process(new InboundMessageProcessor()) // add interceptor that could do smth with posted data (transform it)
                .log(LoggingLevel.INFO, "From REST to db: ${body}")
                .to("seda:toDb")
                .convertBodyTo(String.class)
                .wireTap("direct:toActiveMQ")
                .wireTap("direct:toActiveMQTopic")

                // choice
                // .bean(new InboundRestProcessingBean())
                // .choice()
                // .when(simple("${header.city} == 'Kovacova'"))
                //    .to("seda:toDb")
                // .otherwise()
                //    .to("seda:toDb")
                //    .convertBodyTo(String.class)
                //    .wireTap("direct:toActiveMQ")

                // override rest response
                // .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(201))
                // .transform().simple("Message processed and send to queue with body: ${body}")
                .endRest();

        // "direct" endpoint is synchronized, so one endpoint by one;
        // Camel’s Wire Tap will copy the original Exchange and set its Exchange Pattern to InOnly, as we want the tapped Exchange to be sent in a fire and forget style.
        // The tapped Exchange is then sent in a separate thread, so it can run in parallel with the original.
        // The SEDA component provides asynchronous SEDA behavior, so that messages are exchanged on a BlockingQueue and consumers are invoked in a separate thread from the producer.
        from("seda:toDb")
                .routeId("weatherInfoToDbId")
                .to("jpa:" + OutBoundWeatherInfo.class.getName() ); //+ "?persistenceUnit=postgresql&flushOnSend=true"
        from("direct:toActiveMQ")
                .routeId("toActiveMQId")
                .to("activemq:{{activemq.weather.queue}}?exchangePattern=InOnly");
        from("direct:toActiveMQTopic")
                .routeId("toActiveMQTopicId")
                .wireTap("activemq:topic:{{activemq.weather.topic}}?exchangePattern=InOnly");


        // => write to file => delete from db
//        from("timer:readWeatherInfo?period={{readWeatherInfo.timer.period}}")
//                .routeId("readWeatherInfoFromDbId")
//                .to("jpa:" + OutBoundWeatherInfo.class.getName() + "?namedQuery=OutBoundWeatherInfo_fetchAll")
//                .split(body())
//                .process(new OutBoundWeatherInfoProcessor()) // add interceptor that could do smth with posted data (transform it)
//                .log(LoggingLevel.INFO, "Fetch all weather from db: ${body}")
//                .convertBodyTo(String.class) // work the same as  exchange.getIn().setBody(fileData.toString()); can't save to file if exchange body isn't string
//                .to("file:{{rest.to.location}}?fileName={{rest.restDb.file}}&fileExist=append&appendChars=\n") // add &fileExist=append&appendChars=\n because in other way it will be recorded only the last row
//                .toD("jpa:" +OutBoundWeatherInfo.class.getName() + "?nativeQuery=DELETE FROM out_bound_weather_info WHERE id = ${header.consumedId}"); // CONSUMED_ID_KEY; &useExecuteUpdate=true
//                // toD - dynamically changed (${header.consumedId})






//        rest("api/weather")
//                .id("getWeatherById")
//                .get("weatherInfo/{id}").description("Details of an weather by id").outType(Weather.class)
//                .produces("application/json").route()
//                .to("sql:SELECT * FROM out_bound_weather_info wi where wi.id = id")
//                .log(LoggingLevel.INFO, "GET from db: ${body}");
//
//        rest("api/weather")
//                .id("getWeatherList")
//                .get("weatherInfo").description("Details of an weather")
//                .produces("application/json").route()
//                .to("sql: SELECT * FROM out_bound_weather_info")
//                .log(LoggingLevel.INFO, "GET from db: ${body}");

    }
//    @Override
//    public void configure() throws Exception {
//        restConfiguration()
//                .component("jetty")
//                .host("localhost")
//                .port(8080)
//                .bindingMode(RestBindingMode.json)
//                .enableCORS(true);
//
//        rest("api/weather")
//                .id("restRouterId")
//                .produces("application/json")
//                .post("weatherInfo").type(Weather.class).route()
//                .to("direct:process");
//
//        from("direct:process")
//                .routeId("processMessageRouteId")
//                .process(new InboundMessageProcessor()) // add interceptor that could do smth with "from" data (log it)
//                .log(LoggingLevel.INFO, "Transformed Body: ${body}")
//                // .to("jpa:com.example.weatherinfo.output.entity.UserInfo?persistenceUnit=postgresql&flushOnSend=true")
//                .to("jpa:" +OutBoundWeatherInfo.class.getName() + "?persistenceUnit=postgresql&flushOnSend=true")
//                .convertBodyTo(String.class) // work the same as  exchange.getIn().setBody(fileData.toString()); can't save to file if exchange body isn't string
//                .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}&fileExist=append&appendChars=\n"); // add &fileExist=append&appendChars=\n because in other way it will be recorded only the last row
//
//    }
}
