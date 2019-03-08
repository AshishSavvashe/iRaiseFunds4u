package com.appbell.iraisefund4u.resto.ui;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.appbell.common.util.AppUtil;
import com.appbell.iraisefund4u.R;
import com.appbell.iraisefund4u.common.util.DateUtil;
import com.appbell.iraisefund4u.resto.util.AndroidAppConstants;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;
    Bundle bundle;
    int voucherBookID;
    View parentView;
    MapView map;
    Marker marker;
    long startDate;
    long endDate;
    String currentAppliedFilter;
    ArrayList<VoucherData> voucherlist;
    ArrayList<VoucherData> filtededList;
    Activity activity = getActivity();


    public static MapViewFragment getInstance( int voucherBookID, long startdate, long endDate ,String selectedItem4Filter){
        MapViewFragment fragment = new MapViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("voucherBookID", voucherBookID);
        bundle.putLong("startdate", startdate);
        bundle.putLong("endDate", endDate);
        bundle.putString("currentAppliedFilter",selectedItem4Filter);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        voucherBookID = getArguments().getInt("voucherBookID", 0);
        startDate = getArguments().getLong("startdate", 0);
        endDate = getArguments().getLong("endDate", 0);
        currentAppliedFilter = getArguments().getString("currentAppliedFilter");

        bundle = new VoucherBookService(getActivity()).restaurantDetails(voucherBookID);

        map =  parentView.findViewById(R.id.map);
        map.onCreate(savedInstanceState);
        map.getMapAsync(MapViewFragment.this);


        ((VoucherListActivity)getActivity()).showRestoNameAndMonthSpineer();

        ViewModel viewModel = ViewModelProviders.of(getActivity()).get(ViewModel.class);

        voucherlist = (viewModel.getVoucherList(voucherBookID));

        filtededList = getFilteredList(currentAppliedFilter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parentView = inflater.inflate(R.layout.fragment_map_view, null);
        return parentView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        renderMap();
    }


    public ArrayList<VoucherData> getFilteredList(String selectedItem4Filter) {

        SimpleDateFormat sdf = DateUtil.getDateFormatter(getActivity(),"MMM-yy");

        ArrayList<VoucherData> filterResults = null;

        String[] selectedItem = selectedItem4Filter.toString().split("~");

        if (selectedItem4Filter != null && selectedItem4Filter.length() > 0) {

            if (selectedItem[0].contains(AndroidAppConstants.ALLRestaurant) && selectedItem[1].contains(AndroidAppConstants.ALLMonth)) {

                filterResults = new ArrayList<>();
                filterResults.addAll(voucherlist);

            } else {
                ArrayList<VoucherData> voucherFiltered = new ArrayList<>();
                boolean restMatched;
                boolean monthMatched;

                for (int i = 0, len = voucherlist.size(); i < len; i++) {

                    restMatched = false;
                    monthMatched = false;

                    if (AppUtil.getValAtIndex(selectedItem, 0).contains(AndroidAppConstants.ALLRestaurant) || AppUtil.getValAtIndex(selectedItem, 0).contains(voucherlist.get(i).getRestaurantName())) {
                        restMatched = true;
                    }
                    if (AppUtil.getValAtIndex(selectedItem, 1).contains(AndroidAppConstants.ALLMonth) || AppUtil.getValAtIndex(selectedItem, 1).contains(sdf.format(voucherlist.get(i).getVoucherStartTime()))) {
                        monthMatched = true;
                    }
                    if (restMatched && monthMatched) {
                        voucherFiltered.add(voucherlist.get(i));
                    }
                }
                filterResults= voucherFiltered;
            }
        } else {
            filterResults= voucherlist;
        }

        return filterResults;

    }

    private void renderMap() {
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                LatLngBounds.Builder builder = LatLngBounds.builder();

                if (filtededList != null && filtededList.size() > 0) {

                    parentView.findViewById(R.id.btnNoData).setVisibility(View.GONE);

                    for (int i = 0; i < filtededList.size(); i++) {

                        addMarker(filtededList.get(i).getRestaurantlat(), filtededList.get(i).getRestaurantlong(), filtededList.get(i).getRestaurantName(),filtededList.get(i).getRestaurantAddress());

                        LatLng latLng = (LatLng) bundle.get(filtededList.get(i).getRestaurantName());
                        builder.include(latLng);
                    }

                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 150);
                    mMap.moveCamera(cameraUpdate);
                } else {
                    parentView.findViewById(R.id.map).setVisibility(View.GONE);
                    parentView.findViewById(R.id.btnNoData).setVisibility(View.VISIBLE);
                }
            }

            public final Marker addMarker(double restaurantlat, double restaurantlong, String restaurantName, String restaurantAddress) {

                marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(restaurantlat, restaurantlong))
                        .title(restaurantName));

                marker.setSnippet(restaurantAddress);
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(getMarkerBitmapFromView(R.drawable.address_icon, restaurantName, getLayoutInflater())));
                marker.hideInfoWindow();

                return marker;

            }
        });

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {

                String selectedRestoName = marker.getTitle();

                ((VoucherListActivity) getActivity()).applyFilteronVoucherList(selectedRestoName);

            }
        });

        MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity());
        mMap.setInfoWindowAdapter(markerInfoWindowAdapter);
    }

    private Bitmap getMarkerBitmapFromView(int address_icon,String restaurantName, LayoutInflater inflater) {

        View customMarkerView;
        customMarkerView = inflater.inflate(R.layout.view_custom_marker, null);
        TextView tvRestoName = customMarkerView.findViewById(R.id.tvRestoName);
        tvRestoName.setText(restaurantName);
        customMarkerView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        customMarkerView.layout(0, 0, customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight());
        customMarkerView.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(customMarkerView.getMeasuredWidth(), customMarkerView.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = customMarkerView.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        customMarkerView.draw(canvas);

        return returnedBitmap;
    }


    public void  applyFilter(String selectedItem4Filter){

        if(marker != null){
            marker.remove();
            renderMap();
        }
         filtededList = getFilteredList(selectedItem4Filter);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                getActivity().finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        map.onResume();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        map.onDestroy();
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

}


