package quant.cann.genometestproject;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.adapters.PhotoGalleryRVAdapter;
import quant.cann.genometestproject.adapters.ReviewsRVAdapter;
import quant.cann.genometestproject.data.PhotoGalleryData;
import quant.cann.genometestproject.data.ReviewData;
import quant.cann.genometestproject.fragments.DetailPlaceMapFragment;
import quant.cann.genometestproject.fragments.HoursOfOperationDialogFragment;
import quant.cann.genometestproject.fragments.PlacesNearMeFragment;
import quant.cann.genometestproject.utils.OnSSItemClickListener;
import quant.cann.genometestproject.utils.TypeFaceHelper;

public class DetailsActivity extends AppCompatActivity implements OnMapReadyCallback, View.OnClickListener, OnSSItemClickListener {
    private static final String TAG = "DetailsActivity";
    public static String PLACE_ID = "PlaceId";
    public static int howManyReviews = 0;
    @Bind(R.id.sliding_layout)
    SlidingUpPanelLayout suplSlidinUpLayout;
    @Bind(R.id.place_name_tx)
    TextView txPlaceName;
    @Bind(R.id.place_address)
    TextView txAdress;
    @Bind(R.id.place_website_address)
    TextView txWebsite;
    @Bind(R.id.place_phone_number)
    TextView txPhoneNumber;
    @Bind(R.id.how_many_reviews)
    TextView howManyReviewsTx;
    @Bind(R.id.hop_hours)
    TextView hourOfOperationTx;
    @Bind(R.id.full_details)
    RelativeLayout llFullDetails;
    @Bind(R.id.review_recycler_view_layout)
    RecyclerView fullReviewRecyclerView;
    @Bind(R.id.photo_gallery_recycler_view)
    RecyclerView photoGalleryRecyclerView;
    @Bind(R.id.dragView)
    LinearLayout dragViewLayout;
    @Bind(R.id.noPhotosToShowTx)
    TextView noPhotosToShowTx;
    @Bind(R.id.hop_hours_card)
    CardView hopHoursCard;
    @Bind(R.id.viewOnMapTx)
    TextView viewOnMapTx;
    @Bind(R.id.hours_rl)
    RelativeLayout hoursRl;
    @Bind(R.id.opHrTx)
    TextView opHrTx;
    @Bind(R.id.topBar)
    RelativeLayout topBar;
    List<String> openingDays = new ArrayList<>();


    //    @Bind(R.id.how_many_reviews)TextView howManyReviews;
    String placeName;
    //    private SupportMapFragment fragment;
//    private GoogleMap map;
    String placeId;
    double lat, lng;
    String iconURL;

    ReviewsRVAdapter reviewsRVAdapter;

    List<ReviewData> reviewDataList = new ArrayList<>();
    List<PhotoGalleryData> photoGalleryList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        ButterKnife.bind(this);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            placeId = extras.getString(PlacesNearMeFragment.PLACE_ID);
            lat = extras.getDouble(PlacesNearMeFragment.LAT);
            lng = extras.getDouble(PlacesNearMeFragment.LNG);
            iconURL = extras.getString(PlacesNearMeFragment.ICONURL);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTypeFaces();

        initReviewRecyclerView();
        initPhotoGallery();

        viewOnMapTx.setOnClickListener(this);
        hopHoursCard.setOnClickListener(this);
        hoursRl.setOnClickListener(this);


        suplSlidinUpLayout.setAnchorPoint(0.5f);

        llFullDetails.setVisibility(View.GONE);

//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
//                .findFragmentById(R.id.detailedMap);
//        mapFragment.getMapAsync(this);

        getJSONResonse();

    }

    void setTypeFaces() {
        TypeFaceHelper tf = new TypeFaceHelper(this);
        opHrTx.setTypeface(tf.getOpenSansBold());
        hourOfOperationTx.setTypeface(tf.getOpenSansLight());
        txPlaceName.setTypeface(tf.getOpenSansBold());
        txAdress.setTypeface(tf.getOpenSansRegular());
        txPhoneNumber.setTypeface(tf.getOpenSansLight());
        howManyReviewsTx.setTypeface(tf.getOpenSansLight());


    }

    private void getJSONResonse() {// Tag used to cancel the request
        String tag_json_obj = "json_obj_req2";

        String url = "https://maps.googleapis.com/maps/api/place/details/json?placeid=" + placeId + "&key=AIzaSyB9wM1tT4wr6_kYQfBsUrxIvWOh0kALX6E";

        final ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        llFullDetails.setVisibility(View.GONE);
                        parseJSONResults(response);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Volley Error", "Volley error");
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                // hide the progress dialog
                pDialog.hide();
                llFullDetails.setVisibility(View.GONE);
                showErrorForGettingClosestPlaces();

            }
        });
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(
                1500,
                10,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
// Adding request to request queue
        Global.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);

    }

    // show an error for grabbing closest places
    private void showErrorForGettingClosestPlaces() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                this);
        builder.setCancelable(true);
        builder.setTitle("Error connecting to server");
        builder.setMessage("Would you like to try again?");
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        getJSONResonse();

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                new LatLng(lat, lng), 10));

        // You can customize the marker image using images bundled with
        // your app, or dynamically generated bitmaps.
//        if (getBitmapIconForPlace() != null) {
//            map.addMarker(new MarkerOptions()
//                    .icon(BitmapDescriptorFactory.fromBitmap(getBitmapIconForPlace()))
//                    .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
//                    .position(new LatLng(lat, lng)));
//        } else {
        map.addMarker(new MarkerOptions()
                .icon(BitmapDescriptorFactory
                        .defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                .anchor(0.0f, 1.0f) // Anchors the marker on the bottom left
                .position(new LatLng(lat, lng)));
//        }
    }

    private Bitmap getBitmapIconForPlace() {
        try {
            return Ion.with(getApplicationContext())
                    .load(iconURL).asBitmap().get();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    private void parseJSONResults(JSONObject result) {
        try {
            JSONObject firstJSON = result.getJSONObject("result");

            placeName = firstJSON.getString("name");
            txPlaceName.setText(firstJSON.getString("name"));
            txAdress.setText(firstJSON.getString("formatted_address"));
            if (!firstJSON.isNull("formatted_phone_number")) {
                txPhoneNumber.setText("Phone number\n" + firstJSON.getString("formatted_phone_number"));
            } else {
                txPhoneNumber.setText("No phone number\nfor this place");
            }
            if (!firstJSON.isNull("website")) {
                txWebsite.setText(dependHtmlAddress(firstJSON.getString("website")));
            } else {
                txWebsite.setText("No website\nfor this place");
            }

            getReviews(firstJSON);
            getPhotoGallery(firstJSON);


            if (!firstJSON.isNull("opening_hours")) {
                hourOfOperationTx.setText("Open all hours");
                JSONArray jsoa = firstJSON.getJSONObject("opening_hours").getJSONArray("weekday_text");
                for (int i = 0; i < jsoa.length(); i++) {
                    String j = jsoa.get(i).toString();
                    hourOfOperationTx.setText(j + "...");
                    openingDays.add(j);
                }


            }
            llFullDetails.setVisibility(View.VISIBLE);

        } catch (JSONException ex) {
            Log.e("JSON", ex.getMessage().toString());
        }
        llFullDetails.setVisibility(View.VISIBLE);

    }

    private String dependHtmlAddress(String addy) {
        String newAddy;
        if (addy.contains("http://")) {
            newAddy = addy.replace("http://", "");
            if (newAddy.contains("/")) {
                return "Website\n" + newAddy.replace("/", "");
            } else {
                return "Website\n" + newAddy;
            }
        } else {
            return "Website\n" + addy;
        }
    }

    private void getReviews(JSONObject input) {
        try {
            if (!input.isNull("reviews")) {
                howManyReviews = 0;
                for (int i = 0; i < input.getJSONArray("reviews").length(); i++) {
                    howManyReviews++;
                    howManyReviewsTx.setText(String.valueOf(howManyReviews) + " reviews!\nSwipe to view");
                    final JSONObject j = input.getJSONArray("reviews").getJSONObject(i);

                    String fullImgUrl = "https://www.googleapis.com/plus/v1/people/" + j.getString("author_url").replace("https://plus.google.com/", "") + "?fields=image&key=AIzaSyDNeU1c99SGxOzpcg1VOMeQB1C8r5ARhSE";

                    Ion.with(this).load(fullImgUrl).asString().setCallback(new FutureCallback<String>() {
                        @Override
                        public void onCompleted(Exception e, String result) {
                            if (result != null) {
                                try {
                                    JSONObject jso = new JSONObject(result);
                                    String imageURL = jso.getJSONObject("image").getString("url");

                                    Log.e("RATING URL", j.getString("rating"));

                                    reviewDataList.add(
                                            new ReviewData(j.getString("author_name"), j.getString("rating"), j.getString("text"), imageURL)
                                    );

                                    reviewsRVAdapter = new ReviewsRVAdapter(reviewDataList, DetailsActivity.this);

                                    fullReviewRecyclerView.setAdapter(reviewsRVAdapter);
                                    reviewsRVAdapter.notifyDataSetChanged();
                                } catch (JSONException ex) {
                                    ex.printStackTrace();
                                }

                            }
                        }
                    });


                }
                topBar.setVisibility(View.VISIBLE);

            } else {

                howManyReviewsTx.setText("There are no reviews for this place");
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


//    private void setUpToolbarForRatings(JSONObject resultJSON) {
//        try {
//            if (!resultJSON.isNull("reviews")) {
//
//            }
//
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//    }

    void parseReviews(JSONObject input) {
//        try {
//
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
    }
//
//    private void parseRatings(JSONObject response) {
//        try {
//            JSONArray ja = response.getJSONArray("reviews");
//            for (int i=0; i < ja.length(); i++) {
//                JSONObject jso = ja.getJSONObject(i);
//                Log.e("Author Name", jso.getString("author_name"));
//            }
//        } catch (JSONException ex) {
//            ex.printStackTrace();
//        }
//    }

    private void getPhotoGallery(JSONObject input) {
        try {
            if (!input.isNull("photos")) {
                for (int i = 0; i < input.getJSONArray("photos").length(); i++) {
                    JSONObject jso = input.getJSONArray("photos").getJSONObject(i);
                    String ref = jso.getString("photo_reference");
                    String height = Integer.toString(jso.getInt("height"));
                    String width = Integer.toString(jso.getInt("width"));

                    photoGalleryList.add(
                            new PhotoGalleryData(
                                    "https://maps.googleapis.com/maps/api/place/photo?photoreference=" + ref + "&sensor=false&maxheight=" + height + "&maxwidth=" + width + "&key=AIzaSyDNeU1c99SGxOzpcg1VOMeQB1C8r5ARhSE"
                            )

                    );


                }

                initPhotoGallery();
            } else {
                noPhotosToShowTx.setVisibility(View.VISIBLE);
                photoGalleryRecyclerView.setVisibility(View.GONE);
            }
        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }

    private void initPhotoGallery() {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        photoGalleryRecyclerView.setLayoutManager(layoutManager);

        PhotoGalleryRVAdapter pgAdapter = new PhotoGalleryRVAdapter(getPhotoGalleryItems(), this);
        photoGalleryRecyclerView.setAdapter(pgAdapter);
    }

    //
    private List<PhotoGalleryData> getPhotoGalleryItems() {
        return photoGalleryList;
    }

    private void initReviewRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
        fullReviewRecyclerView.setLayoutManager(llm);
        fullReviewRecyclerView.hasFixedSize();
    }

    @Override
    public void onClick(View v) {

        if (v instanceof RelativeLayout) {
            if (v.getId() == R.id.hours_rl) {
                HoursOfOperationDialogFragment dialogFragment = HoursOfOperationDialogFragment.newInstance(openingDays.get(0),
                        openingDays.get(1), openingDays.get(2), openingDays.get(3), openingDays.get(4), openingDays.get(5), openingDays.get(6)

                );
                dialogFragment.show(getSupportFragmentManager(), "TAG");
            }
        }


        if (v instanceof TextView) {
            if (v.getId() == R.id.viewOnMapTx) {
                // open up the mapfragment
                DetailPlaceMapFragment dpMapFrag = DetailPlaceMapFragment.newInstance(lat, lng, placeName);
                dpMapFrag.show(getSupportFragmentManager(), "TAG");

            }
        }

    }


    @Override
    public void onItemClick(View v, int pos) {

    }


}
