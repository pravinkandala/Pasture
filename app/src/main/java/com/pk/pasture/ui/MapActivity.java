package com.pk.pasture.ui;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.maps.android.SphericalUtil;
import com.pk.pasture.R;
import com.pk.pasture.service.GetLocation;
import com.pk.pasture.util.AreaUtil;

import java.util.ArrayList;
import java.util.List;

import static com.pk.pasture.R.id.map;


/**
 * Reference from
 * http://stackoverflow.com/questions/20901141/how-to-draw-free-hand-polygon-in-google-map-v2-in-android
 * 
 */

public class MapActivity extends AppCompatActivity {
    private Button mBtnDrawState, mBtnDisplayArea;
    private FrameLayout mFrameLayout;
    private Boolean IS_MAP_MOVABLE;
    private List<LatLng> mLatLng = new ArrayList<>();

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mFrameLayout = (FrameLayout) findViewById(R.id.fram_map);
        mBtnDrawState = (Button) findViewById(R.id.btn_draw_State);
        mBtnDisplayArea = (Button) findViewById(R.id.btn_display_area);

        IS_MAP_MOVABLE = false;

        ((MapFragment)getFragmentManager().findFragmentById(map)).getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;
                GetLocation getLocation = new GetLocation(getApplicationContext());
                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(new LatLng(getLocation.getLatitude(),getLocation.getLongitude())).zoom(16).build();
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });

        mBtnDrawState.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (IS_MAP_MOVABLE != true) {
                    mBtnDrawState.setText("Lock");
                    IS_MAP_MOVABLE = true;
                } else {
                    mBtnDrawState.setText("UnLock");
                    IS_MAP_MOVABLE = false;
                }
            }
        });

        mBtnDisplayArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                double area = SphericalUtil.computeArea(mLatLng);
                Toast.makeText(getApplicationContext(),"Area:"+ AreaUtil.getFormatedArea(area) ,Toast.LENGTH_LONG).show();
            }
        });

        mFrameLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
        public boolean onTouch(View v, MotionEvent event) {
            float x = event.getX();
            float y = event.getY();

            int x_co = Math.round(x);
            int y_co = Math.round(y);

                final Projection projection = mMap.getProjection();
                Point x_y_points = new Point(x_co, y_co);

            LatLng latLng = mMap.getProjection().fromScreenLocation(x_y_points);
                final double latitude = latLng.latitude;

                final double longitude = latLng.longitude;

            int mEventAction = event.getAction();
            switch (mEventAction) {
                case MotionEvent.ACTION_DOWN:
                    // finger touches the screen
                    mLatLng.add(new LatLng(latitude, longitude));

                case MotionEvent.ACTION_MOVE:
                    // finger moves on the screen
                    mLatLng.add(new LatLng(latitude, longitude));

                case MotionEvent.ACTION_UP:
                    // finger leaves the screen
                    Draw_Map();
                    break;
            }

            if (IS_MAP_MOVABLE == true) {
                return true;

            } else {
                return false;
            }
        }
        });
    }


    /**
     *
     * Displays polygons on map
     */

    public void Draw_Map() {
        final PolygonOptions rectOptions = new PolygonOptions();
        rectOptions.addAll(mLatLng);
        rectOptions.strokeColor(Color.BLUE);
        rectOptions.strokeWidth(7);
        rectOptions.fillColor(Color.CYAN);
        mMap.addPolygon(rectOptions);
    }


    /**
     * Displays marker in between the polygons
     *
     * Following code is Taken reference from
     * http://stackoverflow.com/questions/30173397/show-text-on-polygon-android-google-map-v2
     */
    public Marker addText(final Context context, final GoogleMap map,
                          final LatLng location, final String text, final int padding,
                          final int fontSize) {
        Marker marker = null;

        if (context == null || map == null || location == null || text == null
                || fontSize <= 0) {
            return marker;
        }

        final TextView textView = new TextView(context);
        textView.setText(text);
        textView.setTextSize(fontSize);

        final Paint paintText = textView.getPaint();

        final Rect boundsText = new Rect();
        paintText.getTextBounds(text, 0, textView.length(), boundsText);
        paintText.setTextAlign(Paint.Align.CENTER);

        final Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        final Bitmap bmpText = Bitmap.createBitmap(boundsText.width() + 2
                * padding, boundsText.height() + 2 * padding, conf);

        final Canvas canvasText = new Canvas(bmpText);
        paintText.setColor(Color.BLACK);

        canvasText.drawText(text, canvasText.getWidth() / 2,
                canvasText.getHeight() - padding - boundsText.bottom, paintText);

        final MarkerOptions markerOptions = new MarkerOptions()
                .position(location)
                .anchor(0.5f, 1);

        marker = map.addMarker(markerOptions);

        return marker;
    }



}
