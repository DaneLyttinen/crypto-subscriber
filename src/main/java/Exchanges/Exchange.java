package Exchanges;

import com.binance.api.client.domain.event.CandlestickEvent;
import org.ta4j.core.BarSeries;
import org.ta4j.core.BaseBarSeries;

import java.util.List;

public interface Exchange {
    BaseBarSeries getMarketData(String interval, String coin);

    void startListener(String interval, String coin);
}
