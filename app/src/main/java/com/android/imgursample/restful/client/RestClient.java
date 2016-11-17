package com.android.imgursample.restful.client;

import com.android.imgursample.restful.model.ImageResponse;
import com.android.imgursample.util.NetworkConstants;

import org.androidannotations.rest.spring.annotations.Get;
import org.androidannotations.rest.spring.annotations.Path;
import org.androidannotations.rest.spring.annotations.RequiresHeader;
import org.androidannotations.rest.spring.annotations.Rest;
import org.androidannotations.rest.spring.api.RestClientHeaders;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.GsonHttpMessageConverter;


@Rest(rootUrl = NetworkConstants.BASE_URL, converters = {GsonHttpMessageConverter.class, StringHttpMessageConverter.class})
public interface RestClient extends RestClientHeaders {

    @Override
    void setHeader(String name, String value);

    @RequiresHeader("Authorization")
    @Get(NetworkConstants.GET_GALLERY_ITEM)
    ImageResponse getImages(@Path String section, @Path String page, @Path String showViral);
}
