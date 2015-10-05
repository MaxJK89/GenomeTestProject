package quant.cann.genometestproject.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import quant.cann.genometestproject.R;
import quant.cann.genometestproject.data.ReviewData;
import quant.cann.genometestproject.data.SimplePlaceData;
import quant.cann.genometestproject.utils.OnSSItemClickListener;
import quant.cann.genometestproject.utils.TypeFaceHelper;

/**
 * Created by angboty on 10/3/2015.
 */
public class ReviewsRVAdapter extends RecyclerView.Adapter<ReviewsRVAdapter.HRAVH> {
    public static List<ReviewData> list;
    Context context;

    OnSSItemClickListener mListener;

    public ReviewsRVAdapter(List<ReviewData> list, Context context) {
        ReviewsRVAdapter.list = list;
        this.context = context;
    }

    public void add(ReviewData obj) {
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_item_layout, viewGroup, false);
        HRAVH pvh = new HRAVH(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(HRAVH vH, int i) {

        vH.comment.setText(list.get(i).getComment());
        vH.name.setText(list.get(i).getAuthorName());
        vH.comment.setTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/opensanslight" +
                ".ttf"));
        vH.name.setTypeface(new TypeFaceHelper(context).getOpenSansBold());
        Picasso.with(context).load(list.get(i).getAuthorNameUrl()).into(vH.image);
//        vH.rBar.setRating(Float.parseFloat(list.get(i).getRating()));
        vH.rBar.setText(list.get(i).getRating() + " STARS");
        vH.rBar.setTypeface(new TypeFaceHelper(context).getOpenSansRegular());
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
        TextView comment, name;
        CircularImageView image;
        TextView rBar;

        HRAVH(View itemView) {
            super(itemView);
            cV = (CardView) itemView.findViewById(R.id.gplus_card);
            name = (TextView) itemView.findViewById(R.id.gplus_name);
            comment = (TextView) itemView.findViewById(R.id.gplus_comment);
            image = (CircularImageView) itemView.findViewById(R.id.gplus_image);
            rBar = (TextView) itemView.findViewById(R.id.starRatingTx);

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
