package com.example.river_island;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private ProgressDialog pDialog;
    ListView mListView;
    Button show_refresh_btn;
    private static String url = "https://static-ri.ristack-3.nn4maws.net/v1/plp/en_gb/2506/products.json";
    ArrayList<Product> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started.");
        mListView = (ListView) findViewById(R.id.listView);
        productsList = new ArrayList<>();
        new GetProducts().execute();
        show_refresh_btn = (Button) findViewById(R.id.show_refresh_btn);
    }

    //Async task class to get json by making HTTP call//
    private class GetProducts extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //Progress dialog//
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();
            // Making a request to url and getting response//
            String jsonStr = sh.makeServiceCall(url);
            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    JSONObject jsonObj = new JSONObject(jsonStr);
                    // Getting JSON//
                    JSONArray products = jsonObj.getJSONArray("Products");
                    // looping through all products//
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        String cost = c.getString("cost");
                        String dollar_cost = c.getString("costUSD");
                        String euro_cost = c.getString("costEUR");
                        String product_name = c.getString("name");
                        String product_id = c.getString("prodid");
                        boolean isTrending = c.getBoolean("isTrending");

                        if (isTrending == true)
                        {
                            Product product = new Product(product_name, "TRENDING", "£ " + cost + ", $ " + dollar_cost + ", € " + euro_cost, "https://riverisland.scene7.com/is/image/RiverIsland/" + product_id + "_main");
                            productsList.add(product);
                        }
                        else
                        {
                            Product product = new Product(product_name,"", "£ " + cost + ", $ " + dollar_cost + ", € " + euro_cost, "https://riverisland.scene7.com/is/image/RiverIsland/" + product_id + "_main");
                            productsList.add(product);
                        }

                        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String imageUri =  productsList.get(position).getImgURL() ;
                                Intent intent = new Intent(view.getContext(), Activity_image.class);
                                intent.putExtra("imageurl",imageUri);
                                finish();
                                startActivity(intent);
                            }
                        });
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });
                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check internet connection for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();

            //Updating parsed JSON data into ListView//
            show_refresh_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ProductListAdapter adapter = new ProductListAdapter(MainActivity.this, R.layout.adapter_view_layout, productsList);
                    mListView.setAdapter(adapter);
                }});
            mListView.setAdapter(null);
        }
    }
}
