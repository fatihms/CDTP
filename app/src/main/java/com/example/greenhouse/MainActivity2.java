package com.example.greenhouse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;

import java.util.ArrayList;

public class MainActivity2 extends AppCompatActivity {

    private Toolbar toolbar;
    private LineChart barChart;
    private String[] arrOfStr1,arrOfStr2,arrOfStr3;
    private LineDataSet ghDataSet1, ghDataSet2, ghDataSet3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        arrOfStr1 = intent.getStringArrayExtra("arrOfStr1");
        arrOfStr2 = intent.getStringArrayExtra("arrOfStr2");
        arrOfStr3 = intent.getStringArrayExtra("arrOfStr3");


        barChart = findViewById(R.id.groupBarChart);

        //Log.d("TAG", "hey"+ arrOfStr1[0].length());


        ghDataSet1 = new LineDataSet(gh1(),"Sera 1");
        ghDataSet1.setColor(Color.BLUE);
        ghDataSet1.setCircleColor(Color.GREEN);
        ghDataSet1.setDrawCircles(true);
        ghDataSet1.setDrawCircleHole(true);
        ghDataSet1.setLineWidth(5);
        ghDataSet1.setDrawValues(false);

        ghDataSet2 = new LineDataSet(gh2(),"Sera 2");
        ghDataSet2.setColor(Color.RED);
        ghDataSet2.setCircleColor(Color.GREEN);
        ghDataSet2.setDrawCircles(true);
        ghDataSet2.setDrawCircleHole(true);
        ghDataSet2.setLineWidth(5);
        ghDataSet2.setDrawValues(false);

        ghDataSet3 = new LineDataSet(gh3(),"Sera 3");
        ghDataSet3.setColor(Color.MAGENTA);
        ghDataSet3.setCircleColor(Color.GREEN);
        ghDataSet3.setDrawCircles(true);
        ghDataSet3.setDrawCircleHole(true);
        ghDataSet3.setLineWidth(5);
        ghDataSet3.setDrawValues(false);


        LineData data = new LineData (ghDataSet1, ghDataSet2, ghDataSet3);

        barChart.setData(data);

        barChart.setVisibleXRangeMaximum(10);

    }


    private ArrayList<Entry> gh1(){
        ArrayList<Entry> barEntries = new ArrayList<>();

        selectionSort(arrOfStr1);

        int sum = arrOfStr1.length;

        if(sum != 1){
            int i = 1, j = 1;
            for (String a : arrOfStr1){
                if (Math.abs(sum - i) < 20) {
                    int num = Integer.parseInt(a.split("=")[1]);
                    if(num > 0){
                        barEntries.add(new Entry(j, num));
                        j++;
                    }
                }
                i++;
            }
        }else{
            barEntries.add(new Entry(1, 0));
        }

        return barEntries;
    }

    private ArrayList<Entry> gh2(){
        ArrayList<Entry> barEntries = new ArrayList<>();

        selectionSort(arrOfStr2);

        int sum = arrOfStr2.length;

        if(sum != 1){
            int i = 1, j = 1;
            for (String a : arrOfStr2){
                if (Math.abs(sum - i) < 20) {
                    int num = Integer.parseInt(a.split("=")[1]);
                    if(num > 0){
                        barEntries.add(new Entry(j, num));
                        j++;
                    }
                }
                i++;
            }
        }else{
            barEntries.add(new Entry(1, 0));
        }

        return barEntries;

    }

    private ArrayList<Entry> gh3(){

        ArrayList<Entry> barEntries = new ArrayList<>();

        selectionSort(arrOfStr3);

        int sum = arrOfStr3.length;

        if(sum != 1){
            int i = 1, j = 1;
            for (String a : arrOfStr3) {
                if (Math.abs(sum - i) < 20) {
                    int num = Integer.parseInt(a.split("=")[1]);
                    if (num > 0) {
                        barEntries.add(new Entry(j, num));
                        j++;
                    }
                }
                i++;
            }
        }else{
            barEntries.add(new Entry(1, 0));
        }

        return barEntries;

    }

    public static void selectionSort(String[] arr){

        for (int i = 0; i < arr.length - 1; i++)
        {
            int index = i;
            for (int j = i + 1; j < arr.length; j++){

                int sec, min, hour;

                if(j == 0){
                    sec = Integer.parseInt(arr[j].split("=")[0].split(":")[2]);
                    min = Integer.parseInt(arr[j].split("=")[0].split(":")[1]);
                    //hour = Integer.parseInt(arr[j].split("=")[0].split(":")[0].split(" ")[1]);
                }else{
                    sec = Integer.parseInt(arr[j].split("=")[0].split(":")[2]);
                    min = Integer.parseInt(arr[j].split("=")[0].split(":")[1]);
                    //hour = Integer.parseInt(arr[j].split("=")[0].split(":")[0].split(" ")[2]);
                }

                int sec2, min2, hour2;
                if(index == 0){
                    sec2 = Integer.parseInt(arr[index].split("=")[0].split(":")[2]);
                    min2 = Integer.parseInt(arr[index].split("=")[0].split(":")[1]);
                    //hour2 = Integer.parseInt(arr[index].split("=")[0].split(":")[0].split(" ")[1]);
                }else{
                    sec2 = Integer.parseInt(arr[index].split("=")[0].split(":")[2]);
                    min2 = Integer.parseInt(arr[index].split("=")[0].split(":")[1]);
                    //hour2 = Integer.parseInt(arr[index].split("=")[0].split(":")[0].split(" ")[2]);
                }

                if (sec + (min*60)  < sec2 + (min2*60)  ){
                    index = j;//searching for lowest index
                }
            }
            String smallerNumber = arr[index];
            arr[index] = arr[i];
            arr[i] = smallerNumber;
        }


    }

}