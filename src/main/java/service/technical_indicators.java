package service;

import Exchanges.BinanceExchange;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import domain.bollingerBands;
import dto.indicator_requestDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.BaseBarSeries;

@Path("/technical_indicators")
public class technical_indicators {

    private static Logger LOGGER = LoggerFactory.getLogger(technical_indicators.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/bollingerBands/{sma_interval}/{sd_interval}")
    public Response createBollinger(indicator_requestDTO IndicatorRequest, @PathParam("sma_interval") int sma_interval, @PathParam("sd_interval") int sd_interval){
        BinanceExchange instance = BinanceExchange.getInstance();
        BaseBarSeries series = instance.getMarketData(IndicatorRequest.getInterval(), IndicatorRequest.getCoin());
        bollingerBands bollingerBands = new bollingerBands(sma_interval, sd_interval, series);
        bollingerBands.createBollinger();
        instance.webSocketClient.onCandlestickEvent(IndicatorRequest.getCoin(), instance.Interval, response -> bollingerBands.handleSocketEvent(response));
        return Response.ok().entity("Service online").build();
    }

}
