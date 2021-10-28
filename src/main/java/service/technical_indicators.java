package service;

import Exchanges.BinanceExchange;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Response;

@Path("/technical_indicators")
public class technical_indicators {

    @GET
    @Path("/bollingerBands")
    public Response createBollinger(){
        //BinanceExchange binanceExchange = new BinanceExchange();
       // binanceExchange.getMarketData("15m", "ethbusd");
        //binanceExchange.startListener("15m", "ethbusd");
        return Response.ok().entity("Service online").build();
    }

}
