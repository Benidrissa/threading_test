package com.drjikasoft.threading_test;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {
    private Button mButton ;
    private Button mButton2 ;
    private ImageView mImageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton = (Button)findViewById(R.id.btnDownload);
        mImageView = (ImageView)findViewById(R.id.image);

        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("Entered onClick","");

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        final Bitmap bmp = loadImageFromNetwork("http://homepages.ius.edu/RWISMAN/C490/html/Android-URLImage-1.png");
                        mImageView.post(new Runnable() {
                            @Override
                            public void run() {
                                updateImage(bmp);
                            }
                        });
                    }
                }).start();
            }
        });
        mButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new     DownloadImageTask()
                        .execute("http://homepages.ius.edu/RWISMAN/C490/html/Android-URLImage-1.png");
            }
        });
    }

    public Bitmap loadImageFromNetwork(final String lien){
        Bitmap bmp = null;
        try{
            URL url = new URL( lien );
            HttpURLConnection conn =  (HttpURLConnection)url.openConnection();
            conn.connect();
            InputStream is = conn.getInputStream();
            bmp = BitmapFactory.decodeStream(is);           // Download image to bmp
        }
        catch (Exception e) {
            System.out.println("getImage failure:"+e);
            //e.printStackTrace();

            //some change

        }
        return bmp;
    }

    public class DownloadImageTask extends AsyncTask<String,Integer,Bitmap>{
        protected Bitmap doInBackground(String... url){
            return loadImageFromNetwork(url[0]);
        }

        protected void onPostExecute(Bitmap bmp){
            updateImage(bmp);
        }
    }

    private void updateImage(Bitmap bmp){
        mImageView.setImageBitmap(bmp);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
}
