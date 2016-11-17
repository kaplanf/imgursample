package com.android.imgursample.fragments;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.android.imgursample.R;
import com.android.imgursample.activity.MainActivity;
import com.android.imgursample.adapters.ImageListAdapter;
import com.android.imgursample.interfaces.OnMainFragmentListener;
import com.android.imgursample.restful.client.RestClient;
import com.android.imgursample.restful.model.ImageObject;
import com.android.imgursample.restful.model.ImageResponse;
import com.android.imgursample.util.ImageCache;
import com.android.imgursample.util.ImgurConstants;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.rest.spring.annotations.RestService;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestClientException;

import java.util.ArrayList;

import static com.android.imgursample.fragments.GalleryFragment.ListType.GRID;
import static com.android.imgursample.fragments.GalleryFragment.ListType.LIST;
import static com.android.imgursample.fragments.GalleryFragment.ListType.STAGGERED;

/**
 * Created by kaplanf on 11/11/2016.
 */

@EFragment(R.layout.gallery_fragment)
public class GalleryFragment extends BaseFragment implements AdapterView.OnItemSelectedListener, CompoundButton.OnCheckedChangeListener,
        ImageListAdapter.ItemSelected {

    public enum ListType {LIST, GRID, STAGGERED}

    @RestService
    RestClient restClient;

    @ViewById(R.id.galleryRecyclerView)
    RecyclerView galleryRecyclerView;

    @ViewById(R.id.gallerySectionSpinner)
    Spinner gallerySectionSpinner;

    @ViewById(R.id.galleryViralSwitch)
    Switch galleryViralSwitch;

    @ViewById(R.id.galleryListViewSelect)
    RelativeLayout galleryListViewSelect;

    @ViewById(R.id.galleryGridViewSelect)
    RelativeLayout galleryGridViewSelect;

    @ViewById(R.id.galleryStaggeredViewSelect)
    RelativeLayout galleryStaggeredViewSelect;

    @ViewById(R.id.getImageButton)
    TextView getImageButton;

    private OnMainFragmentListener listener;

    private String section;

    private int page = 0;

    private boolean showViral = true;

    private RecyclerView.LayoutManager layoutManager;

    private ImageResponse imageResponse;

    ImageListAdapter imageListAdapter;

    ImageCache imageCache;

    ListType listType;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        listener = (OnMainFragmentListener) context;
    }

    @AfterViews
    protected void afterViews() {
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(R.string.gallery);

        if (listType == null) {
            listType = LIST;
        }

        gallerySectionSpinner.setOnItemSelectedListener(this);
        galleryViralSwitch.setOnCheckedChangeListener(this);

        section = gallerySectionSpinner.getSelectedItem().toString().toLowerCase();
        showViral = galleryViralSwitch.isChecked();

        layoutManager = new LinearLayoutManager(getActivity());
        galleryRecyclerView.setHasFixedSize(true);
        galleryRecyclerView.setLayoutManager(layoutManager);

        imageCache = ImageCache.getInstance();
        imageCache.initializeCache();

        galleryViralSwitch.setChecked(true);
        galleryViralSwitch.setVisibility(gallerySectionSpinner.getSelectedItemPosition() == 2 ? View.VISIBLE : View.GONE);

        if (isConnectionAvailable()) {
            getGalleryItems(section, page, showViral);
        } else {
            showConnectionError();
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        section = gallerySectionSpinner.getItemAtPosition(pos).toString().toLowerCase();
        galleryViralSwitch.setVisibility(pos == 2 ? View.VISIBLE : View.GONE);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    @Background
    void getGalleryItems(String section, int page, boolean showViral) {
        try {
            showProgressDialog();
            restClient.setHeader(HttpHeaders.AUTHORIZATION, "Client-ID " + ImgurConstants.CLIENT_ID);
            imageResponse = restClient.getImages(section, Integer.toString(page), Boolean.toString(showViral));
            if (imageResponse != null && imageResponse.imageObjects != null && imageResponse.imageObjects.size() > 0)
                initializeList(imageResponse, listType);
        } catch (RestClientException exception) {
            exception.printStackTrace();
        } finally {
            hideProgressDialog();
        }
    }

    @Click({R.id.galleryListViewSelect, R.id.galleryGridViewSelect, R.id.galleryStaggeredViewSelect})
    void clickSelectView(View v) {
        switch (v.getId()) {
            case R.id.galleryListViewSelect:
                listType = LIST;
                initializeList(imageResponse, LIST);
                break;

            case R.id.galleryGridViewSelect:
                listType = GRID;
                initializeList(imageResponse, GRID);
                break;

            case R.id.galleryStaggeredViewSelect:
                listType = STAGGERED;
                initializeList(imageResponse, STAGGERED);
                break;
        }
    }

    @Click(R.id.getImageButton)
    void clickGetImage(View v) {
        getGalleryItems(section, page, showViral);
    }

    @UiThread
    void initializeList(ImageResponse imageResponse, ListType listTypeEnum) {

        switch (listTypeEnum) {
            case LIST:
                layoutManager = new LinearLayoutManager(getActivity());
                break;
            case GRID:
                layoutManager = new GridLayoutManager(getActivity(), 2);
                break;
            case STAGGERED:
                layoutManager = new StaggeredGridLayoutManager(2, 1);
                break;
        }

        galleryRecyclerView.setLayoutManager(layoutManager);
        imageListAdapter = new ImageListAdapter(prepareImageObjectList(imageResponse.imageObjects), getActivity(), listTypeEnum, imageCache);
        imageListAdapter.setItemSelected(this);
        imageListAdapter.notifyDataSetChanged();
        galleryRecyclerView.setAdapter(imageListAdapter);
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        showViral = b;
    }

    public ArrayList<ImageObject> prepareImageObjectList(ArrayList<ImageObject> imageObjects) {
        ArrayList<ImageObject> preparedList = new ArrayList<ImageObject>();
        for (ImageObject imageObject : imageObjects) {
            if (imageObject.is_album) {
                preparedList.add(imageObject);
            } else {
                if (imageObject.type.equals("image/png") || imageObject.type.equals("image/jpeg")) {
                    preparedList.add(imageObject);
                }
            }
        }
        return preparedList;
    }

    @Override
    public void clicked(int position) {
        listener.toImageDetailFragment(prepareImageObjectList(imageResponse.imageObjects).get(position));
    }
}
