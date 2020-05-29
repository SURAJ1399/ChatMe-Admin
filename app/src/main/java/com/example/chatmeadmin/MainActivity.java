package com.example.chatmeadmin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;
    public List<Model> categoryModelList;
    Adapter_Cart allJobsAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
getSupportActionBar().hide();
        categoryModelList=new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);


        LinearLayoutManager layoutManager= new LinearLayoutManager(MainActivity.this);


        //RecyclerView.LayoutManager layoutManager;
        recyclerView.setLayoutManager(layoutManager);

        //Passing image array and assigning adapter is object of recycler_adapter class.

        allJobsAdapter= new Adapter_Cart(categoryModelList);
        //allJobsAdapter2=new Adapter_Products(categoryModelList2);

        recyclerView.setAdapter(allJobsAdapter);
        firebaseFirestore=FirebaseFirestore.getInstance();
        categoryModelList.clear();

        firebaseFirestore.collection("Verification").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {
                       // String blogpostid = doc.getDocument().getId();
                        if(doc.getDocument().getString("state").equals("0"))
                        {
                        Model allJobsModel = doc.getDocument().toObject(Model.class);
                        categoryModelList.add(allJobsModel);
//                            // Toast.makeText(getContext(), ""+doc.getDocument().getString("bio"), Toast.LENGTH_SHORT).show();
                        allJobsAdapter.notifyDataSetChanged();}

                    }
                }
                if(categoryModelList.size()==0)
                {
                    Toast.makeText(MainActivity.this, "No Users Found", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
