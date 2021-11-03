package Exchanges;

import com.binance.api.client.domain.market.CandlestickInterval;

public class interval {
    private CandlestickInterval Interval;

    public interval(String interval){
        switch(interval){
            case("1m"):
                Interval = CandlestickInterval.ONE_MINUTE;
                break;
            case("5m"):
                Interval = CandlestickInterval.FIVE_MINUTES;
                break;
            case("15m"):
                Interval = CandlestickInterval.FIFTEEN_MINUTES;
                break;
            case("30m"):
                Interval = CandlestickInterval.HALF_HOURLY;
                break;
            case("1h"):
                Interval = CandlestickInterval.HOURLY;
                break;
            case("4h"):
                Interval = CandlestickInterval.FOUR_HORLY;
                break;
            case("6h"):
                Interval = CandlestickInterval.SIX_HOURLY;
                break;
            case("8h"):
                Interval = CandlestickInterval.EIGHT_HOURLY;
                break;
            case("12h"):
                Interval = CandlestickInterval.TWELVE_HOURLY;
                break;
            case("d"):
                Interval = CandlestickInterval.DAILY;
                break;
            case("w"):
                Interval = CandlestickInterval.WEEKLY;
                break;
            case("m"):
                Interval = CandlestickInterval.MONTHLY;
                break;
        }
    }
    public CandlestickInterval getInterval(){
        return Interval;
    }
}
