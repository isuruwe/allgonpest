package mud.per.iw.techapp;



import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData(String getvisituid, List<Species> getspsdata) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> cricket=null;
String vtsdes = "";
        for (int i = 0; i <getspsdata.size(); i++) {
            cricket = new ArrayList<String>();
            vtsdes="Species:"+getspsdata.get(i).getspdesc();
            cricket.add("Species:"+getspsdata.get(i).getspdesc());
            cricket.add("Age:"+getspsdata.get(i).getage());
            cricket.add("Breed:"+getspsdata.get(i).getbreed());
            cricket.add("Average Consumed:"+getspsdata.get(i).getavgcns());
            cricket.add("Dead Presence:"+getspsdata.get(i).getdeadpr());
            cricket.add("Count:"+getspsdata.get(i).getcnt());
            Log.wtf("jkl",vtsdes);
            expandableListDetail.put(vtsdes, cricket);
        }







        return expandableListDetail;
    }
}
