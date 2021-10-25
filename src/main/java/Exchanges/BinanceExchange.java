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
    public static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    public static BinanceApiRestClient apiRestClient = factory.newRestClient();
    public static BarSeries series = new BaseBarSeries("my_live_series" );
    public static long last_unix;
    public static BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
    CandlestickInterval Interval;

    @Override
    public void getMarketData(String interval, String coin) {
        Interval = new interval(interval).getInterval();
        List<Candlestick> candlesticks = apiRestClient.getCandlestickBars(coin, Interval).subList(300,500);
        int j = 0;
        for (Candlestick bar:candlesticks) {
            if(j++ == candlesticks.size() - 1){
                last_unix = bar.getCloseTime();
            }
            Instant i = Instant.ofEpochSecond(bar.getCloseTime());
            ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
            series.addBar(z, bar.getOpen(), bar.getHigh(), bar.getLow(), bar.getClose(), bar.getVolume());
        }
    }

    @Override
    public void startListener(String interval, String coin) {
        webSocketClient.onCandlestickEvent(coin, Interval, response -> handleSocketEvent(response));
    }


    public void handleSocketEvent(CandlestickEvent res) {

    }
}
