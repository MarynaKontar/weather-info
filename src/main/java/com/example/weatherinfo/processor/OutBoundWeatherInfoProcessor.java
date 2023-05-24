package com.example.weatherinfo.processor;

import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class OutBoundWeatherInfoProcessor implements Processor {
    public static final String CONSUMED_ID_KEY = "consumedId";

    /**
     * Could be written inline:
     * .process(exchange -> {
     *                     OutBoundWeatherInfo fileData = exchange.getMessage().getBody(OutBoundWeatherInfo.class);
     *                     LOGGER.info("This is the read FileData: {}", fileData);
     *                 })
     * @param exchange
     * @throws Exception
     */
    @Override
    public void process(Exchange exchange) throws Exception {
        // fileData is exchange body
        OutBoundWeatherInfo data = exchange.getMessage().getBody(OutBoundWeatherInfo.class);
        if(data != null)
        {
            exchange.getMessage().setHeader(CONSUMED_ID_KEY, data.getId()); // will use it to delete processed value (for example, from db)
        }

    }
}
