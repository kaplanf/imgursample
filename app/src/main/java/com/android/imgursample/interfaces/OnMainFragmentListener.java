package com.android.imgursample.interfaces;

import com.android.imgursample.restful.model.ImageObject;

public interface OnMainFragmentListener {

    void onCloseFragment(String tag);

    void onStartFragment(String tag);

    void toImageDetailFragment(ImageObject imageObject);
}
