package mud.per.iw.techapp;



public class Products {
    private String puid;
    private  String pdesc;

    public Products() {
    }

    public Products(String puid, String pdesc) {
        this.puid = puid;
        this.pdesc = pdesc;

    }

    public String getpuid() {
        return puid;
    }
    public String getpdesc() {
        return pdesc;
    }



    public void setpuid(String puid) {
        this.puid = puid;
    }
    public void setpdesc(String pdesc) {
        this.pdesc = pdesc;
    }


}