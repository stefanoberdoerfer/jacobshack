package com.app.shopsnoffs;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.app.shopsnoffs.model.Json;
import com.app.shopsnoffs.model.ShopsNoffsData;
import com.app.shopsnoffs.model.ShopsNoffsDatum;
import com.app.shopsnoffs.network.CustomRequestListener;
import com.app.shopsnoffs.network.Request;
import com.app.shopsnoffs.network.RequestType;
import com.app.shopsnoffs.network.Successhandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

public class MainActivity extends AppCompatActivity implements Successhandler{

    private AutoCompleteTextView autoText;
    private ArrayAdapter<String> adapter;
    private ImageView img_big, img1, img2, img3, img4;
    private TextView tv_big, tvb1,tvb2,tvb3,tvb4;
    private ProgressBar pb;
    private TextView[] tvs = new TextView[5];
    private ImageView[] ivs = new ImageView[5];
    private View[] cons = new View[5];
    private View conB, con1, con2, con3, con4;

    private SpiceManager spiceManager = new SpiceManager(
            JacksonSpringAndroidSpiceService.class);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();

        makeRequest();

    }


    public void makeRequest(){
        Request offerrequest = new Request(RequestType.getAllOffers,null);
        spiceManager.execute(offerrequest, new CustomRequestListener(this));
    }

    private void initViews(){
        autoText = (AutoCompleteTextView) findViewById(R.id.autoTextView);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, getResources().getStringArray(R.array.suggestions));
        autoText.setThreshold(1);
        autoText.setAdapter(adapter);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        autoText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH ||
                        actionId == EditorInfo.IME_ACTION_DONE ||
                        event.getAction() == KeyEvent.ACTION_DOWN &&
                                event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
                    fab.performClick();
                    return true;
                }
                return false;
            }
        });

        pb = (ProgressBar) findViewById(R.id.progressbar);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if(autoText.getText().toString().isEmpty()) {
                    Snackbar.make(view, "Please type in words to search for!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else{*/
                    Intent i = new Intent(MainActivity.this,MapsActivity.class);

                    if(!autoText.getText().toString().isEmpty()){
                        Bundle extras = new Bundle();
                        extras.putString("query",autoText.getText().toString());
                        i.putExtras(extras);
                    }

                    startActivity(i);
                //}
            }
        });

        img_big = (ImageView) findViewById(R.id.image_big);
        img1 = (ImageView) findViewById(R.id.image1);
        img2 = (ImageView) findViewById(R.id.image2);
        img3 = (ImageView) findViewById(R.id.image3);
        img4 = (ImageView) findViewById(R.id.image4);

        ivs[0] = img_big;
        ivs[1] = img1;
        ivs[2] = img2;
        ivs[3] = img3;
        ivs[4] = img4;

        tv_big = (TextView) findViewById(R.id.counter_big);
        tvb1 = (TextView) findViewById(R.id.counter1);
        tvb2 = (TextView) findViewById(R.id.counter2);
        tvb3 = (TextView) findViewById(R.id.counter3);
        tvb4 = (TextView) findViewById(R.id.counter4);

        tvs[0] = tv_big;
        tvs[1] = tvb1;
        tvs[2] = tvb2;
        tvs[3] = tvb3;
        tvs[4] = tvb4;

        conB = findViewById(R.id.badgecontainer_big);
        con1 = findViewById(R.id.badgecontainer1);
        con2 = findViewById(R.id.badgecontainer2);
        con3 = findViewById(R.id.badgecontainer3);
        con4 = findViewById(R.id.badgecontainer4);

        cons[0] = conB;
        cons[1] = con1;
        cons[2] = con2;
        cons[3] = con3;
        cons[4] = con4;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onSuccess(Json json) {
        pb.setVisibility(View.GONE);
        RequestManager rm = Glide.with(this);
        if(json instanceof ShopsNoffsData){
            Log.i("Networking","SUCCESS!");
            ShopsNoffsData data = (ShopsNoffsData) json;

            for(int i = 0; i<data.size() && i < 5;i++){
                final ShopsNoffsDatum s = data.get(i);
                rm.load(s.getUrl()).into(ivs[i]);
                tvs[i].setText(s.getDiscount() + "%");
                cons[i].setVisibility(View.VISIBLE);

                ivs[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(MainActivity.this, DetailActivity.class);
                        i.putExtra("data", s);
                        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            v.setTransitionName("picture");
                            ActivityOptionsCompat options = ActivityOptionsCompat.
                                    makeSceneTransitionAnimation(MainActivity.this, v, "picture");
                            startActivity(i, options.toBundle());
                        }else{
                            startActivity(i);
                        }
                    }
                });
            }
        }
    }

    @Override
    public void onFailure() {

    }

    @Override
    public void onStart() {
        spiceManager.start(this);
        super.onStart();
    }

    @Override
    public void onStop() {
        spiceManager.shouldStop();
        super.onStop();

    }

}
