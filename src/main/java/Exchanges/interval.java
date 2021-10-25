package Exchanges;

import com.binance.api.client.domain.market.CandlestickInterval;

public class interval {
    private CandlestickInterval Interval;

    public interval(String interval){
        switch(interval){
            case ("1m"):
                Interval = CandlestickInterval.ONE_MINUTE;
            case("5m"):
                Interval = CandlestickInterval.FIVE_MINUTES;
            case("15m"):
                Interval = CandlestickInterval.FIFTEEN_MINUTES;
            case("30m"):
                Interval = CandlestickInterval.HALF_HOURLY;
            case("1h"):
                Interval = CandlestickInterval.HOURLY;
            case("4h"):
                Interval = CandlestickInterval.FOUR_HORLY;
            case("6h"):
                Interval = CandlestickInterval.SIX_HOURLY;
            case("8h"):
                Interval = CandlestickInterval.EIGHT_HOURLY;
            case("12h"):
                Interval = CandlestickInterval.TWELVE_HOURLY;
            case("d"):
                Interval = CandlestickInterval.DAILY;
            case("w"):
                Interval = CandlestickInterval.WEEKLY;
            case("m"):
                Interval = CandlestickInterval.MONTHLY;
        }
    }
    public CandlestickInterval getInterval(){
        return Interval;
    }
}
