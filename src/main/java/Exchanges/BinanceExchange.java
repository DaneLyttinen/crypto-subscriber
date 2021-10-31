package Exchanges;

import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class BinanceExchange implements  Exchange {
    private BinanceApiClientFactory factory;
    private BinanceApiRestClient apiRestClient;
    private long last_unix;
    public CandlestickInterval Interval;
    public BinanceApiWebSocketClient webSocketClient;
    private static BinanceExchange instance;

    private BinanceExchange(){
        webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
        factory = BinanceApiClientFactory.newInstance();
        apiRestClient = factory.newRestClient();
    }

    public static synchronized BinanceExchange getInstance(){
        if (instance == null){
            instance = new BinanceExchange();
        }
        return instance;
    }

    /// getCandleStickBars seems to return 25 candlesticks instead of the default 500?
    @Override
    public BaseBarSeries getMarketData(String interval, String coin) {
        Interval = new interval(interval).getInterval();

        List<Candlestick> candlesticks = apiRestClient.getCandlestickBars(coin, Interval);
        //int size = candlesticks.size();
        //candlesticks = candlesticks.subList(size-200,size);
        BaseBarSeries series = new BaseBarSeries("my_live_series" );
        int j = 0;
        for (Candlestick bar:candlesticks) {
            if(j++ == candlesticks.size() - 1){
                last_unix = bar.getCloseTime();
            }
            Instant i = Instant.ofEpochSecond(bar.getCloseTime());
            ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
            series.addBar(z, bar.getOpen(), bar.getHigh(), bar.getLow(), bar.getClose(), bar.getVolume());
        }
        return series;
    }

    @Override
    public void startListener(String interval, String coin) {
        CandlestickInterval Interval = new interval(interval).getInterval();
        webSocketClient.onCandlestickEvent(coin, Interval, this::handleSocketEvent);
    }


    public void handleSocketEvent(CandlestickEvent res) {
        System.out.println(res.getClose());
    }
}
