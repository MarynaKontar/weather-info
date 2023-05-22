package com.example.weatherinfo.components;

import com.example.weatherinfo.output.entity.Weather;
import com.example.weatherinfo.processor.InboundMessageProcessor;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.dataformat.beanio.BeanIODataFormat;
import org.apache.camel.dataformat.bindy.csv.BindyCsvDataFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LegacyFileRoute extends RouteBuilder {
    public static final String LEGACY_FILE_ROUTE_ID = "legacyFileRouteId";
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyFileRoute.class);

    // legacy binding
    private BeanIODataFormat inboundDataFormat = new BeanIODataFormat("inboundMessageBeanIOMapping.xml", "inputMessageStream");
    BindyCsvDataFormat bindyDataFormat = new BindyCsvDataFormat(Weather.class);

    @Override
    public void configure() throws Exception {
        // without "split" there will be RouteDefinition type
       from("file:{{legacy.from.location}}?fileName={{legacy.input.file}}")
                .routeId(LEGACY_FILE_ROUTE_ID)
                .split(body().tokenize("\n", 1, true))// split file body by token (token "\n") to 2 rows (group=2) and skip the first row
                // .unmarshal(inboundDataFormat) // map splits to rows body to the bean described in "inputMessageStream" of "inboundMessageBeanIOMapping.xml"
                .unmarshal(bindyDataFormat) // map splits to rows body to the UserInfo bean
                .process(new InboundMessageProcessor()) // add interceptor that could do smth with "from" data (log it)
                .log(LoggingLevel.INFO, "From file to file: ${body}")
                .convertBodyTo(String.class) // work the same as  exchange.getIn().setBody(fileData.toString()); can't save to file if exchange body isn't string
                .to("file:{{legacy.to.location}}?fileName={{legacy.output.file}}&fileExist=append&appendChars=\n") // add &fileExist=append&appendChars=\n because in other way it will be recorded only the last row
                .end();
        System.out.println("Just to have debug point");
    }
}
