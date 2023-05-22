package com.example.weatherinfo.processor;

import com.example.weatherinfo.components.LegacyFileRoute;
import com.example.weatherinfo.output.entity.Weather;
import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import com.example.weatherinfo.output.entity.WindDirection;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InboundMessageProcessor implements Processor {
    private static final Logger LOGGER = LoggerFactory.getLogger(LegacyFileRoute.class);

    /**
     * Could be written inline:
     * .process(exchange -> {
     *                     UserInfo fileData = exchange.getIn().getBody(UserInfo.class);
     *                     LOGGER.info("This is the read FileData: {}", fileData);
     *                 })
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // fileData is exchange body
        Weather fileData = exchange.getIn().getBody(Weather.class);

        // if wants transform exchange body, need to set it explicitly
        exchange.getMessage().setBody(new OutBoundWeatherInfo(fileData.getAdditionalInfo(), fileData.getWindDirection(), fileData.getAddress()));
    }
}
