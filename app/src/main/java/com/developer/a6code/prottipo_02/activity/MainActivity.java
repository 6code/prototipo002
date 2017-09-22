package com.developer.a6code.prottipo_02.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.developer.a6code.prottipo_02.R;
import com.developer.a6code.prottipo_02.assyncTask.Download;
import com.developer.a6code.prottipo_02.model.loadMarkers;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    private GoogleMap mMap;
    private static final int MY_LOCATION_REQUEST_CODE = 1; //codigo para ter controle de que local estou fazendo essa validaçao
    private String[] MinhasPermissoes = new String[]{Manifest.permission.ACCESS_FINE_LOCATION}; //string de permissoes necessarias


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.developer.a6code.prottipo_02.R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map2);
        mapFragment.getMapAsync(this);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
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

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        LatLng latLng = new LatLng(-3.776872, -49.675535);
        CameraPosition update = new CameraPosition(latLng, 13, 0, 0);
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(update), 1000, null);

        //aqui eu chamo as funçoes onde eu faço as alterações no meu mapa

        MinhaLocalização();      //ativo minha localizaçao
        LoadMarkers();           //Carrego os marcadores no mapa, as informaçoes vem do BD
        LoadRotas();             //faço download das rotas e carrego no mapa
        EventoDeClickMarkres();   //capturo evento de click no marcador, e exibo mensagens ao usuaio

    }


    /*************************************************************************************************************/


    public void LoadRotas() {


        String url = "https://firebasestorage.googleapis.com/v0/b/prototipos-6da50.appspot.com/o/rapidinho.kml?alt=media&token=0d0845fb-2310-4733-a036-b64063cb548a";
        Download dw = (Download) new Download(MainActivity.this, mMap).execute(url); //executanod esse processo de forma assincrona

    }

    /*************************************************************************************************************/

    //verifica se p usuario tem permissao, se nao ele pede permissao
    public void MinhaLocalização() {

        //veririfca se o ususario ja deu a permissao
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            //   mMap.setTrafficEnabled(true); //mostra o trafego

        } else {
            //pede a permissao para o usuario
            String[] p = new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this, p, MY_LOCATION_REQUEST_CODE);
        }
    }

    //Tratando a resposta da permissao em tempo de execução
    //grantResults retorna o se o usurio deu a permissao
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        //aqui eu implemento a interface padrap do metodo onRequest, por isso eu uso o super
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)) {
            mMap.setMyLocationEnabled(true); //caso o usuario de a permissao, ele seta  a localizaçao do usuaio
            //   mMap.setTrafficEnabled(true);
        }

    }


    /*************************************************************************************************************/


    public void LoadMarkers() {


        String ref = "rapidinho";


        final FirebaseDatabase database = null;

        final DatabaseReference markers = database.getInstance().getReference().child("ROTAS").child(ref);

        markers.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) { //datasnapshot captura qualquer  alteraçoes no BD

                Iterable<DataSnapshot> dataSnapshots = dataSnapshot.getChildren();
                mMap.clear();
                for (DataSnapshot dataSnapshot1 : dataSnapshots) {

                    loadMarkers mdlk = dataSnapshot1.getValue(loadMarkers.class);
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mdlk.getLatitude(), mdlk.getLongitude())).title(mdlk.getParada()));
                    markers.keepSynced(true);
                    mMap.getUiSettings().setMapToolbarEnabled(false);


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



    /*************************************************************************************************************/

    public void EventoDeClickMarkres(){



        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                LatLng latLng = marker.getPosition();
                Toast.makeText(MainActivity.this, "" + latLng ,Toast.LENGTH_LONG).show();



                return false;
            }
        });

    }

}


