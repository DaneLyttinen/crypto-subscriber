package service;

import Exchanges.BinanceExchange;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import domain.bollingerBands;
import org.ta4j.core.BaseBarSeries;

@Path("/technical_indicators")
public class technical_indicators {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/bollingerBands")
    public Response createBollinger(@PathParam("coin") String coin, @PathParam("interval") String interval, @PathParam("sma_interval") int sma_interval, @PathParam("sd_interval") int sd_interval){
        BinanceExchange instance = BinanceExchange.getInstance();
        BaseBarSeries series = instance.getMarketData(coin, interval);
        bollingerBands bollingerBands = new bollingerBands(sma_interval, sd_interval);
        bollingerBands.createBollinger(series);
        //instance.webSocketClient.onCandlestickEvent(coin, instance.Interval, response -> (response));
        return Response.ok().entity("Service online").build();
    }

}
