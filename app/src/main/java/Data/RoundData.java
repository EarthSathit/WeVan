package Data;

/**
 * Created by Asus on 16/5/2561.
 */

public class RoundData {
    private String round_id, route_id, route, price, time_route, time, van_id, seat;

    public RoundData(String prmRoundID, String prmRouteID, String prmRoute, String prmPrice,
                     String prmTimeRoute, String prmTime, String prmVanID, String prmSeat) {
        this.round_id = prmRoundID;
        this.route_id = prmRouteID;
        this.route = prmRoute;
        this.price = prmPrice;
        this.time_route = prmTimeRoute;
        this.time = prmTime;
        this.van_id = prmVanID;
        this.seat = prmSeat;
    }

    public String getRound_id() { return round_id; }
    public String getRoute_id() { return route_id; }
    public String getRoute() { return route; }
    public String getPrice() { return price; }
    public String getTime_route() { return time_route; }
    public String getTime() { return time; }
    public String getVan_id() { return van_id; }
    public String getSeat() {return seat;}
}
