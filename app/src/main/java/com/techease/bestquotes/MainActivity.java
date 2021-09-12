package com.techease.bestquotes;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAdListener;
import com.techease.bestquotes.Model.StepImage;
import com.techease.bestquotes.Model.Wolf;
import com.techease.bestquotes.Utils.Common;


import com.makeramen.roundedimageview.RoundedTransformationBuilder;
import com.squareup.picasso.Transformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {


    private GridView gridWolf;
    private ArrayList<Wolf> wolfList;
    private ArrayList<StepImage> stepsImagesList;
    private ProgressBar progressBar;
    private RelativeLayout constraintLayout;

    // Facebook Ads
    private final String TAG = StepsActivity.class.getSimpleName();
    private com.facebook.ads.InterstitialAd mInterstitialAdFacebook;
    private Handler handler;
    private AdView adView;

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        printKeyHach();


        adView = new com.facebook.ads.AdView(this, getString(R.string.facebook_banner_id), AdSize.BANNER_HEIGHT_50);

        // Find the Ad Container
        LinearLayout adContainer = findViewById(R.id.banner_container);

        // Add the ad view to your activity layout
        adContainer.addView(adView);

        adView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback

            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Ad loaded callback
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
            }
        });

        // Request an ad
        adView.loadAd();


        // Ads Facebook Interstitial
        mInterstitialAdFacebook = new com.facebook.ads.InterstitialAd(MainActivity.this, getResources().getString(R.string.facebook_interstitial_id));
        // Set listeners for the Interstitial Ad
        mInterstitialAdFacebook.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                mInterstitialAdFacebook.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });


        progressBar = findViewById(R.id.progressBar);
        gridWolf = findViewById(R.id.gridWolf);

        try {

            Common.stepsActivity = new StepsActivity();

            gridWolf.setAdapter(new MainActivity.ListResources(MainActivity.this));

            if (Common.isConnectingToInternet(MainActivity.this)) {

                gridWolf.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
            } else {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.please_on_internet), Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {

        }

        //loadWolves();

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        changeColor();

    }

    private void changeColor() {
        TypedArray ta = getResources().obtainTypedArray(R.array.androidcolors);
        int[] colors = new int[ta.length()];
        for (int i = 0; i < ta.length(); i++) {
            colors[i] = ta.getColor(i, 0);
            Log.d("zma colors", String.valueOf(colors[i]));

        }
        ta.recycle();
    }


    private void printKeyHach() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo("com.learntodraw.drawcriminaltattoo", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream inputStream = getApplicationContext().getAssets().open(getResources().getString(R.string.name_file));
            int size = inputStream.available();
            byte[] buffer = new byte[size];
            inputStream.read(buffer);
            inputStream.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    class ListResources extends BaseAdapter {

        Context context;

        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        ListResources(Context context) {

            this.context = context;

            wolfList = new ArrayList<Wolf>();
            stepsImagesList = new ArrayList<StepImage>();

            //wolfList.clear();

            try {
                // get JSONObject from JSON file
                JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
                // fetch JSONArray named wolves
                JSONArray jsonArrayWolf = jsonObject.getJSONArray(getResources().getString(R.string.name_json));
                // implement for loop for getting wolves list data
                for (int i = 0; i < jsonArrayWolf.length(); i++) {

                    stepsImagesList.clear();

                    // create a JSONObject for fetching single wolf data
                    JSONObject wolfDetail = jsonArrayWolf.getJSONObject(i);
                    Log.d("Details : ", wolfDetail.getString("title"));

                    // fetch id, title, logo, niveau
                    int ID = wolfDetail.getInt("id");
                    String Title = wolfDetail.getString("title");
//                    String ImageCircleRectangle = wolfDetail.getString("logo");
                    String Level = wolfDetail.getString("niveau");
                    String color = wolfDetail.getString("backgroundColor");

                    // fetch JSONArray named steps
                    JSONArray jsonArraySteps = wolfDetail.getJSONArray("steps");
                    // implement for loop for getting steps image list data
                    for (int j = 0; j < jsonArraySteps.length(); j++) {

                        // create a object for getting steps data from JSONObject
                        JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(j);
                        // fetch image
                        String imageStep = jsonObjectSteps.getString("image");

                        // fetch image it in arraylist
                        stepsImagesList.add(new StepImage(imageStep));
                    }

                    //Add your values in your `ArrayList` as below:
                    wolfList.add(new Wolf(ID,
                            Title,
                            stepsImagesList.size(),
                            Level,
                            stepsImagesList, color));

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Toast.makeText(getApplicationContext(), String.valueOf(wolfList.size()), Toast.LENGTH_LONG).show();

        }

        @Override
        public int getCount() {
            return wolfList.size();
        }

        @Override
        public Object getItem(int position) {
            return wolfList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater layoutInflater = getLayoutInflater();
            final View wolfItem = layoutInflater.inflate(R.layout.item_wolf, parent, false);

            TextView tvDraw = wolfItem.findViewById(R.id.tvDraw);
            RelativeLayout mainLayout = wolfItem.findViewById(R.id.btnStart);
            TextView tvStepsNumber = wolfItem.findViewById(R.id.tvStepsNumber);
            constraintLayout = wolfItem.findViewById(R.id.btnStart);
//            constraintLayout.setBackgroundColor(Color.RED);

            try {
                Typeface face = Typeface.createFromAsset(context.getAssets(), "roboto_medium.ttf");
                tvDraw.setTypeface(face);
                tvStepsNumber.setTypeface(face);

            } catch (Exception ex) {

            }

            try {

                //final Wolf wolf = wolfList.get(position);

                tvDraw.setText(wolfList.get(position).getTitle());
                constraintLayout.setBackgroundColor(Color.parseColor(wolfList.get(position).getColor()));
                tvStepsNumber.setText(new StringBuilder()
                        .append(wolfList.get(position).getStepsNumber())
                        .append(" ")
                        .append("Quotes"));

                Transformation transformation = new RoundedTransformationBuilder()
                        .borderColor(Color.WHITE)
                        .borderWidthDp(0)
                        .cornerRadiusDp(10)
                        .oval(false)
                        .build();

            } catch (Exception ex) {

            }



            mainLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int ID_Wolf_Current = wolfList.get(position).getID();
                    String Title_Current = wolfList.get(position).getTitle();

                    Intent intent = new Intent(MainActivity.this, StepsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ID_Wolf", ID_Wolf_Current);
                    bundle.putString("Title", Title_Current);
                    intent.putExtras(bundle);
                    startActivity(intent);

                    // show ads mInterstitialAd Google
                    /*
                    if (mInterstitialAdGoogle.isLoaded()) {
                        mInterstitialAdGoogle.show();
                    }
                    else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                    */

                    // show ads mInterstitialAd Google
                    if (mInterstitialAdFacebook.isAdLoaded()) {
                        mInterstitialAdFacebook.loadAd();
                    } else {
                        Log.d("TAG", "The interstitial wasn't loaded yet.");
                    }
                }
            });

            return wolfItem;
        }
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item_wolf clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app));
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_app_via)));
        }
        if (id == R.id.action_rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_app_store)));
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.

        int id = item.getItemId();

        if (id == R.id.nav_share) {
            Intent sharingIntent = new Intent(Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(Intent.EXTRA_TEXT, getResources().getString(R.string.share_app));
            startActivity(Intent.createChooser(sharingIntent, getResources().getString(R.string.share_app_via)));
        }
        if (id == R.id.nav_rate) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getResources().getString(R.string.url_app_store)));
            startActivity(intent);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
