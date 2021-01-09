package com.example.greenhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.*;

public class MainActivity extends AppCompatActivity {

    // Veriler için kullanılan değişkenler
    private RecyclerView recyclerView;
    private FirebaseFirestore firebaseFirestore;
    private FirestoreAdapter adapter;
    private Query query, query2;

    // Görsel amaçlı kullanılan değişkenler
    private Toolbar toolbar;
    private BottomSheetDialog bottomSheetDialog;
    private TextView temperature_tv, name_tv, description_tv; // sırasıyla BottomSheetDialog da gösterilecek seranın sıcaklığı, adı ve açıklaması
    private Button save_btn; // BottomSheetDialog da yapılanları kaydetmek için
    private SeekBar seekBar; // BottomSheetDialog da sıcaklığı arttırmak için
    private Switch onOffSwitch; // BotoomSheetDialog da serayı kapatıp açmak için

    private double temp;
    private boolean onOff;
    String[] arrOfStr1, arrOfStr2, arrOfStr3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        bottomSheetDialog = new BottomSheetDialog(MainActivity.this); // Ekranın alt tarafından çıkacak dialog

        firebaseFirestore = FirebaseFirestore.getInstance(); // Firestore veritabanına erişimi sağlamaktadır

        recyclerView = findViewById(R.id.greenHouse_list); // Listeli bir şekilde verileri göstermek için RecyclerView widget

        // Query
        query = firebaseFirestore.collection("GreenHouses"); // Firestore veritabanı içerisinde yer alan collection kısmını elde etmemizi sağlar


        firebaseFirestore.collection("Logs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if(document.getId().equals("sera1logs")){
                                    String s3l = document.getData().toString().replace("{","").replace("}","");
                                    arrOfStr1 = s3l.split(",");
                                }
                                if(document.getId().equals("sera2logs")){
                                    String s3l = document.getData().toString().replace("{","").replace("}","");
                                    arrOfStr2 = s3l.split(",");
                                }
                                if(document.getId().equals("sera3logs")){
                                    String s3l = document.getData().toString().replace("{","").replace("}","");
                                    arrOfStr3 = s3l.split(",");
                                }

                                Log.d("TAG", document.getId() + " => " + document.getData());
                            }
                        } else {
                            Log.w("TAG", "Error getting documents.", task.getException());
                        }
                    }
                });



        // RecyclerOptions
        /*
           Adapter sınıfımıza gönderdiğimiz FirestorerRecyclerOptions yapısı
           query sınıfından gelen sorguyu alır ve build ederek oluşturmasını sağlar
           daha sonra Adaptera bunu göndererek veri kümesinin istenildiği şekilde oluşturulmasını sağlar

         */
        FirestoreRecyclerOptions<GreenHousesModel> options = new FirestoreRecyclerOptions.Builder<GreenHousesModel>()
                .setQuery(query, GreenHousesModel.class)
                .build();

        adapter = new FirestoreAdapter(options);

        recyclerView.setHasFixedSize(true); // Boyut sabitleniyor
        recyclerView.setLayoutManager(new LinearLayoutManager(this)); // Varsayılan ListView gibi LayoutManager kullanmak için
        recyclerView.setAdapter(adapter); // Adapteri activitymizde kullanmak için

        /*
         * Listeden bir elemana tıklandığında tetiklenecek olaylar
         */
        adapter.setOnItemClickListener(new FirestoreAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                GreenHousesModel greenHousesModel = documentSnapshot.toObject(GreenHousesModel.class);
                final String id = documentSnapshot.getId(); // Tıklanan elemanın id si alınıyor
                //String path = documentSnapshot.getReference().getPath();
                //GreenHousesModel greenHousesModel1 = adapter.getItem(position);

                /*
                 * Ayarların yapılacağı BottomSheetDialog kısmı
                 */
                View view = getLayoutInflater().from(MainActivity.this).inflate(R.layout.layout_dialog, null);

                // Tanımlamalar
                temperature_tv = view.findViewById(R.id.settingsTemperature);
                name_tv = view.findViewById(R.id.settingsGreenHouseName);
                save_btn = view.findViewById(R.id.settingsSave);
                seekBar = view.findViewById(R.id.settingsSeekBar);
                description_tv = view.findViewById(R.id.settingsDescription);
                onOffSwitch = view.findViewById(R.id.settingsSwitch);

                boolean rg;
                if(greenHousesModel.getRunning().equals("1")){
                    rg = true;
                }else{
                    rg = false;
                }

                onOffSwitch.setChecked(rg); // Seranın açık/kapalı olması durumu gösteriliyor
                description_tv.setText(greenHousesModel.getDesc()); // Seranın açıklaması gösteriliyor
                name_tv.setText(greenHousesModel.getName()); // Seranın adı gösteriliyor
                temperature_tv.setText(greenHousesModel.getCurrentTemp()); // Güncel sıcaklık gösteriliyor

                temp = Double.parseDouble(greenHousesModel.getCurrentTemp());

                final DocumentReference documentReference = firebaseFirestore.collection("GreenHouses").document(id);

                // Switch tetiklediğinde oluşan durum veritabanına yazdırmak için alınıyor
                onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        onOff = b;
                    }
                });

                /*
                 * Mevcut sıcaklığın üzerine ekleme yapılıyor
                 * 40 sınır olarak belirlendi
                 */

                final double t = Double.parseDouble(greenHousesModel.getCurrentTemp());
                int k  = (int) t;

                //seekBar.setProgress(k); // seekbar ın başlangıç ayarı

                seekBar.setMax(40 - k); // Mevcut sıcaklık 22 ise sadece 18 birimlik arttırım gerçekleşebilir

                seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                        double tt = i + t; // Mevcut sıcaklığın üzerine seekbar dan gelen değer ekleniyor
                        temp = tt;
                        temperature_tv.setText("" + tt);
                    }

                    @Override
                    public void onStartTrackingTouch(SeekBar seekBar) { }

                    @Override
                    public void onStopTrackingTouch(SeekBar seekBar) { }

                });

                // Değişikler veritabanına kaydediliyor
                save_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        documentReference.update("targettemp",String.valueOf((int)temp));
                        if(onOff){
                            documentReference.update("running", "1");
                        }else{
                            documentReference.update("running", "0");
                        }

                    }
                });

                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
            }
        });



    }

    /*
     * Sağ üst köşeye Toolbar ın üstüne menü ekleniyor
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    /*
     * Toolbar da bulunan menüden click sonucu oluşacak eylemler belirleniyor
    */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.log){
            // Toast.makeText(getApplicationContext(), "Log Sayfası",Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(MainActivity.this, MainActivity2.class);
            intent.putExtra("arrOfStr1", arrOfStr1); //veri gönderiliyor
            intent.putExtra("arrOfStr2", arrOfStr2); //veri gönderiliyor
            intent.putExtra("arrOfStr3", arrOfStr3); //veri gönderiliyor

            startActivity(intent);
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening(); // Ekranda listelediğimiz öğelerin sürekli olarak dinlenmesi sağlar
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening(); // Dinleme olayının durdurulmasını sağla
    }

}