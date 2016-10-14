package io.github.vitovalov.tabbedcoordinator;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class About extends Fragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;
    private boolean mapsSupported = true;
    private Button filter;
    private double radious;
    private LatLng myLaLn;
    private Circle mapCircle;
    private View view;
    private String flag;
    private io.github.vitovalov.tabbedcoordinator.Events event;

    private TextView dateTo;
    private TextView dateFrom;
    private TextView eventDescription;
    private TextView eventName;



    private void initializeMap() {
//
//        if(googleMap==null)
//        {
//            googleMap = getActivity().getAs(this);
//        }
        setUpMarker();
    }


    public void setUpMarker()
    {
            int zoomVal = 10;
            myLaLn = new LatLng(event.getEventLocationLatitude(), event.getEventLocationLongitude());


        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(myLaLn);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        googleMap.addMarker(markerOptions);

        //googleMap.setMyLocationEnabled(true);
        googleMap.getUiSettings().setCompassEnabled(true);
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLaLn,40));
        // Zoom in, animating the camera.
        googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        // Zoom out to zoom level 10, animating with a duration of 2 seconds.
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(12), 2000, null);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_about, container, false);

        event = (io.github.vitovalov.tabbedcoordinator.Events) getArguments().getSerializable("EVENT_ABOUT");

        MapsInitializer.initialize(getActivity());

        dateFrom = (TextView) view.findViewById(R.id.date_from);
        dateTo = (TextView) view.findViewById(R.id.date_to);
        eventName = (TextView) view.findViewById(R.id.event_name);
        eventDescription = (TextView) view.findViewById(R.id.event_description);

        dateFrom.setText(event.getEventDateFrom());
        dateTo.setText(event.getEventDateTo());
        eventDescription.setText(event.getEventDescription());
        eventName.setText(event.getEventName());

        mapView = (MapView) view.findViewById(R.id.about_map);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPause() {
        super.onPause();
        // googleMap.clear();
    }

    @Override
    public void onResume() {
        super.onResume();
        //googleMap.clear();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        // mapView.onLowMemory();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;

        Log.e("in set up marker"," ****** "+googleMap);

        setUpMarker();

    }

}
