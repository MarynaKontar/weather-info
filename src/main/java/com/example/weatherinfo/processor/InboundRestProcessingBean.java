package com.example.weatherinfo.processor;

import com.example.weatherinfo.output.entity.OutBoundWeatherInfo;
import org.apache.camel.Exchange;

public class InboundRestProcessingBean {
    public  void validate(Exchange exchange)
    {
        OutBoundWeatherInfo weatherInfo = exchange.getMessage().getBody(OutBoundWeatherInfo.class);
        exchange.setProperty("city", weatherInfo.getAddress().getCity());
    }

}
