package com.redudant.memorableplaces;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ListView listView;

    static ArrayList<String> places = new ArrayList<> ();
    static ArrayList<LatLng> locations = new ArrayList<> (); //membuat dafar list lokasi

    static ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        listView = findViewById ( R.id.listView );

        places.add ( "Add a new places..." );

        locations.add ( new LatLng ( 0, 0 ) ); //menambahkan

        arrayAdapter = new ArrayAdapter<> ( this, android.R.layout.simple_list_item_1, places );

        listView.setAdapter ( arrayAdapter );

        listView.setOnItemClickListener ( new AdapterView.OnItemClickListener () {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("placeNumber", position);

                startActivity(intent);
            }

        });


    }
}