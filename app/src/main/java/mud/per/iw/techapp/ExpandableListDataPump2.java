package mud.per.iw.techapp;



import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump2 {
    public static HashMap<String, List<String>> getData(String getvisituid, List<Products> getspsdata) {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();
        List<String> cricket=null;
        String vtsdes = "";
        if(getspsdata!=null) {
            for (int i = 0; i < getspsdata.size(); i++) {
                cricket = new ArrayList<String>();
                vtsdes = getspsdata.get(i).getpdesc();

                Log.wtf("jkl", vtsdes);
                expandableListDetail.put(vtsdes, cricket);
            }
        }






        return expandableListDetail;
    }
}
