package com.example.clothingshop;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.clothingshop.Prevalent.Prevalent;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class Setting extends AppCompatActivity {

    final int REQUEST_CODE_GALLERY = 999;
    private ImageView profileImageView;
    // FOR THE USER INFO FIELDS TO UPDATE IN SETTINGS
    private EditText edtChangeName, edtChangePhone, edtChangeAddress;
    // SETTING BUTTONS
    private TextView profileChangeTextBtn,  closeTextBtn, saveTextButton;
    private Button btnUpdate, btnClose;
    // TO GET THE PICTURE STORED IN FIREBASE STORAGE  ALREADY
    private Uri uri;
    private ImageView profilePic;
    private Bitmap bitmap;
    // FOR USERS DATABASE
    DatabaseHelperCustomer userDb;
    private Uri imageUri;
    private String myUrl = "";
    private StorageReference storageProfilePictureRef;
    private String checker = "";
    private StorageTask uploadTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        storageProfilePictureRef = FirebaseStorage.getInstance().getReference().child("Profile pictures");
        edtChangeName = (EditText) findViewById(R.id.edtchangeName);
        edtChangePhone = (EditText) findViewById(R.id.changePhone);
        edtChangeAddress = (EditText) findViewById(R.id.edtchangeAddress);
        profileChangeTextBtn = (TextView)findViewById(R.id.change_avt);
        profileImageView = (ImageView) findViewById(R.id.imageView2);
        btnClose = (Button) findViewById(R.id.btnClose);
        btnUpdate = (Button) findViewById(R.id.btnUpdate);
        // CONSTRUCTOR TO CREATE USER DATABASE USING USER DATABASE HELPER CLASS
        userDb = new DatabaseHelperCustomer(this);

        // FUNCTION TO DISPLAY ALL THE PROFILE INFORMATION
        userInfoDisplay(edtChangeName,edtChangePhone,edtChangeAddress);

        // CLOSE BUTTON
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                // TO END THE CURRENT TASK AND GO BACK TO THE PREVIOUS SCREEN
                finish();
            }
        });
        //  BUTTON TO CHANGE THE PROFILE PICTURE
        profileChangeTextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                checker = "clicked";
                CropImage.activity(imageUri)
                        .setAspectRatio(1, 1)
                        .start(Setting.this);
            }
        });
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                if (checker.equals("clicked")){
                    userInfoSaved();
                }
                else{
                    updateUserInfo();
                }
            }
        });
    }
    private void updateUserInfo(){
        String oldphone = Prevalent.currentOnlineUser.getPhone();
        String phone = edtChangePhone.getText().toString();
        String name = edtChangeName.getText().toString();
        String address = edtChangeAddress.getText().toString();
        userDb.updateDataUsers(oldphone, phone, name, address);
        startActivity(new Intent(Setting.this, HomePage.class));
        Toast.makeText(Setting.this, "Profile Info updated successfully.", Toast.LENGTH_SHORT).show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE  &&  resultCode==RESULT_OK  &&  data!=null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageUri = result.getUri();
            profileImageView.setImageURI(imageUri);
        }
        else{
            Toast.makeText(this, "Error, Try Again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(Setting.this, Setting.class));
            finish();
        }
    }
    private void userInfoSaved(){
        if (TextUtils.isEmpty(edtChangeName.getText().toString())){
            Toast.makeText(this, "Your Name is mandatory!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtChangePhone.getText().toString())){
            Toast.makeText(this, "Phone Number is mandatory!", Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(edtChangeAddress.getText().toString())){
            Toast.makeText(this, "Please enter your address!", Toast.LENGTH_SHORT).show();
        }
        else if(checker.equals("clicked")){
            uploadImage();
        }
    }
    private void uploadImage(){

        if (imageUri != null){
            final StorageReference fileRef = storageProfilePictureRef.child(Prevalent.currentOnlineUser.getPhone() + ".jpg");
            uploadTask = fileRef.putFile(imageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception{
                    if (!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task){
                    if (task.isSuccessful()){
                        Uri downloadUrl = task.getResult();
                        myUrl = downloadUrl.toString();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users");
                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap. put("image", myUrl);
                        ref.child(Prevalent.currentOnlineUser.getPhone()).updateChildren(userMap);
                        Prevalent.currentOnlineUser.setProfilePic("true");
                        startActivity(new Intent(Setting.this, HomePage.class));
                        Toast.makeText(Setting.this, "Profile Info updated successfully!", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                    else
                    {
                        Toast.makeText(Setting.this, "Error.", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else
        {
            Toast.makeText(this, "Image is not selected.", Toast.LENGTH_SHORT).show();
        }
    }
    private void userInfoDisplay(final EditText edtChangeName,final EditText edtChangePhone,final EditText edtChangeAddress){
        // RETRIEVING USER DATA FROM SQLITE DATABASE
        String phone = Prevalent.currentOnlineUser.getPhone();
        String name = Prevalent.currentOnlineUser.getName();
        String address = userDb.getUserAddress(phone);
        if (address != null && !address.equals("null"))
            edtChangeAddress.setText(address);
        edtChangePhone.setText(phone);
        edtChangeName.setText(name);

        // TO DISPLAY PROFILE PIC - PROFILE PIC OF USER STORED IN FIREBASE FOR EASE
        DatabaseReference UsersRef = FirebaseDatabase.getInstance().getReference().child("Users").child(Prevalent.currentOnlineUser.getPhone());
        UsersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot){
                if (dataSnapshot.exists()){
                    if (dataSnapshot.child("image").exists()){
                        String image = dataSnapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profileImageView);
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
