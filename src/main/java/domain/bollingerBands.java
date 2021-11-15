package domain;

import com.binance.api.client.domain.event.CandlestickEvent;
import com.google.api.core.ApiFuture;
import com.google.cloud.pubsub.v1.Publisher;
import com.google.protobuf.ByteString;
import com.google.pubsub.v1.PubsubMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.ta4j.core.Bar;
import org.ta4j.core.BaseBar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.TypicalPriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;
import org.ta4j.core.num.DecimalNum;
import service.technical_indicators;

import java.time.Duration;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.concurrent.ExecutionException;

public class bollingerBands {
    private int sma_interval;
    private int sd_interval;
    private BollingerBandsMiddleIndicator bbmSMA;
    private BollingerBandsUpperIndicator bbmUpper;
    private BollingerBandsLowerIndicator bbmLower;
    private long last_unix;
    private Bar last_bar;
    private BaseBarSeries series;
    private Publisher publisher;
    //private BaseBarSeries series;

    private static Logger LOGGER = LoggerFactory.getLogger(bollingerBands.class);

    public bollingerBands(int sma_interval, int sd_interval, BaseBarSeries series, Publisher publisher){
        this.sma_interval = sma_interval;
        this.sd_interval = sd_interval;
        this.series = series;
        this.publisher = publisher;
    }

    public void handleSocketEvent(CandlestickEvent res){
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
    }

    public void createBollinger() {
        TypicalPriceIndicator closePrice = new TypicalPriceIndicator(series);
        int last = series.getBarCount()-1;
        SMAIndicator sma = new SMAIndicator(closePrice, sma_interval);
        StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, sd_interval);

        System.out.println(last);
        bbmSMA = new BollingerBandsMiddleIndicator(sma);
        bbmUpper = new BollingerBandsUpperIndicator(bbmSMA, sd);
        bbmLower = new BollingerBandsLowerIndicator(bbmSMA, sd);
        LOGGER.info("bbm SMA = "+ bbmSMA.getValue(last));
        LOGGER.info("bbm UPPER = "+ bbmUpper.getValue(last));
        LOGGER.info("bbm LOWER = "+ bbmLower.getValue(last));
        last_bar = series.getLastBar();
        publish(last);
    }

    public void publish(int last) {
        float close = last_bar.getClosePrice().floatValue();
        String message = null;
        if (close >= bbmUpper.getValue(last).floatValue()){
            //publish alert of overbuying, potential sell
            message = "Potential sell";
            System.out.println("potential sell");
        }
        if (close <= bbmLower.getValue(last).floatValue()){
            //publish alert of overselling, potential buy
            message = "Potential buy";
        }
        if (message != null){
            ByteString data = ByteString.copyFromUtf8(message);
            PubsubMessage pubsubMessage = PubsubMessage.newBuilder().setData(data).build();
            ApiFuture<String> messageIdFuture = publisher.publish(pubsubMessage);
        }
    }

}
