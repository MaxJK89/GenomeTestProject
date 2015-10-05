package quant.cann.genometestproject.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.HexagonImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import quant.cann.genometestproject.R;
import quant.cann.genometestproject.data.SimplePlaceData;
import quant.cann.genometestproject.utils.OnSSItemClickListener;

/**
 * Created by Max Kleine on 10/1/2015.
 */
public class PlacesNearMeRecyclerViewAdapter extends RecyclerView.Adapter<PlacesNearMeRecyclerViewAdapter.HRAVH> {
    public static List<SimplePlaceData> list;
    Context context;

    OnSSItemClickListener mListener;

    public PlacesNearMeRecyclerViewAdapter(List<SimplePlaceData> list, Context context) {
        PlacesNearMeRecyclerViewAdapter.list = list;
        this.context = context;
    }

    public void add(SimplePlaceData obj) {
        list.add(obj);
        notifyDataSetChanged();
    }

    public void remove(SimplePlaceData obj) {
        list.remove(obj);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public HRAVH onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.stores_near_me_rv_item, viewGroup, false);
        HRAVH pvh = new HRAVH(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(HRAVH vH, int i) {

        vH.placeName.setText(list.get(i).getsPlaceName());
        vH.placeName.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensanslight" +
                ".ttf"));
        Picasso.with(context).load(list.get(i).getsIconURL()).into(vH.placeImage);


    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public void setOnItemClickListener(final OnSSItemClickListener mItemClickListener) {
        this.mListener = mItemClickListener;
    }

    public class HRAVH extends RecyclerView.ViewHolder implements View.OnClickListener {

        CardView cV;
        TextView placeName;
        HexagonImageView placeImage;

        HRAVH(View itemView) {
            super(itemView);
            cV = (CardView) itemView.findViewById(R.id.pnm_card_view);
            placeName = (TextView) itemView.findViewById(R.id.pnm_name_textview);
            placeImage = (HexagonImageView) itemView.findViewById(R.id.pnm_imageview);

            cV.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

}
