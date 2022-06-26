package com.example.clothingshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.clothingshop.Model.Cart;
import com.example.clothingshop.Prevalent.Prevalent;
import com.example.clothingshop.ViewHolder.CartViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ContentCart extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private Button btnNext;
    private TextView txtTotalAmount, txtMsg1;
    private int overTotalPrice = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_cart);
        recyclerView = findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        btnNext = (Button) findViewById(R.id.btnNext);
        txtTotalAmount = (TextView) findViewById(R.id.page_title);
//        bắt sự kiện khi nhấn Next(chưa xong)
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                txtTotalAmount.setText("Tổng = VND" + String.valueOf(overTotalPrice));
                Intent intent = new Intent(ContentCart.this, ConfirmFinalOrder.class);
                // send the total price to the next  - confirm final order activity
                intent.putExtra("Tổng", String.valueOf(overTotalPrice));
                startActivity(intent);
                finish();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        CheckOrderState();
        //tham chiếu đến nhánh con Card List
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
        //liên kết Truy vấn với RecyclerView và phản hồi tất cả các sự kiện thời gian thực bao gồm các items được thêm, xóa, di chuyển hoặc thay đổi.
        //được sử dụng với quy mô nhà khi tất cả kết quả được trả về trong 1 lần
        FirebaseRecyclerOptions<Cart> options =
                new FirebaseRecyclerOptions.Builder<Cart>()
                        .setQuery(cartListRef.child("User View")
                                .child(Prevalent.currentOnlineUser.getPhone())
                                .child("Products"), Cart.class).build();
//      tạo adapter để lên kết với viewholder
        FirebaseRecyclerAdapter<Cart, CartViewHolder> adapter
                = new FirebaseRecyclerAdapter<Cart, CartViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CartViewHolder holder, int position, @NonNull final Cart model){
//                đổ thông tin ra layout
                holder.txtProductQuantity.setText("Số lượng = " + model.getQuantity());
                holder.txtProductPrice.setText("Giá " + model.getPrice() + "VND");
                holder.txtProductName.setText(model.getPname());

                int oneTypeProductTPrice = 0;
                try{
                    oneTypeProductTPrice = ((Integer.parseInt(model.getPrice()))) * Integer.parseInt(model.getQuantity());
                }catch(Exception e){
                    System.out.println("Exception raised!!\n");
                }


                overTotalPrice += oneTypeProductTPrice;
                // khi clik vào item trong cart sẽ có 2 lựa chọn là edit hoặc remove
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view){
                        CharSequence options[] = new CharSequence[]{"Edit","Remove"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(ContentCart.this);
                        builder.setTitle("Cart Options");
                        //bắt sự kiện khi click vào 1 trong 2 option
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i){
                                // i==0 là chọn edit
                                if (i == 0){
                                    // đưa người dùng trở lại ProductDetails
                                    Intent intent = new Intent(ContentCart.this, ProductDetails.class);
                                    // Intents are asynchronous messages which allow Android components to request functionality from other components of the Android system.
                                    // For example an Activity can send an Intents to the Android system which starts another Activity .
                                    // putExtra() adds extended data to the intent.
                                    // ProductDetailsActivity can access tje pid of product now from this intent
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                                // remove option for a product in cart
                                if (i == 1){
                                    // remvoves the item from firebase database
                                    cartListRef.child("User View")
                                            .child(Prevalent.currentOnlineUser.getPhone())
                                            .child("Products")
                                            .child(model.getPid())
                                            .removeValue()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task){
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(ContentCart.this, "Item removed successfully!", Toast.LENGTH_SHORT).show();
                                                        //Intent intent = new Intent(CartActivity.this, CartActivity.class);
                                                        //startActivity(intent);
                                                    }
                                                }
                                            });
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
            @NonNull
            @Override
            public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items_layout, parent, false);
                CartViewHolder holder = new CartViewHolder(view);
                return holder;
            }
        };

        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }
    private void CheckOrderState(){
        DatabaseReference ordersRef;
        ordersRef = FirebaseDatabase.getInstance().getReference().child("Orders").child(Prevalent.currentOnlineUser.getPhone());
        ordersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    String shippingState = dataSnapshot.child("state").getValue().toString();
                    String userName = dataSnapshot.child("fname").getValue().toString();
                    if (shippingState.equals("shipped")){
                        txtTotalAmount.setText("Dear " + userName + "\n order is shipped successfully.");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        txtMsg1.setText("Congratulations, your final order has been Shipped successfully. Soon you will received your order at your door step.");
                        btnNext.setVisibility(View.GONE);

                        Toast.makeText(ContentCart.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                    else if(shippingState.equals("not shipped")){
                        txtTotalAmount.setText("Shipping State : Not Shipped");
                        recyclerView.setVisibility(View.GONE);

                        txtMsg1.setVisibility(View.VISIBLE);
                        btnNext.setVisibility(View.GONE);

                        Toast.makeText(ContentCart.this, "you can purchase more products, once you received your first final order.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}