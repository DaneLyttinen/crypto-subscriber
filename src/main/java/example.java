import com.binance.api.client.BinanceApiClientFactory;
import com.binance.api.client.BinanceApiRestClient;
import com.binance.api.client.BinanceApiWebSocketClient;
import com.binance.api.client.domain.event.CandlestickEvent;
import com.binance.api.client.domain.market.Candlestick;
import com.binance.api.client.domain.market.CandlestickInterval;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.ClosePriceIndicator;
import org.ta4j.core.indicators.helpers.TypicalPriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;

public class example {
    public static BinanceApiWebSocketClient webSocketClient = BinanceApiClientFactory.newInstance().newWebSocketClient();
    public static BinanceApiClientFactory factory = BinanceApiClientFactory.newInstance();
    public static BinanceApiRestClient apiRestClient = factory.newRestClient();
    public static BarSeries series = new BaseBarSeries("my_live_series" );
    public static long last_unix;

    public static void main(String[] args){
        List<Candlestick> candlesticks = apiRestClient.getCandlestickBars("ETHBUSD", CandlestickInterval.FIFTEEN_MINUTES).subList(300,500);
        int j = 0;
        for (Candlestick bar:candlesticks) {
            if(j++ == candlesticks.size() - 1){
                last_unix = bar.getCloseTime();
            }
            Instant i = Instant.ofEpochSecond(bar.getCloseTime());
            ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
            series.addBar(z, bar.getOpen(), bar.getHigh(), bar.getLow(), bar.getClose(), bar.getVolume());
        }
        webSocketClient.onCandlestickEvent("ethbusd", CandlestickInterval.FIFTEEN_MINUTES, response -> handleSocketEvent(response));
    }

    public static void handleSocketEvent(CandlestickEvent res){
        Instant i = Instant.ofEpochSecond(res.getCloseTime());
        ZonedDateTime z = ZonedDateTime.ofInstant(i, ZoneOffset.UTC);
        BaseBar bar = BaseBar.builder(DecimalNum::valueOf, Number.class)
                .timePeriod(Duration.ofMinutes(15))
                .endTime(z)
                .openPrice(Double.parseDouble(res.getOpen()))
                .highPrice(Double.parseDouble(res.getOpen()))
                .lowPrice(Double.parseDouble(res.getLow()))
                .closePrice(Double.parseDouble(res.getClose()))
                .volume(Double.parseDouble(res.getVolume()))
                .build();
        System.out.println(series.getLastBar());
        if (res.getCloseTime() == last_unix){
            series.addBar(bar,true);
            createBollinger();
        }
        else{
            series.addBar(bar);
            last_unix = res.getCloseTime();
            series = series.getSubSeries(1, 201);
        }
        System.out.println(series.getBarCount());
    }

    public static void createBollinger(){
        TypicalPriceIndicator closePrice = new TypicalPriceIndicator(series);
        int last = series.getBarCount()-1;
        SMAIndicator sma = new SMAIndicator(closePrice, 20);
        StandardDeviationIndicator sd20 = new StandardDeviationIndicator(closePrice, 20);

        BollingerBandsMiddleIndicator bbmSMA = new BollingerBandsMiddleIndicator(sma);
        BollingerBandsUpperIndicator bbmUpper = new BollingerBandsUpperIndicator(bbmSMA, sd20);
        BollingerBandsLowerIndicator bbmLower = new BollingerBandsLowerIndicator(bbmSMA, sd20);
        System.out.println(bbmSMA.getValue(last));
        System.out.println(bbmUpper.getValue(last));
        System.out.println(bbmLower.getValue(last));
    }
}
