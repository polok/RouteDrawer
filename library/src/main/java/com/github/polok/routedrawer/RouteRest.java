/*

Copyright 2014 Marcin Polak

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

 */

package com.github.polok.routedrawer;

import com.github.polok.routedrawer.model.TravelMode;
import com.google.android.gms.maps.model.LatLng;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.IOException;

import rx.Observable;
import rx.functions.Func0;
import rx.schedulers.Schedulers;
import rx.util.async.Async;

public class RouteRest implements RouteApi {

    private OkHttpClient client;

    public RouteRest(OkHttpClient client) {
        this.client = client;
    }

    public RouteRest() {
        this.client = new OkHttpClient();
    }

    @Override
    public Observable<String> getJsonDirections(final LatLng start, final LatLng end, final TravelMode mode) {
        Func0<String> resultFunc = new Func0<String>() {
            @Override
            public String call() {
                try {
                    return getJSONDirection(start, end, mode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return "";
            }
        };

        return Async.start(resultFunc, Schedulers.io());
    }

    private String getJSONDirection(LatLng start, LatLng end, TravelMode mode) throws IOException {
        String url = "http://maps.googleapis.com/maps/api/directions/json?"
                + "origin="
                + start.latitude + ","
                + start.longitude
                + "&destination="
                + end.latitude + ","
                + end.longitude
                + "&sensor=false&units=metric&mode="
                + mode.name().toLowerCase();

        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }


}
