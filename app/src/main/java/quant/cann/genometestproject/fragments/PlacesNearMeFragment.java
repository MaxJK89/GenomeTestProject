package quant.cann.genometestproject.fragments;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import quant.cann.genometestproject.DetailsActivity;
import quant.cann.genometestproject.Global;
import quant.cann.genometestproject.R;
import quant.cann.genometestproject.adapters.PlacesNearMeRecyclerViewAdapter;
import quant.cann.genometestproject.data.SimplePlaceData;
import quant.cann.genometestproject.utils.OnSSItemClickListener;

public class PlacesNearMeFragment extends Fragment implements OnSSItemClickListener {

    public static String PLACE_ID = "PlaceId";
    public static String ICONURL = "PlaceId";
    public static String LAT = "Lat";
    public static String LNG = "Lng";
    public static int howManyPlaces = 0;
    public final String TAG = "PlacesNearMeFragment";
    @Bind(R.id.pnm_swipe_refresh_layout)
    SwipeRefreshLayout srlSwipeRefreshLayout;
    @Bind(R.id.places_near_me_recycler_view)
    RecyclerView rvRecyclerView;
    List<SimplePlaceData> placeDataList = new ArrayList<>();
    PlacesNearMeRecyclerViewAdapter pnmrvAdapter;
    private Global app;

    public PlacesNearMeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        app = ((Global) getActivity().getApplication());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_places_near_me, container, false);
        ButterKnife.bind(this, v);


        initRecyclerView();
        setUpRefresher();

        getClosestPlacesJSON(
                String.valueOf(Global.getLatitude()),
                String.valueOf(Global.getLongitude()),
                100, false, null);

//        test();
        return v;
    }


    private void initRecyclerView() {
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        rvRecyclerView.setHasFixedSize(true);
        rvRecyclerView.setLayoutManager(llm);
    }

    private void setAdapter() {
        if (pnmrvAdapter != null) {
            rvRecyclerView.setAdapter(pnmrvAdapter);
            pnmrvAdapter.setOnItemClickListener(this);
        }
    }

    private void setUpRefresher() {
        if (srlSwipeRefreshLayout != null) {
            srlSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    refreshItems();
                }
            });
        }
    }

    private void refreshItems() {
        // refresh Items
        // when finished call this....
        onItemsLoadedComplete();

    }

    private void onItemsLoadedComplete() {
        srlSwipeRefreshLayout.setRefreshing(false);
    }

    private void getClosestPlacesJSON(String latitude, String longitude, int radius, boolean isNextPage, String nextPageToken) {
        String tag_json_obj = "json_obj_req";
        String url;
        if (!isNextPage) {
            url = "https://maps.googleapis.com/maps/api/place/search/json?location="
                    + latitude + "," + longitude + "&radius=" + String.valueOf(radius) + "&key=AIzaSyB9wM1tT4wr6_kYQfBsUrxIvWOh0kALX6E&sensor=true";

        } else {
            url = "https://maps.googleapis.com/maps/api/place/search/json?location="
                    + latitude + "," + longitude + "&radius=" + String.valueOf(radius) + "&key=AIzaSyB9wM1tT4wr6_kYQfBsUrxIvWOh0kALX6E&sensor=true" +
                    "&pagetoken=" + nextPageToken;
        }


        final ProgressDialog pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading...");
        pDialog.show();

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET,
                url, null,
                new Response.Listener<JSONObject>() {

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e(TAG, response.toString());
                        parseJSONMessage(response);
                        pDialog.hide();
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());

                // hide the progress dialog
                pDialog.hide();

                showErrorForGettingClosestPlaces();

            }
        });

// Adding request to request queue
        Global.getInstance().addToRequestQueue(jsonObjReq, tag_json_obj);
    }


    private void parseJSONMessage(JSONObject input) {
        try {

            JSONArray jaResults = input.getJSONArray("results");

            for (int i = 0; i < jaResults.length(); i++) {
                JSONObject jso = jaResults.getJSONObject(i);

                howManyPlaces++;

                Log.e("PLEASES", String.valueOf(i));

                placeDataList.add(new SimplePlaceData(
                                jso.getString("name"), jso.getString("place_id"),
                                jso.getString("icon"), jso.getJSONObject("geometry").getJSONObject("location").getDouble("lat"), jso.getJSONObject("geometry").getJSONObject("location").getDouble("lng"))
                );
            }

            // can only get 20 requeust at a time so we check later and try to get more
            // first we must see if there are more then 20... if so then get more but only if we have less then 25
            if (howManyPlaces >= 20) {
                if (howManyPlaces > 25) {
                    if (checkToGetMoreResults(input)) {
                        getClosestPlacesJSON(
                                String.valueOf(Global.getLatitude()),
                                String.valueOf(Global.getLongitude()),
                                100, true, input.getString("next_page_token"));
                    }
                }
            }

            pnmrvAdapter = new PlacesNearMeRecyclerViewAdapter(placeDataList, getActivity());
            pnmrvAdapter.notifyDataSetChanged();
            setAdapter();

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
    }


    private boolean checkToGetMoreResults(JSONObject input) {
        return !input.isNull("next_page_token");
    }

    // show an error for grabbing closest places
    private void showErrorForGettingClosestPlaces() {
        AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity());
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
                        getClosestPlacesJSON(
                                String.valueOf(Global.getLatitude()),
                                String.valueOf(Global.getLongitude()),
                                100, false, null);

                    }
                });
        builder.setNegativeButton("No",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // used for clicks inside the recycler view
    @Override
    public void onItemClick(View v, int position) {
        Intent i = new Intent(getActivity(), DetailsActivity.class);
        i.putExtra(PLACE_ID, PlacesNearMeRecyclerViewAdapter.list.get(position).getsPlaceID());
        i.putExtra(LAT, PlacesNearMeRecyclerViewAdapter.list.get(position).getLat());
        i.putExtra(LNG, PlacesNearMeRecyclerViewAdapter.list.get(position).getLng());
        getActivity().startActivity(i);
    }

}
