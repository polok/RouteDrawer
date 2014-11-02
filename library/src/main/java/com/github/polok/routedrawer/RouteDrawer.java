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

import android.graphics.Color;

import com.github.polok.routedrawer.model.Legs;
import com.github.polok.routedrawer.model.Route;
import com.github.polok.routedrawer.model.Routes;
import com.github.polok.routedrawer.model.Step;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class RouteDrawer implements DrawerApi {

    private static final float DEFAULT_MARKER_ALPHA = 1;
    private static final int DEFAULT_PATH_WIDTH = 5;
    private static final int DEFAULT_PATH_COLOR = Color.RED;

    private float alpha;
    private int pathWidth;
    private int pathColor;

    private BitmapDescriptor bitmapDescriptor;

    private GoogleMap googleMap;

    private RouteDrawer(RouteDrawerBuilder builder) {
        this.googleMap = builder.googleMap;

        this.alpha = builder.alpha;
        this.pathWidth = builder.pathWidth;
        this.pathColor = builder.pathColor;
        this.bitmapDescriptor = builder.bitmapDescriptor;
    }

    @Override
    public void drawPath(Routes routes) {
        PolylineOptions lineOptions = null;

        for (Route route : routes.routes) {
            for (Legs legs : route.legs) {
                lineOptions = new PolylineOptions();

                for (Step step : legs.steps) {
                    lineOptions.add(new LatLng(step.startLocation.lat, step.startLocation.lng));
                    lineOptions.add(new LatLng(step.endLocation.lat, step.endLocation.lng));

                    googleMap.addMarker(
                            new MarkerOptions()
                                    .alpha(alpha)
                                    .position(new LatLng(step.startLocation.lat, step.startLocation.lng))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );

                    googleMap.addMarker(
                            new MarkerOptions()
                                    .alpha(alpha)
                                    .position(new LatLng(step.endLocation.lat, step.endLocation.lng))
                                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    );

                    lineOptions.width(pathWidth);
                    lineOptions.color(pathColor);

                    googleMap.addPolyline(lineOptions);

                }
            }
        }
    }

    public static class RouteDrawerBuilder {
        private BitmapDescriptor bitmapDescriptor;

        private int pathWidth;
        private int pathColor;
        private float alpha;

        private final GoogleMap googleMap;

        public RouteDrawerBuilder(GoogleMap googleMap) {
            this.googleMap = googleMap;

            this.pathWidth = DEFAULT_PATH_WIDTH;
            this.pathColor = DEFAULT_PATH_COLOR;
            this.alpha = DEFAULT_MARKER_ALPHA;
            this.bitmapDescriptor = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE);
        }

        public RouteDrawerBuilder withColor(int pathColor) {
            this.pathColor = pathColor;
            return this;
        }

        public RouteDrawerBuilder withWidht(int pathWidth) {
            this.pathWidth = pathWidth;
            return this;
        }

        public RouteDrawerBuilder withMarkerIcon(BitmapDescriptor bitmapDescriptor) {
            this.bitmapDescriptor = bitmapDescriptor;
            return this;
        }

        public RouteDrawerBuilder withAlpha(float alpha) {
            this.alpha = alpha;
            return this;
        }

        public RouteDrawer build() {
            return new RouteDrawer(this);
        }

    }

}
