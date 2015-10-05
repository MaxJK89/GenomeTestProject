package quant.cann.genometestproject.data;

/**
 * Created by Max Kleine on 10/1/2015.
 */
public class SimplePlaceData {

    public String sPlaceName;
    public String sPlaceID;
    public String sIconURL;
    public double lat;
    public double lng;

    public SimplePlaceData(String sPlaceName, String sPlaceID, String sIconURL,
                           double lat, double lng) {
        this.sPlaceName = sPlaceName;
        this.sPlaceID = sPlaceID;
        this.sIconURL = sIconURL;
        this.lat = lat;
        this.lng = lng;
    }

    public String getsPlaceName() {
        return sPlaceName;
    }


    public void setsPlaceName(String sPlaceName) {
        this.sPlaceName = sPlaceName;
    }

    public String getsIconURL() {
        return sIconURL;
    }

    public void setsIconURL(String sIconURL) {
        this.sIconURL = sIconURL;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public String getsPlaceID() {
        return sPlaceID;

    }

    public void setsPlaceID(String sPlaceID) {
        this.sPlaceID = sPlaceID;
    }

}
