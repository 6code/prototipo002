package com.developer.a6code.prottipo_02.configure;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by edu on 27/08/17.
 */

public class configureFirebase {

    private static DatabaseReference databaseReference;
    private static FirebaseStorage firebaseStorage;


    public static DatabaseReference getDatabaseReference(){

        if(databaseReference == null) {
            databaseReference = FirebaseDatabase.getInstance().getReference();


        }
        return databaseReference;
    }

    private static FirebaseStorage getFirebaseStorage(){

        if(firebaseStorage == null){
            firebaseStorage = FirebaseStorage.getInstance();
        }

        return firebaseStorage;

    }



}
