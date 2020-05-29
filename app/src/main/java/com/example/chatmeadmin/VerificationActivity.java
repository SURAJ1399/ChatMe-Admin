package com.example.chatmeadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.OkHttpClient;
import okhttp3.Request;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class VerificationActivity extends AppCompatActivity {
    ImageView photo,id;
    Button verify,cancel;
    String tokenid;
    TextView idnumber;
    String docid;
    String xx[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        idnumber=findViewById(R.id.idno);
        photo=findViewById(R.id.photo);
        id=findViewById(R.id.idimage);
        verify=findViewById(R.id.submitbtn);
        cancel=findViewById(R.id.submitbtn1);
    docid=getIntent().getStringExtra("id");
    xx=docid.split("91");
       // Toast.makeText(this, ""+xx[1], Toast.LENGTH_SHORT).show();

     //   Toast.makeText(this, ""+docid, Toast.LENGTH_SHORT).show();
        final FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        firebaseFirestore.collection("Verification").document(docid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                idnumber.setText(task.getResult().getString("idnumber"));
                Glide.with(VerificationActivity.this).load(task.getResult().getString("photourl")).into(photo);
                Glide.with(VerificationActivity.this).load(task.getResult().getString("idurl")).into(id);

            }
        });

        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(VerificationActivity.this);
                progressDialog.setMessage("Connecting..");
                progressDialog.show();
                DocumentReference documentReference= firebaseFirestore.collection("Verification").document(docid);
                documentReference.update("state","1");
                progressDialog.dismiss();
                Intent intent=new Intent(VerificationActivity.this,MainActivity.class);
                startActivity(intent);
                finish();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog=new ProgressDialog(VerificationActivity.this);
                progressDialog.setMessage("Connecting..");
                progressDialog.show();
                DemoTask notificationTask = new DemoTask();
                notificationTask.execute();

               // final OkHttpClient client=new OkHttpClient();
//                String  url = "https://www.smsgatewayhub.com/api/mt/SendSMS?APIKey=qO3Yw24kVkepMeKVkhXkIA&senderid=CRTCNR&channel=2&DCS=0&flashsms=0&" +
//                        "number="+"91"+mobile1+"&text="+"We have received Your Order and will be to delivered by  "+deliveryp+".You can find detail information at your mail."+"\n"+"   Cart Corner"+"&route=1";
//                Request request=new Request.Builder()
//                        .url(url)
//                        .build();
//                client.newCall(request).enqueue(new okhttp3.Callback() {
//                    @Override
//                    public void onFailure(okhttp3.Call call, IOException e) {
//                        // Toast.makeText(Home.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
//                        if(response.isSuccessful())
//                        {
//
//                        }
//                    }
//                });


                firebaseFirestore.collection("Verification").document(docid).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        progressDialog.dismiss();
                        Intent intent=new Intent(VerificationActivity.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });



            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
       docid=getIntent().getStringExtra("id");
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        firebaseFirestore.collection("Token").document(docid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                tokenid=task.getResult().getString("token");
            }
        });

    }
    class DemoTask extends AsyncTask<Void, Void, Void> {

        protected Void doInBackground(Void... arg0) {
            try {
                String jsonResponse;

                URL url = new URL("https://fcm.googleapis.com/fcm/send");
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setUseCaches(false);
                con.setDoOutput(true);
                con.setDoInput(true);

                con.setRequestProperty("Content-Type", "application/json");
                con.setRequestProperty("Authorization", "key=AAAAUieiVmw:APA91bEa14oR7FWB7aac_nDMW-bmNN_3tIMxGAHeTzXNCPXPqflT784SlsrFsARRR5UK9xBRdSkkki3av5U8HGfuqOAVAyTFVVG7GVdkxhX04nOWaFU-UjM_1IFI4XdsRxlGR8GCK2hM");
                con.setRequestMethod("POST");
                String strJsonBody = "{\n\t\"to\":\""+tokenid+"\"\n\t\"notification\":{\n\t\t\"title\":\"Verification Rejected\",\n\t\t\"body\":\"Your Application For Request Verification is Rejected. "+"\",\n\t\t\"image\":\"https://drive.google.com/open?id=1Rq7yfePTtEms6qtdGQZ5BOe-CMUJkRnu\"\n\t}\n\t\n}";

                byte[] sendBytes = strJsonBody.getBytes("UTF-8");
                con.setFixedLengthStreamingMode(sendBytes.length);

                OutputStream outputStream = con.getOutputStream();
                outputStream.write(sendBytes);

                int httpResponse = con.getResponseCode();

                if (  httpResponse >= HttpURLConnection.HTTP_OK
                        && httpResponse < HttpURLConnection.HTTP_BAD_REQUEST) {
                    Scanner scanner = new Scanner(con.getInputStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                else {
                    Scanner scanner = new Scanner(con.getErrorStream(), "UTF-8");
                    jsonResponse = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
                    scanner.close();
                }
                System.out.println("jsonResponse:\n" + jsonResponse);
                Log.e("jsonMessage",jsonResponse);


            } catch(Throwable t) {
                t.printStackTrace();
            }
            return  null;
        }

        protected void onPostExecute(Void result) {
            // TODO: do something with the feed
        }
    }
}
