package com.example.kisan_udyog;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.tensorflow.lite.Interpreter;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EcoFragment extends Fragment{

    private static final String TAG = "EcoFragment";
    private TextView result;
    private EditText temperature, rainfall, pH, humidity;
    Interpreter interpreter;
    private Button predict;
    private float mtemperature, mrainfall, mpH, mhumidity;
    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_eco, container, false);

        result=view.findViewById(R.id.outputModel);
        temperature=view.findViewById(R.id.temperature);
        rainfall=view.findViewById(R.id.rainfall);
        pH=view.findViewById(R.id.pHvalue);
        humidity=view.findViewById(R.id.Humidity);

        try {
            interpreter = new Interpreter(loadModelFile(),null);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        predict=view.findViewById(R.id.predict);
        predict.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mtemperature=Float.parseFloat(temperature.getText().toString());
                mrainfall=Float.parseFloat(rainfall.getText().toString());
                mpH=Float.parseFloat(pH.getText().toString());
                mhumidity=Float.parseFloat(humidity.getText().toString());
                String f= doInference(mtemperature,mhumidity,mpH,mrainfall);
                result.setText("result"+"-->"+f);
            }
        });
        return view;
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor assetFileDescriptor = getActivity().getAssets().openFd("tfmodels.tflite");
        FileInputStream fileInputStream = new FileInputStream(assetFileDescriptor.getFileDescriptor());
        FileChannel fileChannel= fileInputStream.getChannel();
        long startOffset = assetFileDescriptor.getStartOffset();
        long length =assetFileDescriptor.getLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset,length);
    }

    public String doInference(float j,float k, float l, float m){
        float[] input = new float[]{j, k,l,m};
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
}
