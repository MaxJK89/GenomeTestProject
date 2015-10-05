package quant.cann.genometestproject.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import quant.cann.genometestproject.R;
import quant.cann.genometestproject.data.PhotoGalleryData;
import quant.cann.genometestproject.data.SimplePlaceData;
import quant.cann.genometestproject.utils.OnSSItemClickListener;

/**
 * Created by angboty on 10/4/2015.
 */
public class PhotoGalleryRVAdapter extends RecyclerView.Adapter<PhotoGalleryRVAdapter.HRAVH> {
    public static List<PhotoGalleryData> list;
    Context context;

    OnSSItemClickListener mListener;

    public PhotoGalleryRVAdapter(List<PhotoGalleryData> list, Context context) {
        PhotoGalleryRVAdapter.list = list;
        this.context = context;
    }

    public void add(PhotoGalleryData obj) {
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
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_gallery_rv_item, viewGroup, false);
        HRAVH pvh = new HRAVH(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(HRAVH vH, int i) {

        Picasso.with(context).load(list.get(i).getImageURL()).into(vH.image);
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
        RoundedImageView image;

        HRAVH(View itemView) {
            super(itemView);
            cV = (CardView) itemView.findViewById(R.id.card4);
            image = (RoundedImageView) itemView.findViewById(R.id.photo_gallery_image_view);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }
}
