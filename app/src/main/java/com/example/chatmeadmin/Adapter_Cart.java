package com.example.chatmeadmin;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


public class Adapter_Cart extends RecyclerView.Adapter<Adapter_Cart.ViewHolder> {
    Context context;
    public List<Model> cartModel;
    FirebaseFirestore firebaseFirestore;
    FirebaseAuth firebaseAuth;

    public Adapter_Cart(List<Model> cartModel) {
        this.cartModel=cartModel;
    }

    @NonNull
    @Override
    public Adapter_Cart.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.request_adapter, parent, false);
        context = parent.getContext();
        return new Adapter_Cart.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Adapter_Cart.ViewHolder holder, final int position) {
       holder.number.setText(cartModel.get(position).getUserid());
       holder.verify.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
              // Toast.makeText(context, ""+cartModel.get(position).getUserid(), Toast.LENGTH_SHORT).show();
               Intent intent=new Intent(context,VerificationActivity.class);
               intent.putExtra("id",cartModel.get(position).getUserid());
               context.startActivity(intent);

           }
       });


    }

    @Override
    public int getItemCount() {
        return cartModel.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView number;
        Button verify;
        LinearLayout select_subcat;
        View mView;

        public ViewHolder(View view) {
            super(view);
            mView = view;
           number=mView.findViewById(R.id.number);
           verify=mView.findViewById(R.id.btn);

        }
    }
}





