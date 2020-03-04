package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery forecastQuery = new ForecastQuery();
        forecastQuery.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=xml&units=metric",
                "http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");  //Type 1
    }

                                                //Type1     Type2   Type3
    private class ForecastQuery extends AsyncTask< String, Integer, String>
    {
        String minTemperature = "";
        String maxTemperature = "";
        String currentTemperature = "";
        String icon = "";
        String uvRating;
        TextView uvRatingView = findViewById(R.id.textViewUvRating);
        ImageView weatherIcon = findViewById(R.id.imageViewCurrentWeather);
        TextView currentTemperatureView = findViewById(R.id.textViewCurrentTemperature);
        TextView minTemperatureView = findViewById(R.id.textViewMinTemperature);
        TextView maxTemperatureView = findViewById(R.id.textViewMaxTemperature);
        ProgressBar loadingImage = findViewById(R.id.progressBar);
        Bitmap image = null;

        //Type3                Type1
        public String doInBackground(String ... args)
        {
            Log.i("DoInBackground", "Starting DoInBackground");

            try {

                //create a URL object of what server to contact:
                URL weatherUrl = new URL(args[0]);

                //open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) weatherUrl.openConnection();

                //wait for data:
                InputStream response = urlConnection.getInputStream();

                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(false);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( response  , "UTF-8");

                int eventType = xpp.getEventType(); //The parser is currently at START_DOCUMENT

                while(eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG)
                    {
                        //If you get here, then you are pointing at a start tag
                        if(xpp.getName().equals("temperature"))
                        {
                            //If you get here, then you are pointing to a <Weather> start tag
                            currentTemperature = "Current Weather: " + xpp.getAttributeValue(null,    "value");
                            publishProgress(25);
                            minTemperature = "Min Temperature: " + xpp.getAttributeValue(null, "min");
                            publishProgress(50);
                            maxTemperature = "Max Temperature: " + xpp.getAttributeValue(null, "max");
                            publishProgress(75);
                        }

                        else if(xpp.getName().equals("weather"))
                        {
                            icon = xpp.getAttributeValue(null, "icon"); // this will run for <AMessage message="parameter" >
                            URL imageUrl = new URL("http://openweathermap.org/img/w/" + icon + ".png");
                            HttpURLConnection connection = (HttpURLConnection) imageUrl.openConnection();
                            connection.connect();
                            int responseCode = connection.getResponseCode();
                            Log.i("Creating Bitmap", "Response Code: " + responseCode);
                            if (responseCode == 200) {
                                image = BitmapFactory.decodeStream(connection.getInputStream());
                            }

                            Log.i("FileName", "File Name: " + icon + ".png");
                            if(fileExistance(icon + ".png")){
                                Log.i("FileExists", "The file already exists");
                                FileInputStream fis = null;
                                try {    fis = openFileInput(icon + ".png");   }
                                catch (FileNotFoundException e) {    e.printStackTrace();  }
                                Bitmap bm = BitmapFactory.decodeStream(fis);
                                publishProgress(100);

                            } else {
                                Log.i("FileNotExists", "The file is being downloaded");
                                FileOutputStream outputStream = openFileOutput( icon + ".png", Context.MODE_PRIVATE);
                                image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                                outputStream.flush();
                                outputStream.close();
                                publishProgress(100);

                            }

                        }
                    }
                    eventType = xpp.next(); //move to the next xml event and store it in a variable
                }

                //create a URL object of what server to contact:
                URL uvRatingUrl = new URL(args[1]);

                //open the connection
                urlConnection = (HttpURLConnection) uvRatingUrl.openConnection();

                //wait for data:
                response = urlConnection.getInputStream();

                //JSON reading:
                //Build the entire string response:
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString(); //result is the whole string


                // convert string to JSON:
                JSONObject uvReport = new JSONObject(result);

                //get the double associated with "value"
                uvRating = "UV Rating: " + uvReport.getDouble("value");

                Log.i("MainActivity", "The uv is now: " + uvRating) ;

            }
            catch (Exception e)
            {
                Log.e("Error", e.getMessage());
            }
            Log.i("Weather", currentTemperature + minTemperature + maxTemperature + " Icon Name: " + icon + uvRating);
            return "Done";
        }

        //Type 2
        public void onProgressUpdate(Integer ... args)
        {
            loadingImage.setVisibility(View.VISIBLE);
            loadingImage.setProgress(args[0]);
            Log.i("onProgressUpdate", "Progress Update");
        }
        //Type3
        public void onPostExecute(String fromDoInBackground)
        {
            weatherIcon.setImageBitmap(image);
            currentTemperatureView.setText(currentTemperature);
            minTemperatureView.setText(minTemperature);
            maxTemperatureView.setText(maxTemperature);
            loadingImage.setVisibility(View.INVISIBLE);
            uvRatingView.setText(uvRating);
            Log.i("HTTP", fromDoInBackground);
        }
    }

    public boolean fileExistance(String fname){
        Log.i("fileExistance", "Inside fileExistance Method");
        File file = getBaseContext().getFileStreamPath(fname);
        Log.i("filePath", getFileStreamPath(fname).toString());
        return file.exists();
    }
}