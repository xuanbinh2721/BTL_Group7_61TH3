package com.example.clothingshop.Owner;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clothingshop.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class OwnerAddProduct extends AppCompatActivity {
    String categoryName;
    String Description,Price,Pname,saveCurrentDate,saveCurrentTime;
    ImageView InputProductImage;
    EditText InputProductName,InputProductDesp,InputProductPrice;
    Uri ImageUri;
    private static final int GalleryPick = 1;
    private String productRandomKey;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private String downloadImageUrl;
    //private ProgressDialog loadingBar;
    Button AddNewProductButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_owner_add_product);
        AddNewProductButton = (Button) findViewById(R.id.btnadd);
        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        InputProductImage = (ImageView) findViewById(R.id.imageView3);
        InputProductName = (EditText) findViewById(R.id.edtNameProductAdd);
        InputProductDesp = (EditText) findViewById(R.id.edtProductDes);
        InputProductPrice = (EditText) findViewById(R.id.edtProductPrice);
        //loadingBar = new ProgressDialog(this);
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }
    //
    private void ValidateProductData(){
        Description = InputProductDesp.getText().toString();
        Price = InputProductPrice.getText().toString();
        Pname = InputProductName.getText().toString();

        if (ImageUri == null){
            Toast.makeText(this,"Product Image is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description)){
            Toast.makeText(this,"Product Description is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Price)){
            Toast.makeText(this,"Product Price is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Pname)){
            Toast.makeText(this,"Product Name is mandatory!",Toast.LENGTH_SHORT).show();
        }
        else{
            // we need to get the time of new product addition
            StoreProductInformation();
            // Store all this data to database
        }
    }
    private void StoreProductInformation(){
        //loadingBar.setTitle("Add New Product");
        //loadingBar.setMessage("Dear Admin, please wait while we are adding the new product...");
        //oadingBar.setCanceledOnTouchOutside(false);
        //loadingBar.show();

        // To get the time of addition of a new product to database by the admin
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        System.out.println(saveCurrentDate+","+saveCurrentTime);

        productRandomKey = saveCurrentDate + saveCurrentTime;

        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");
        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e){
                String message = e.toString();
                Toast.makeText(OwnerAddProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                //loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot){
                Toast.makeText(OwnerAddProduct.this, "Product Image uploaded Successfully...", Toast.LENGTH_SHORT).show();
                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                    {
                        if (!task.isSuccessful()){
                            throw task.getException();
                        }
                        // this will just get the image url and not the link!!
                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task)
                    {
                        if (task.isSuccessful()){
                            // if image is uploaded successfully we can get the link
                            downloadImageUrl = task.getResult().toString();
                            Toast.makeText(OwnerAddProduct.this, "got the Product Image Url Successfully...", Toast.LENGTH_SHORT).show();
                            //To save the product to out Database
                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });
    }
    private void SaveProductInfoToDatabase(){
        // FIRST WE STORE ALL THE INFO OF THE PRODUCT TO A HASHMAP
        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("date", saveCurrentDate);
        productMap.put("time", saveCurrentTime);
        productMap.put("description", Description);
        // Image is get through image url
        productMap.put("image", downloadImageUrl);
        productMap.put("category", categoryName);
        productMap.put("price", Price);
        productMap.put("pname", Pname);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task)
                    {
                        if (task.isSuccessful()){
                            Intent intent = new Intent(OwnerAddProduct.this, OwnerCategory.class);
                            startActivity(intent);
                            //loadingBar.dismiss();
                            Toast.makeText(OwnerAddProduct.this, "Product is added successfully..", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            //loadingBar.dismiss();
                            String message = (task.getException()!=null)?task.getException().toString():null;
                            if (message!=null)
                                Toast.makeText(OwnerAddProduct.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private void OpenGallery(){
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(Intent.createChooser(galleryIntent,"Selected IMG"), GalleryPick);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GalleryPick && resultCode == RESULT_OK && data!=null){
            ImageUri = data.getData();
            // sets the image view to this image
            InputProductImage.setImageURI(ImageUri);
        }
    }
}
