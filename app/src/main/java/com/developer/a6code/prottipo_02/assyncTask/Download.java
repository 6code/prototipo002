package com.developer.a6code.prottipo_02.assyncTask;

import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.maps.GoogleMap;
import com.google.maps.android.kml.KmlLayer;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by edu on 30/08/17.
 */

public class Download extends AsyncTask<String,Integer,String>{


    private Context co;
    private GoogleMap mMap;
    private KmlLayer layer;
   // ProgressDialog mProgressDialog;


    //aqui no contrutor eu capturo o ocntexto da aplicaçao
    public Download (Context context, GoogleMap map){
        co = context;
        mMap = map;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

       // mProgressDialog = ProgressDialog.show(co,"Aguarde","Sincronizando...",true);
    }

    @Override
    protected String doInBackground(String... Url) { //nesse metodo, executa o proceso em paralelo, idela para carregr o elemetos no mapa

        URL url = null;
        try {

            url = new URL(Url[0]);
            URLConnection connection = url.openConnection();
            connection.connect();

            InputStream input = new BufferedInputStream(url.openStream()); //faz o dowlad e ler o arquivo

            layer = new KmlLayer(mMap, input, co.getApplicationContext());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    protected void onPostExecute(String s) { //aqui ele volta para thrad principal e marca o mapa
        super.onPostExecute(s);
        try {

            if(layer != null) {
                layer.addLayerToMap();
            }

         //   mProgressDialog.dismiss();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

    }

}
