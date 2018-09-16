package Data;

/**
 * Created by Asus on 21/5/2561.
 */

public class ReservData {
    private String re_id, round_id, phone, reserv_date, travel_date, pm_status, pm_method,
                    route, time, price, van_id;

    public ReservData(String prmRe_ID, String prmRoundID, String prmPhone, String prmReservDate,
                      String prmTravelDate, String prmPMStatus, String prmPMMethod,
                     String prmRoute, String prmTime, String prmPrice, String prmVanID) {
        this.re_id = prmRe_ID;
        this.round_id = prmRoundID;
        this.phone = prmPhone;
        this.reserv_date = prmReservDate;
        this.travel_date = prmTravelDate;
        this.pm_status = prmPMStatus;
        this.pm_method = prmPMMethod;
        this.route = prmRoute;
        this.time = prmTime;
        this.price = prmPrice;
        this.van_id = prmVanID;
    }

    public String getRe_id() {return re_id;}
    public String getRound_id() { return round_id; }
    public String getId_card() { return phone; }
    public String getReserv_date() { return reserv_date; }
    public String getTravel_date(){ return travel_date; }
    public String getPm_status() { return pm_status; }
    public String getPm_method() {return pm_method;}
    public String getRoute() { return route; }
    public String getTime() { return time; }
    public String getPrice() { return price; }
    public String getVan_id() { return van_id; }
}
