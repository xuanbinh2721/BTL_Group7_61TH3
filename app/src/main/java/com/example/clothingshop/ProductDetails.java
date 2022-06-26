package com.example.clothingshop;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.clothingshop.Model.Products;
import com.example.clothingshop.Prevalent.Prevalent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class ProductDetails extends AppCompatActivity {
    private Button addToCartButton;
    private ImageView productImage;
    private EditText edtNumber;
    private TextView productPrice, productDescription,productName;
    private String productID = "",state = "Normal";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        ánh xạ
        setContentView(R.layout.activity_product_details);
        productID = getIntent().getStringExtra("pid");
        addToCartButton = (Button) findViewById(R.id.pd_add_to_cart_button);
        edtNumber = (EditText) findViewById(R.id.editNumber);
        productImage = (ImageView) findViewById(R.id.product_image_details);
        productName = (TextView) findViewById(R.id.product_name_details);
        productDescription = (TextView) findViewById(R.id.product_description_details);
        productPrice = (TextView) findViewById(R.id.product_price_details);
//        gọi hàm getProductDetails();
        getProductDetails(productID);
//        Bắt sự kiện click vào nút addToCart
        addToCartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (state.equals("Order Placed") || state.equals("Order Shipped")){
//                    Toast.makeText(ProductDetails.this, "Bạn không thể đặt ship được vì có 1 đơn hàng đang được ship.", Toast.LENGTH_LONG).show();
//                }
//                else{
//                gọi ra hàm addingToCartList để thêm vào cart
                    addingToCartList();
//                }
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();

    }
//Hàm xử lý thêm vào cart
    private void addingToCartList() {
        String saveCurrentTime, saveCurrentDate;
//        gọi các hàm xử lý thời gian để lấy được thời gian hiện tại khi thêm vào giỏ hàng
        Calendar callForDate = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(callForDate.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(callForDate.getTime());
//        gọi phương thức để tham chiếu đến firebase và vào nhánh con là Card List
        final DatabaseReference cartListRef = FirebaseDatabase.getInstance().getReference().child("Cart List");
//        tạo hash map để lưu dũ liệu lấy ra từ Products
        final HashMap<String, Object> cartMap = new HashMap<>();
        cartMap.put("pid", productID);
        cartMap.put("pname", productName.getText().toString());
        cartMap.put("price", productPrice.getText().toString());
        cartMap.put("date", saveCurrentDate);
        cartMap.put("time", saveCurrentTime);
        cartMap.put("quantity", edtNumber.getText().toString());
        cartMap.put("discount", "");
//        Tạo nhánh con của cart list lấy tên là userview và có con là Phone của user và con của user là Product rồi mới tới map
        cartListRef.child("User View").child(Prevalent.currentOnlineUser.getPhone())
                .child("Products").child(productID)
                .updateChildren(cartMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {// Bắt sự kiện khi thêm vào
                    @Override
                    public void onComplete(@NonNull Task<Void> task){
                        if (task.isSuccessful()){ // thành công thì chuyển dữ liệu sang cho Owner qua ownerview trển firebase
                            cartListRef.child("Owner View").child(Prevalent.currentOnlineUser.getPhone())
                                    .child("Products").child(productID)
                                    .updateChildren(cartMap)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task){
                                            if (task.isSuccessful()){ // Nếu thành công thì đưa ra thông báo và quay trở lại trang HomePage
                                                Toast.makeText(ProductDetails.this, "Added to Cart List.", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(ProductDetails.this, HomePage.class);
                                                startActivity(intent);
                                            }
                                        }
                                    });
                        }
                    }
                });
    }
//    hàm đưa ra thông tin của Product
    private void getProductDetails(final String productID) {
//        gọi đến firebase và vào nhánh con Products
        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
//        bắt sự kiện thay đổi giá trị
        productRef.child(productID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){// nếu tồn tại bản ghi của sản phẩm thì đổ thông tin ra layout
                    Products products = dataSnapshot.getValue(Products.class);
                    productName.setText(products.getPname());
                    productPrice.setText(products.getPrice());
                    productDescription.setText(products.getDescription());
                    Picasso.get().load(products.getImage()).into(productImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}