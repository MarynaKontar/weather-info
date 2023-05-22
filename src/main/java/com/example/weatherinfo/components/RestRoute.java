package com.example.weatherinfo.components;

import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import com.example.weatherinfo.output.entity.Weather;
import com.example.weatherinfo.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class RestRoute extends RouteBuilder
{

    @Override
    public void configure() throws Exception {
        restConfiguration()
                .component("jetty")
                .host("localhost")
                .port(8080)
                .bindingMode(RestBindingMode.json)
                .enableCORS(true);

        rest("api/weather")
                .id("postWeather")
                .produces("application/json")
                .post("weatherInfo").type(Weather.class).route()
                .process(new InboundMessageProcessor()) // add interceptor that could do smth with posted data (transform it)
                .log(LoggingLevel.INFO, "From REST to db: ${body}")
                // .to("jpa:com.example.weatherinfo.output.entity.UserInfo?persistenceUnit=postgresql&flushOnSend=true")
                .to("jpa:" +OutBoundWeatherInfo.class.getName() + "?persistenceUnit=postgresql&flushOnSend=true")
                .convertBodyTo(String.class) // work the same as  exchange.getIn().setBody(fileData.toString()); can't save to file if exchange body isn't string
                .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}&fileExist=append&appendChars=\n"); // add &fileExist=append&appendChars=\n because in other way it will be recorded only the last row

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
