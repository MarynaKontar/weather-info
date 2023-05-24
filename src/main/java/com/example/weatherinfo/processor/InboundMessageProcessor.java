package com.example.weatherinfo.processor;

import com.example.weatherinfo.output.entity.Weather;
import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class InboundMessageProcessor implements Processor {

    /**
     * Could be written inline:
     * .process(exchange -> {
     *                     Weather fileData = exchange.getIn().getBody(Weather.class);
     *                     LOGGER.info("This is the read FileData: {}", fileData);
     *                 })
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // fileData is exchange body
        Weather fileData = exchange.getMessage().getBody(Weather.class);
        System.out.println(fileData);
        if(fileData != null)
        {
            // if wants transform exchange body, need to set it explicitly
            exchange.getMessage().setBody(new OutBoundWeatherInfo(fileData.getAdditionalInfo(), fileData.getWindDirection(), fileData.getAddress()));
        }

    }
}
