package domain;

import org.ta4j.core.Bar;
import org.ta4j.core.BaseBarSeries;
import org.ta4j.core.indicators.SMAIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsLowerIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsMiddleIndicator;
import org.ta4j.core.indicators.bollinger.BollingerBandsUpperIndicator;
import org.ta4j.core.indicators.helpers.TypicalPriceIndicator;
import org.ta4j.core.indicators.statistics.StandardDeviationIndicator;

public class bollingerBands {
    private int sma_interval;
    private int sd_interval;
    private BollingerBandsMiddleIndicator bbmSMA;
    private BollingerBandsUpperIndicator bbmUpper;
    private BollingerBandsLowerIndicator bbmLower;
    private long last_unix;
    private Bar last_bar;
    //private BaseBarSeries series;

    public bollingerBands(int sma_interval, int sd_interval){
        this.sma_interval = sma_interval;
        this.sd_interval = sd_interval;
    }

    public void createBollinger(BaseBarSeries series){
        TypicalPriceIndicator closePrice = new TypicalPriceIndicator(series);
        int last = series.getBarCount()-1;
        SMAIndicator sma = new SMAIndicator(closePrice, sma_interval);
        StandardDeviationIndicator sd = new StandardDeviationIndicator(closePrice, sd_interval);

        bbmSMA = new BollingerBandsMiddleIndicator(sma);
        bbmUpper = new BollingerBandsUpperIndicator(bbmSMA, sd);
        bbmLower = new BollingerBandsLowerIndicator(bbmSMA, sd);
        last_bar = series.getLastBar();
        publish(last);
    }

    public void publish(int last){
        float close = last_bar.getClosePrice().floatValue();

        if (close >= bbmUpper.getValue(last).floatValue()){
            //publish alert of overbuying, potential sell
            System.out.println("potential sell");
        }
        if (close <= bbmLower.getValue(last).floatValue()){
            //publish alert of overselling, potential buy
            System.out.println("potential buy");
        }
    }

}
