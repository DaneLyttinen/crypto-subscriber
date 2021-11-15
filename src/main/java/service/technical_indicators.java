package service;

import Exchanges.BinanceExchange;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import cloud.CreatePublisher;
import cloud.CreateTopic;
import com.google.cloud.pubsub.v1.Publisher;
import domain.bollingerBands;
import dto.indicator_requestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BaseBarSeries;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.concurrent.ExecutionException;

@Path("/technical_indicators")
public class technical_indicators {

    private static Logger LOGGER = LoggerFactory.getLogger(technical_indicators.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/bollingerBands/{sma_interval}/{sd_interval}")
    public Response createBollinger(indicator_requestDTO IndicatorRequest, @PathParam("sma_interval") int sma_interval, @PathParam("sd_interval") int sd_interval) throws IOException, ExecutionException, InterruptedException {
        BinanceExchange instance = BinanceExchange.getInstance();
        // In production this will only be reached if topic does not yet exist, error handling for future in case 2 concurrent users want to create same publisher will be needed
        //CreateTopic.createTopic("pubsub-331221", "binance-bollinger-"+IndicatorRequest.getInterval());
        Publisher publisher = CreatePublisher.publisher("pubsub-331221", "binance-bollinger-"+IndicatorRequest.getInterval());
        BaseBarSeries series = instance.getMarketData(IndicatorRequest.getInterval(), IndicatorRequest.getCoin());
        bollingerBands bollingerBands = new bollingerBands(sma_interval, sd_interval, series, publisher);
        bollingerBands.createBollinger();
        instance.webSocketClient.onCandlestickEvent(IndicatorRequest.getCoin(), instance.Interval, response -> bollingerBands.handleSocketEvent(response));
        return Response.ok().entity("Service online").build();
    }

}
