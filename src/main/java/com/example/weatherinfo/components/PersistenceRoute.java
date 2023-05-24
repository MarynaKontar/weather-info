package com.example.weatherinfo.components;

import com.example.weatherinfo.output.entity.Operation;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;


/**
 * http://www.masterspringboot.com/camel/building-a-camel-route-to-jpa/
 * https://github.com/fmarchioni/masterspringboot/tree/master/camel/camel-jpa-springboot
 */

/**
 * Read json from file and write its data to DB
 */
@Component
public class PersistenceRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("file:src/main/resources/data?noop=true&idempotent=true") // noop=true - operation.json file doesn't deleted from src/main/resources/data
                .routeId("fromJsonToDB")
                .unmarshal().json(JsonLibrary.Jackson, Operation.class)// map json value from operation.json file to the Operation bean
                .log("From file to db: ${body}")
                .to("jpa:" + Operation.class)// + "?persistenceUnit=postgresql&flushOnSend=true"
                .end();

//        from("jpa:" + Operation.class + "?persistenceUnit=postgresql&consumeDelete=false")
//                .routeId("readFromPostgres")
//                .process(exchange -> {
//                    Operation operation = exchange.getMessage().getBody(Operation.class );
//                    Double amount = operation.getAmount();
//                    exchange.getIn().setHeader("amount", amount);
//                })
//                .log("Amount: ${headers.amount}")
//                // .log("Body: ${body}")
//                .end();

    }
}