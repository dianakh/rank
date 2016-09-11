package com.example.hp.rank;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;
import android.widget.Toast;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;


public class MainActivity extends AppCompatActivity {

    private RatingBar ratingBar;

    private TextView txtRatingValue;
    private TextView RatingValue;
    private TextView countValue;
    private TextView rateValue;
    private Button btnSubmit;

    private TextView recipe;
    String myJSON;
    private static final String TAG_RESULTS="result";
    private static final String TAG_rank = "rank";
    public int count=0;
    public float result1=0.0f;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addListenerOnRatingBar();
        personList = new ArrayList<HashMap<String,String>>();


    }









    public void addListenerOnRatingBar() {

        ratingBar = (RatingBar) findViewById(R.id.ratingBar);
        txtRatingValue = (TextView) findViewById(R.id.txtRatingValue);
        RatingValue = (TextView) findViewById(R.id.RatingValue);
        countValue = (TextView) findViewById(R.id.countValue);
       rateValue = (TextView) findViewById(R.id.rateValue);
        class GetDataJSON extends AsyncTask<String, Void, String>{

            @Override
            protected String doInBackground(String... params) {
                DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
                HttpPost httppost = new HttpPost("http://10.0.2.2/getrank.php");

                // Depends on your web service
                httppost.setHeader("Content-type", "application/json");

                InputStream inputStream = null;
                String result = null;
                try {
                    HttpResponse response = httpclient.execute(httppost);
                    HttpEntity entity = response.getEntity();

                    inputStream = entity.getContent();
                    // json is UTF-8 by default
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                    StringBuilder sb = new StringBuilder();

                    String line = null;
                    while ((line = reader.readLine()) != null)
                    {
                        sb.append(line + "\n");
                    }
                    result = sb.toString();
                } catch (Exception e) {
                    // Oops
                }
                finally {
                    try{if(inputStream != null)inputStream.close();}catch(Exception squish){}
                }
                return result;
            }

            @Override
            protected void onPostExecute(final String result){
                ratingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
                    public void onRatingChanged(RatingBar ratingBar, float rating,
                                                boolean fromUser) {
                myJSON=result;
                try {
                    JSONObject jsonObj = new JSONObject(myJSON);
                    peoples = jsonObj.getJSONArray(TAG_RESULTS);

                    for(int i=0;i<peoples.length();i++){
                        JSONObject c = peoples.getJSONObject(i);
                        String rank = c.getString(TAG_rank);
                        float rank_float = Float.parseFloat(rank);

                        RatingValue.setText(String.valueOf(rank_float));
                        countValue.setText(String.valueOf(count));
                        rateValue.setText(String.valueOf(rating));
                        count=count+1;
                        rank_float+=rating;
                        result1=rank_float/count;
                        ratingBar.setRating(result1);

                        txtRatingValue.setText(String.valueOf(result1));
                    }
                    //if rating value is changed,
                    //display the current rating value in the result (textview) automatically





                } catch (JSONException e) {
                    e.printStackTrace();
                }
                    }
                });
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute();


    }

    public void btnaddrank(View view) {
        TextView recipe = (TextView)findViewById(R.id.recipe);
        String name = recipe.getText().toString();
        String s = Float.toString(result1);
        addRank backgroundWorker = new addRank(this);
        backgroundWorker.execute(s,name);
    }



}
