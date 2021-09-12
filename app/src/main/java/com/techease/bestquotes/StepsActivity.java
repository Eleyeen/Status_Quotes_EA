package com.techease.bestquotes;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;

import com.techease.bestquotes.Model.Step;
import com.techease.bestquotes.Model.StepImage;
import com.techease.bestquotes.Model.Wolf;
import com.makeramen.roundedimageview.RoundedImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class StepsActivity extends AppCompatActivity implements View.OnClickListener {


    private static StepsActivity instance;

    private ArrayList<Wolf> wolfList;
    private ArrayList<StepImage> stepsImagesList;

    private ArrayList<Step> stepList;
    private int ID_Wolf_Current;
    private String Title_Current, quote;

    private RoundedImageView ivWolfStep;
    private TextView tvStepsContents;
    private TextView tvStepsNumber;
    private Button btnNext, btnPrevious;
    private int step_position = 0;


    // Facebook Ads
    private final String TAG = StepsActivity.class.getSimpleName();
    private InterstitialAd interstitialAdFacebook;
    private AdView adView;
    private Handler handler;

    RoundedImageView rivShare, rivCopy;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_steps);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


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

        instance = this;

        Bundle bundle = getIntent().getExtras();
        ID_Wolf_Current = bundle.getInt("ID_Wolf");
        Title_Current = bundle.getString("Title");

        getSupportActionBar().setTitle(Title_Current);

        //Toast.makeText(getApplicationContext(), String.valueOf(ID_Wolf), Toast.LENGTH_LONG).show();

        tvStepsContents = findViewById(R.id.tv_steps_contents);

        tvStepsNumber = findViewById(R.id.tvStepsNumber);
        btnNext = findViewById(R.id.btnNext);
        btnPrevious = findViewById(R.id.btnPrevious);
        rivShare = findViewById(R.id.riv_share);
        rivCopy = findViewById(R.id.riv_copy);

        btnNext.setOnClickListener(this);
        btnPrevious.setOnClickListener(this);
        rivShare.setOnClickListener(this);
        rivCopy.setOnClickListener(this);

        loadSteps(ID_Wolf_Current);

        changeImage();

        try {
            Typeface face = Typeface.createFromAsset(getApplicationContext().getAssets(), "roboto_medium.ttf");
            tvStepsNumber.setTypeface(face);
        } catch (Exception ex) {

        }


        interstitialAdFacebook = new InterstitialAd(this, getString(R.string.facebook_interstitial_id));


        interstitialAdFacebook.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {

            }

            @Override
            public void onInterstitialDismissed(Ad ad) {

            }

            @Override
            public void onError(Ad ad, AdError adError) {

            }

            @Override
            public void onAdLoaded(Ad ad) {

                if (interstitialAdFacebook == null || !interstitialAdFacebook.isAdLoaded()) {

                } else if (interstitialAdFacebook.isAdInvalidated()) {

                } else {
                    interstitialAdFacebook.show();
                }

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });


        //showAdWithDelay();


    }

    public static StepsActivity getInstance() {
        return instance;
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

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public int loadSteps(int id_wolf_current) {

        wolfList = new ArrayList<Wolf>();
        stepsImagesList = new ArrayList<StepImage>();

        stepList = new ArrayList<Step>();

        //wolfList.clear();

        try {
            // get JSONObject from JSON file
            JSONObject jsonObject = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named wolves
            JSONArray jsonArrayWolf = jsonObject.getJSONArray(getResources().getString(R.string.name_json));
            // implement for loop for getting wolves list data
            for (int i = 0; i < jsonArrayWolf.length(); i++) {

                //stepsImagesList.clear();

                // create a JSONObject for fetching single wolf data
                JSONObject wolfDetail = jsonArrayWolf.getJSONObject(i);
                Log.d("Details : ", wolfDetail.getString("title"));

                // fetch id, title, logo, niveau
                int ID_Wolf = wolfDetail.getInt("id");

                // fetch JSONArray named steps
                JSONArray jsonArraySteps = wolfDetail.getJSONArray("steps");
                // implement for loop for getting steps image list data

                if (id_wolf_current == ID_Wolf) {

                    for (int j = 0; j < jsonArraySteps.length(); j++) {
                        // create a object for getting steps data from JSONObject
                        JSONObject jsonObjectSteps = jsonArraySteps.getJSONObject(j);
                        // fetch image
                        String imageStep = jsonObjectSteps.getString("image");
                        // fetch image it in arraylist
                        stepsImagesList.add(new StepImage(imageStep));
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //Toast.makeText(getApplicationContext(), String.valueOf(stepsImagesList.size()), Toast.LENGTH_LONG).show();

        int ID = 1;

        for (int i = 0; i < stepsImagesList.size(); i++) {

            stepList.add(new Step(ID, stepsImagesList.get(i).getImage(), id_wolf_current));

            ID++;
        }

        return stepList.size();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btnNext:

                step_position++;

                if (step_position % 3 == 0) {

                    try {
                        interstitialAdFacebook.loadAd();

                    } catch (Exception e) {

                    }


                }

                if (step_position == stepList.size()) {
                    step_position--;              // you can leave it this way or improve it later
                    btnNext.setAlpha((float) 0.4);
                    btnPrevious.setAlpha((float) 1.0);
                } else {
                    btnNext.setAlpha((float) 1.0);
                    if (step_position > 0) {
                        btnPrevious.setAlpha((float) 1.0);
                    }
                }

                changeImage();
                break;

            case R.id.btnPrevious:

                step_position--;

                if (step_position == -1) {
                    step_position = 0;          // you can leave it this way or improve it later
                    btnPrevious.setAlpha((float) 0.4);
                    btnNext.setAlpha((float) 1.0);
                } else {
                    btnPrevious.setAlpha((float) 1.0);
                    if (step_position < stepList.size()) {
                        btnNext.setAlpha((float) 1.0);
                    }
                }

                changeImage();
                break;
            case R.id.riv_share:
                shareContent(quote);
                break;
            case R.id.riv_copy:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copy", quote);
                Toast.makeText(this, "Added to clipboard", Toast.LENGTH_SHORT).show();
                clipboard.setPrimaryClip(clip);
        }

    }

    public void changeImage() {

        try {


            tvStepsNumber.setText(new StringBuilder("Quote : ")
                    .append(stepList.get(step_position).getID())
                    .append(" / ")
                    .append(stepList.size()));
            quote = stepList.get(step_position).getImageStep();
            tvStepsContents.setText(new StringBuilder().append(quote));


            if (step_position == stepList.size() - 1) {
                btnNext.setAlpha((float) 0.4);
                if (step_position > 0) {
                    btnPrevious.setAlpha((float) 1.0);
                }
                if (step_position <= 0) {
                    btnPrevious.setAlpha((float) 0.4);
                }
            } else {
                btnNext.setAlpha((float) 1.0);
                if (step_position > 0) {
                    btnPrevious.setAlpha((float) 1.0);
                }
                if (step_position <= 0) {
                    btnPrevious.setAlpha((float) 0.4);
                }
            }
        } catch (Exception ex) {

        }
    }

    private void shareContent(String quote) {
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Invite");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, quote);
        startActivity(Intent.createChooser(sharingIntent, "Invite friends"));
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

    @Override
    protected void onDestroy() {
        if (interstitialAdFacebook != null) {
            interstitialAdFacebook.destroy();
        }
        super.onDestroy();
    }

    // Functions Facebook Ads

    private void showAdWithDelay() {
        /**
         * Here is an example for displaying the ad with delay;
         * Please do not copy the Handler into your project
         */
        // Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                // Check if interstitialAd has been loaded successfully
                if (interstitialAdFacebook == null || !interstitialAdFacebook.isAdLoaded()) {
                    return;
                }
                // Check if ad is already expired or invalidated, and do not show ad if that is the case. You will not get paid to show an invalidated ad.
                if (interstitialAdFacebook.isAdInvalidated()) {
                    return;
                }


                try {
                    interstitialAdFacebook.loadAd();

                } catch (Exception e) {

                }

            }
        }, 1000 * 60 * 1); // Show the ad after 15 minutes
    }

}
