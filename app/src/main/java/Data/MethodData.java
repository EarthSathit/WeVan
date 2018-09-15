package Data;

/**
 * Created by Asus on 18/5/2561.
 */

public class MethodData {
    String pm_id, pm_name;

    public MethodData(String prmPM_id, String prmPM_name){
        this.pm_id = prmPM_id;
        this.pm_name = prmPM_name;
    }

    public String getPm_id(){ return pm_id;}
    public String getPm_name(){return pm_name;}
}
