package dto;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class indicator_requestDTO {
    private String coin;
    private String interval;

    public indicator_requestDTO(){

    }

    public indicator_requestDTO(String coin,String interval){
        this.coin = coin;
        this.interval = interval;
    }

    public String getInterval() {
        return interval;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;

        if (o == null || getClass() != o.getClass()) return false;

        indicator_requestDTO userDTO = (indicator_requestDTO) o;

        return new EqualsBuilder()
                .append(coin, userDTO.coin)
                .append(interval, userDTO.interval)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37)
                .append(coin)
                .append(interval)
                .toHashCode();
    }

    @Override
    public String toString() {
        return "indicator_requestDTO{" +
                "coin='" + coin + '\'' +
                ",interval='" + interval + '\'' +
                '}';
    }
}
