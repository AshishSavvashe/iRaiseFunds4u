package com.appbell.iraisefund4u.resto.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.appbell.iraisefund4u.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

class MarkerInfoWindowAdapter implements GoogleMap.InfoWindowAdapter  {
    private Context context;
    LayoutInflater inflater;
    public MarkerInfoWindowAdapter(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.view_custom_infowindow, null);

        TextView tvRestoName = (TextView) view.findViewById(R.id.tvRestoName);
        TextView tvRestoAddress = (TextView) view.findViewById(R.id.tvRestoAddress);

        tvRestoName.setText(marker.getTitle());
        tvRestoAddress.setText(marker.getSnippet());

        return view;
    }

    @Override
    public View getInfoContents(Marker arg0) {

        return null;
    }
}
