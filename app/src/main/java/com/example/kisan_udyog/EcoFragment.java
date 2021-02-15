package com.example.kisan_udyog;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONObject;
import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EcoFragment extends Fragment{

    JSONObject data = null;
    private Context mcontext = getContext();
    private static final String TAG = "EcoFragment";
    private TextView result;
    private TextView temperature, rainfall, pH, humidity;
    Interpreter interpreter;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;
    int n;
    int hu;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eco, container, false);
        result=view.findViewById(R.id.outputModel);
       temperature=view.findViewById(R.id.tempval);
        rainfall=view.findViewById(R.id.rainfall);
        //pH=view.findViewById(R.id.pHvalue);
        humidity=view.findViewById(R.id.humidval);
        try {
            interpreter = new Interpreter(loadModelFile(),null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        requestLocation();
        return view;
    }

    //========================================================PREDICTION======================================================================================
    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor assetFileDescriptor = getActivity().getAssets().openFd("tfmodels.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel= fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length =assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset,length);
    }
    public String doInference(float a, float b, float c, float d){
        Log.d(TAG, "helllo"+a+" "+b);
        float[] input = new float[]{a, b,6,202};
        String[] Classes = new String[]{"pomegranate", "Tea", "wheat", "muskmelon", "mango", "Jute", "millet",
                "Adzuki Beans", "Pigeon Peas", "papaya", "Ground Nut", "banana", "Coffee", "grapes", "Kidney Beans",
                "maize", "orange", "watermelon", "Tobacco", "Chickpea", "Moth Beans", "Mung Bean", "Coconut", "Peas",
                "rice", "apple", "Lentil", "Cotton", "Sugarcane", "Rubber", "Black gram"};
        float[][] output = new float[1][31];
        List<Float> newOutput = new ArrayList<>();
        interpreter.run(input,output);
        for(int i=0;i<=30;i++){
            newOutput.add(output[0][i]);
        }
        Float max = Collections.max(newOutput);
        Log.d(TAG,"MAXX"+" "+newOutput);
        Log.d(TAG,"MAXX"+" "+newOutput.indexOf(max));
        return Classes[newOutput.indexOf(max)];
    }


    /////============================================================WEATHER API =============================================================================
    public void getJSON(final double lat, final double longi){
        new AsyncTask<Void, Void, JSONObject>() {
            @Override
            protected JSONObject doInBackground(Void... voids) {
                String str="http://api.openweathermap.org/data/2.5/weather?lat="+lat+"&lon="+longi+"&appid=d94cf62848d1ba15929b240e90183338";
                Log.d(TAG, "helllo"+str);
                URLConnection urlConn = null;
                BufferedReader bufferedReader = null;
                try
                {
                    URL url = new URL(str);
                    urlConn = url.openConnection();
                    bufferedReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
                    StringBuffer stringBuffer = new StringBuffer();
                    String line;
                    while ((line = bufferedReader.readLine()) != null)
                    {
                        stringBuffer.append(line);
                    }
                    data = new JSONObject(stringBuffer.toString());
                    final JSONObject temps = data.getJSONObject("main");
                     n=temps.getInt("temp")-273;
                    hu=temps.getInt("humidity");
                   getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Log.d("mine", String.valueOf(n));
                            temperature.setText(String.valueOf(n));
                            humidity.setText(String.valueOf(hu));
                            String f=doInference(n,hu, 6,452);
                            result.setText(f);
                            //Log.d("mine", String.valueOf(hu));
                        }
                    });
                    return new JSONObject(stringBuffer.toString());
                }
                catch(Exception ex)
                {
                    Log.e("App", "yourDataTask", ex);
                    return null;
                }
                finally
                {
                    if(bufferedReader != null)
                    {
                        try {
                            bufferedReader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            @Override
            protected void onPostExecute(JSONObject response) {
                if(response != null)
                {
                    Log.e(TAG, "GOT THE RESPONSE");
                }
            }
        }.execute();
    }


    /////=====================================================LOCATION==========================================================================================


    private void requestLocation() {
        if (ContextCompat.checkSelfPermission(getContext().getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            getCurrentLocation();
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            getCurrentLocation();
        } else {
            Toast.makeText(getActivity(), "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }
    private void getCurrentLocation() {
        final LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setPriority(locationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.getFusedLocationProviderClient(getActivity()).requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                LocationServices.getFusedLocationProviderClient(getActivity()).removeLocationUpdates(this);
                if (locationResult != null && locationResult.getLocations().size() > 0) {
                    int latestLocationIndex = locationResult.getLocations().size() - 1;
                    double latitude = locationResult.getLocations().get(latestLocationIndex).getLatitude();
                    double longitude = locationResult.getLocations().get(latestLocationIndex).getLongitude();
                    getJSON(latitude, longitude);
                }
            }
        }, Looper.getMainLooper());
    }
}
