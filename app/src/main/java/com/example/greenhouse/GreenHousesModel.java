package com.example.greenhouse;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class GreenHousesModel {

    private String item_id;
    private String name;
    private String currenttemp;
    private String running;

    private GreenHousesModel(){

    }

    public GreenHousesModel(String name, String currenttemp, String item_id) {
        this.name = name;
        this.currenttemp = currenttemp;
        this.item_id = item_id;
    }

    public String getItem_id() {
        return item_id;
    }

    public String getName() {
        return name;
    }

    public String getCurrentTemp() {
        return currenttemp;
    }

    public String getDesc(){
        double ct = Double.parseDouble(getCurrentTemp());

        if(ct < 10){
            return "Aşırı Düşük";
        }else if(ct >= 10 && ct <= 20){
            return "Normal";
        }else if(ct > 20 && ct <= 30){
            return "Fazla";
        }else{
            return "Aşırı Fazla";
        }
    }

    public String getRunning(){
        return running;
    }

}

