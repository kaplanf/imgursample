package com.android.imgursample.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imgursample.R;
import com.android.imgursample.fragments.GalleryFragment;
import com.android.imgursample.restful.model.ImageObject;
import com.android.imgursample.util.ImageCache;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;

/**
 * Created by kaplanf on 14/11/2016.
 */

public class ImageListAdapter extends RecyclerView.Adapter<ImageListAdapter.ViewHolder> {

    private ArrayList<ImageObject> imageObjectArrayList;
    private Context context;
    GalleryFragment.ListType type;
    ImageCache cache;

    ItemSelected listener;

    public void setItemSelected(ItemSelected itemSelected) {
        listener = itemSelected;
    }

    public interface ItemSelected {
        public void clicked(int position);
    }

    public ImageListAdapter(ArrayList<ImageObject> imageObjects, Context c, GalleryFragment.ListType listType, ImageCache imageCache) {
        context = c;
        type = listType;
        imageObjectArrayList = imageObjects;
        cache = imageCache;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = null;
        switch (type) {
            case LIST:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_object_cardview, parent, false);
                break;
            case GRID:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_object_cardview_grid, parent, false);
                break;
            case STAGGERED:
                v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.image_object_cardview_staggered, parent, false);
                break;
        }
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final ImageObject imageObject = imageObjectArrayList.get(position);
        String imageLink = "";
        if (imageObject.cover != null) {
            imageLink = "http://i.imgur.com/" + imageObject.cover + "l.png";
        } else {
            imageLink = imageObject.link;
        }
        if (imageLink != null) {
            Bitmap bitmapRetrieved = cache.getImageFromWarehouse(imageObject.id);
            if (bitmapRetrieved == null) {
                Picasso.with(context).load(imageLink).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        cache.addImageToWarehouse(imageObject.id, bitmap);
                        holder.imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_location));
                    }
                });
            } else {
                holder.imageView.setImageBitmap(bitmapRetrieved);
            }
        } else {
            holder.imageView.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.placeholder_location));
        }
        holder.imageDesc.setText(imageObject.description != null ? imageObject.description : imageObject.title);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.clicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return imageObjectArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        TextView imageDesc;
        ImageView imageView;

        public ViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            imageDesc = (TextView) v.findViewById(R.id.cardTextView);
            imageView = (ImageView) v.findViewById(R.id.cardImageView);
        }
    }
}
