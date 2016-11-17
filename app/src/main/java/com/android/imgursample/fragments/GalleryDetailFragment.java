package com.android.imgursample.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.imgursample.R;
import com.android.imgursample.restful.model.ImageObject;
import com.squareup.picasso.Picasso;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.FragmentArg;
import org.androidannotations.annotations.ViewById;

/**
 * Created by kaplanf on 17/11/2016.
 */

@EFragment(R.layout.gallery_detail_fragment)
public class GalleryDetailFragment extends BaseFragment {

    @ViewById(R.id.imageDetailTitle)
    TextView imageDetailTitle;

    @ViewById(R.id.imageDetailBigImage)
    ImageView imageDetailBigImage;

    @ViewById(R.id.imageDetailDescription)
    TextView imageDetailDescription;

    @ViewById(R.id.imageDetailUpVote)
    TextView imageDetailUpVote;

    @ViewById(R.id.imageDetailDownVote)
    TextView imageDetailDownVote;

    @ViewById(R.id.imageDetailScore)
    TextView imageDetailScore;

    @FragmentArg
    ImageObject imageObject;

    Toolbar toolbar;

    CoordinatorLayout.Behavior behavior;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @AfterViews
    protected void afterViews() {

        toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        if (isConnectionAvailable()) {
            imageDetailTitle.setText(imageObject.title != null ? imageObject.title : getResources().getString(R.string.no_title));
            Picasso.with(getActivity()).load(getImageLink(imageObject)).into(imageDetailBigImage);

            imageDetailDescription.setText(imageObject.description != null ? imageObject.description :
                    getResources().getString(R.string.no_desc));

            imageDetailUpVote.setText(getResources().getString(R.string.upvote) + imageObject.ups);
            imageDetailDownVote.setText(getResources().getString(R.string.downvote) + imageObject.downs);
            imageDetailScore.setText(getResources().getString(R.string.score) + imageObject.score);
        } else {
            showConnectionError();
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().onBackPressed();
            }
        });

    }

    private String getImageLink(ImageObject imageObject) {
        String imageLink = "";
        if (imageObject.cover != null) {
            imageLink = "http://i.imgur.com/" + imageObject.cover + "l.png";
        } else {
            imageLink = imageObject.link;
        }
        return imageLink;
    }
}