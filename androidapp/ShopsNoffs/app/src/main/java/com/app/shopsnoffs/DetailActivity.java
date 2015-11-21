package com.app.shopsnoffs;

import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.shopsnoffs.model.ShopsNoffsDatum;
import com.braintreepayments.api.dropin.BraintreePaymentActivity;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 17.10.15.
 */
public class DetailActivity extends AppCompatActivity {
    private ViewPager pager;
    private TextView tv_title, tv_discount,tv_orig, tv_product;
    private CustomPagerAdapter adapter;
    private List<String> imgUrls;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ShopsNoffsDatum data = (ShopsNoffsDatum) getIntent().getExtras().getSerializable("data");

        if(data == null){finish();}

        getSupportActionBar().setTitle(data.getStoreName());

        imgUrls = new ArrayList<>();
        imgUrls.add(data.getUrl());

        initViews();


        tv_title.setText(data.getName());
        tv_discount.setText("ONLY " + String.valueOf(data.getDiscountedPrice()) + ",00€ (" + String.valueOf(data.getDiscount()) + "% off)");
        tv_product.setText("at " + data.getStoreName());

        StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();

        tv_orig.setText(data.getOrigPrice()+"€", TextView.BufferType.SPANNABLE);
        Spannable spannable = (Spannable) tv_orig.getText();
        spannable.setSpan(STRIKE_THROUGH_SPAN, 0, tv_orig.getText().length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        final List<Double> loc = data.getLocation();

        Button b = (Button) findViewById(R.id.btnMaps);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("GEO LOCATION","geo:"+loc.get(0)+","+loc.get(1)+"?z=9");
                //Uri gmmIntentUri = Uri.parse("geo:"+loc.get(0)+","+loc.get(1)+"?z=9");
                //geo:latitude,longitude?z=zoom

                //google.navigation:q=latitude,longitude
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+loc.get(0)+","+loc.get(1));
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                if (mapIntent.resolveActivity(getPackageManager()) != null) {
                    startActivity(mapIntent);
                }
            }
        });
    }

    private void initViews(){
        tv_title = (TextView) findViewById(R.id.textTitle);
        tv_product = (TextView) findViewById(R.id.textProduct);
        tv_discount = (TextView) findViewById(R.id.textdiscount);
        tv_orig = (TextView) findViewById(R.id.textorig);

        pager = (ViewPager) findViewById(R.id.pager);

        CustomPagerAdapter.OnClickListener listener = new CustomPagerAdapter.OnClickListener() {
            @Override
            public void onClick(int position) {

            }
        };

        adapter = new CustomPagerAdapter(this,listener,imgUrls, Glide.with(this));
        pager.setAdapter(adapter);
    }

    public void onBraintreeSubmit(View v) {
        Intent intent = new Intent(this, BraintreePaymentActivity.class);
        intent.putExtra(BraintreePaymentActivity.EXTRA_CLIENT_TOKEN, "eyJ2ZXJzaW9uIjoyLCJhdXRob3JpemF0aW9uRmluZ2VycHJpbnQiOiJiNjRlNDY5YmYzYTU4MTRjMTVhMzE5ZWViMGM4NWY4OThkMWE5NjgwYThhNzI5YzE5NWJjMTEzODdhMWFiODZjfGNyZWF0ZWRfYXQ9MjAxNS0xMC0xOFQwMTozMDoyNC42NzkzODIzNDkrMDAwMFx1MDAyNm1lcmNoYW50X2lkPTM0OHBrOWNnZjNiZ3l3MmJcdTAwMjZwdWJsaWNfa2V5PTJuMjQ3ZHY4OWJxOXZtcHIiLCJjb25maWdVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi9jbGllbnRfYXBpL3YxL2NvbmZpZ3VyYXRpb24iLCJjaGFsbGVuZ2VzIjpbXSwiZW52aXJvbm1lbnQiOiJzYW5kYm94IiwiY2xpZW50QXBpVXJsIjoiaHR0cHM6Ly9hcGkuc2FuZGJveC5icmFpbnRyZWVnYXRld2F5LmNvbTo0NDMvbWVyY2hhbnRzLzM0OHBrOWNnZjNiZ3l3MmIvY2xpZW50X2FwaSIsImFzc2V0c1VybCI6Imh0dHBzOi8vYXNzZXRzLmJyYWludHJlZWdhdGV3YXkuY29tIiwiYXV0aFVybCI6Imh0dHBzOi8vYXV0aC52ZW5tby5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIiwiYW5hbHl0aWNzIjp7InVybCI6Imh0dHBzOi8vY2xpZW50LWFuYWx5dGljcy5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tIn0sInRocmVlRFNlY3VyZUVuYWJsZWQiOnRydWUsInRocmVlRFNlY3VyZSI6eyJsb29rdXBVcmwiOiJodHRwczovL2FwaS5zYW5kYm94LmJyYWludHJlZWdhdGV3YXkuY29tOjQ0My9tZXJjaGFudHMvMzQ4cGs5Y2dmM2JneXcyYi90aHJlZV9kX3NlY3VyZS9sb29rdXAifSwicGF5cGFsRW5hYmxlZCI6dHJ1ZSwicGF5cGFsIjp7ImRpc3BsYXlOYW1lIjoiQWNtZSBXaWRnZXRzLCBMdGQuIChTYW5kYm94KSIsImNsaWVudElkIjpudWxsLCJwcml2YWN5VXJsIjoiaHR0cDovL2V4YW1wbGUuY29tL3BwIiwidXNlckFncmVlbWVudFVybCI6Imh0dHA6Ly9leGFtcGxlLmNvbS90b3MiLCJiYXNlVXJsIjoiaHR0cHM6Ly9hc3NldHMuYnJhaW50cmVlZ2F0ZXdheS5jb20iLCJhc3NldHNVcmwiOiJodHRwczovL2NoZWNrb3V0LnBheXBhbC5jb20iLCJkaXJlY3RCYXNlVXJsIjpudWxsLCJhbGxvd0h0dHAiOnRydWUsImVudmlyb25tZW50Tm9OZXR3b3JrIjp0cnVlLCJlbnZpcm9ubWVudCI6Im9mZmxpbmUiLCJ1bnZldHRlZE1lcmNoYW50IjpmYWxzZSwiYnJhaW50cmVlQ2xpZW50SWQiOiJtYXN0ZXJjbGllbnQzIiwiYmlsbGluZ0FncmVlbWVudHNFbmFibGVkIjp0cnVlLCJtZXJjaGFudEFjY291bnRJZCI6ImFjbWV3aWRnZXRzbHRkc2FuZGJveCIsImN1cnJlbmN5SXNvQ29kZSI6IlVTRCJ9LCJjb2luYmFzZUVuYWJsZWQiOmZhbHNlLCJtZXJjaGFudElkIjoiMzQ4cGs5Y2dmM2JneXcyYiIsInZlbm1vIjoib2ZmIn0=");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (resultCode == BraintreePaymentActivity.RESULT_OK) {
                String paymentMethodNonce = data.getStringExtra(BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE);
                //postNonceToServer(paymentMethodNonce);
            }
        }
    }

    @Override
    public void onBackPressed() {
        supportFinishAfterTransition();
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
        if (id == android.R.id.home) {
            supportFinishAfterTransition();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
