package service;

import Exchanges.BinanceExchange;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/services")
public class IndicatorApplication extends Application {

    private Set<Object> singletons = new HashSet<>();
    private Set<Class<?>> classes = new HashSet<>();

    public IndicatorApplication() {
        classes.add(technical_indicators.class);
        classes.add(TestResource.class);
        singletons.add(BinanceExchange.getInstance());
    }

    @Override
    public Set<Object> getSingletons() {
        return singletons;
    }

    @Override
    public Set<Class<?>> getClasses() {
        return classes;
    }

}
