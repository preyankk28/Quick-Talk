package io.quicktalk.agilean.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.github.florent37.singledateandtimepicker.SingleDateAndTimePicker;
import com.github.florent37.singledateandtimepicker.dialog.SingleDateAndTimePickerDialog;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import io.quicktalk.agilean.Model.BroadcastModel;
import io.quicktalk.agilean.activities.LiveActivity;
import io.quicktalk.agilean.Constants;
import io.quicktalk.agilean.R;
import io.quicktalk.agilean.activities.BaseActivity;

public class BroadCastFragment extends Fragment {
    String currentDateandTime;
    private Context context;
    private View view;
    BaseActivity baseActivity;
    String datetext;
    SharedPreferences.Editor editor;
    EditText topicDiscussion;
    LiveActivity liveActivity;
    FirebaseUser user;
    String Uid;
    FirebaseAuth mAuth;
    TextView dateandtimepick;
    TextView dateandtimepickenter;
    RadioGroup radioGroup;
    private DatabaseReference mDatabase;
    RadioButton now, later, selected;
    LinearLayout addmedia;
    Button inviteParticipants;
    TextView currentdateandtime;
    public static final int RESULT_OK           = -1;
    FrameLayout broadcastImageFrameLayout;
    private StorageReference fileStorage;
    SharedPreferences sharedPreferences;
    String uid;
    private Uri localFileUri, serverFileUri;
    ImageView broadcastImage;
    String topicDiscussionString;
    String LinkforBroadCaster;
    String LinkforAudience;
    // request code
    private final int PICK_IMAGE_REQUEST = 22;
    FirebaseStorage storage;
    StorageReference storageReference;
    String randomname;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_broadcast, container, false);
        init();
        return view;
    }

    private void init() {
        initElements();
        initListeners();
        initActions();
    }

    private void initActions() {
        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        Uid = user.getUid();

    }

    private void initListeners() {
        addmedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //SelectImage();
                pickImage();
                broadcastImageFrameLayout.setVisibility(View.VISIBLE);
            }
        });
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId) {
                    case R.id.now:
                        // switch to fragment 1
                        topicDiscussionString = topicDiscussion.getText().toString().trim();
                        String topicDiscussionStringfinal =  topicDiscussionString.replace(" ", "%20");
                        Log.e("TAG", "Message"+ topicDiscussionStringfinal);

                        LinkforBroadCaster = "https://www.quicktalk.io/?" +  topicDiscussionStringfinal + "__" + liveActivity.getRandomString()+"__1"+ "__" + randomname ;
                        LinkforAudience =  "https://www.quicktalk.io/?" + topicDiscussionStringfinal + "__" + liveActivity.getRandomString()+"__2" + "__" + randomname;
                        dateandtimepick.setVisibility(View.GONE);
                        dateandtimepickenter.setVisibility(View.GONE);
                        inviteParticipants.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String topic =topicDiscussion.getText().toString().trim();
                                if ((topic.isEmpty())) {
                                    topicDiscussion.setError("Please Enter Topic");
                                    topicDiscussion.requestFocus();
                                    return;
                                }
                                if (localFileUri != null){
                                    uploadImage();
                                }
                                sendBroadCastDatatoFirebaseforNow();
                                Toast.makeText(context,"Meeting created sucessfully", Toast.LENGTH_LONG).show();
                                if (localFileUri == null){
                                    HomeFragment fragment = new HomeFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                                //onMoreClicked();
                            }
                        });

                        break;
                    case R.id.later:
                        // Fragment 2
                        topicDiscussionString = topicDiscussion.getText().toString().trim();
                        String topicDiscussionStringfinallater =  topicDiscussionString.replace(" ", "%20");

                        LinkforBroadCaster = "https://www.quicktalk.io/?" +  topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__1"+ "__" + randomname  ;
                        LinkforAudience =  "https://www.quicktalk.io/?" + topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__2"+ "__" + randomname  ;
                        dateandtimepick.setVisibility(View.VISIBLE);
                        dateandtimepickenter.setVisibility(View.VISIBLE);

                        final String topic =topicDiscussion.getText().toString().trim();
                        inviteParticipants.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                if ((topic.isEmpty())) {
                                   topicDiscussion.setError("Please Enter Topic");
                                    topicDiscussion.requestFocus();
                                    return;

                                }
                                datetext =      dateandtimepickenter.getText().toString().trim();
                                Log.e("TAG", "Datetext" + datetext);
                                  if ((datetext.isEmpty())) {
                    inviteParticipants.setEnabled(false);
                    Toast.makeText(context,"Please enter date for later", Toast.LENGTH_LONG).show();
//                                    dateandtimepick.setError("Please Enter Date for Later");
//                                    dateandtimepickenter.requestFocus();
                    return;
                }
                if (datetext != null) {
                    inviteParticipants.setEnabled(true);
                }
                               // onMoreClicked();
                                if (localFileUri != null){
                                    uploadImage();

                                }



                                sendBroadCastDatatoFirebase();


                                Toast.makeText(context,"Meeting created sucessfully", Toast.LENGTH_LONG).show();
                                if (localFileUri == null){
                                    HomeFragment fragment = new HomeFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.fragment_container, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }
                        });
                        break;

                }

            }
        });
        dateandtimepick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                new SingleDateAndTimePickerDialog.Builder(context)
                        // .backgroundColor(Color.BLACK)
                        .mainColor(Color.BLACK)
                        .minutesStep(1)

                        //.bottomSheet()
                        //.curved()
                        //.stepSizeMinutes(1)
                        //.displayHours(false)
                        //.displayMinutes(false)
                        //.todayText("aujourd'hui")
                        .displayListener(new SingleDateAndTimePickerDialog.DisplayListener() {
                            @Override
                            public void onDisplayed(SingleDateAndTimePicker picker) {
                                // Retrieve the SingleDateAndTimePicker
                                Log.e("TAG", "Picker" +picker);
                            }


                        })
                        .title("Select Date and Time")
                        .listener(new SingleDateAndTimePickerDialog.Listener() {
                            @Override
                            public void onDateSelected(Date date) {

                                Log.e("TAG", "Date" + date);

//                                setDate(dateandtimepickenter);
                                SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy");
                              String dateandtimeforlater = sdf.format(date);
                              dateandtimepickenter.setText(dateandtimeforlater);
                                datetext = dateandtimepickenter.getText().toString().trim();
                                Log.e("TAG", "Datetext" + datetext);
                            }
                        }).display();
                datetext =      dateandtimepickenter.getText().toString().trim();
//                if ((datetext.isEmpty())) {
//                    inviteParticipants.setEnabled(false);
////                    Toast.makeText(context,"Please enter date for later", Toast.LENGTH_LONG).show();
////                                    dateandtimepick.setError("Please Enter Date for Later");
////                                    dateandtimepickenter.requestFocus();
//                    return;
//                }
//                if (datetext != null){
//                    inviteParticipants.setEnabled(true);
//
//                }
            }
        });

//        inviteParticipants.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //   uploadImage();
//                // checkifanythingisEmpty();
//                final String topic =topicDiscussion.getText().toString().trim();
//                if ((topic.isEmpty())) {
//                    topicDiscussion.setError("Please Enter Topic");
//                    topicDiscussion.requestFocus();
//                    return;
//                }
//
//
////                onMoreClicked();
//
//            }
//        });

    }

    private void initElements() {
        dateandtimepick = view.findViewById(R.id.dateandtimepick);
        dateandtimepickenter = view.findViewById(R.id.dateandtimepickenter);
        radioGroup = view.findViewById(R.id.radioGroup);

        now = view.findViewById(R.id.now);
        later = view.findViewById(R.id.later);
        addmedia = view.findViewById(R.id.addmedia);
        broadcastImage = view.findViewById(R.id.broadcastimage);
        broadcastImageFrameLayout = view.findViewById(R.id.broadcastimagelayoutframe);
        inviteParticipants = view.findViewById(R.id.inviteparticipants);
        topicDiscussion = view.findViewById(R.id.topicdiscussion);
      //  currentdateandtime = view.findViewById(R.id.currentdateandtime);
        SimpleDateFormat sdf = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy");
        currentDateandTime = sdf.format(new Date());
        //currentdateandtime.setText(currentDateandTime);
        randomname = UUID.randomUUID().toString();

    }
    public void pickImage() {

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, 101);
        } else {
            ActivityCompat.requestPermissions(((AppCompatActivity)getActivity()), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 102);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 101) {
            if (resultCode == RESULT_OK) {

                localFileUri = data.getData();
                 broadcastImage.setImageURI(localFileUri);
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 102) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 101);
            } else {
                Toast.makeText(context, R.string.permission_required, Toast.LENGTH_SHORT).show();
            }

        }
    }
    public void setDate (TextView view){

        Date today = Calendar.getInstance().getTime();//getting date
        SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd hh:mm:ss yyyy " );//formating according to my need
        String date = formatter.format(today);
        view.setText(date);

    }
    public void showpopup(){
        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);


        AlertDialog alert = alertDialogBuilder.create();
        alert.setMessage("Do you wish to host the meeting?");
        alertDialogBuilder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    }
    public void onMoreClicked() {
        // Do nothing at the moment

        topicDiscussionString = topicDiscussion.getText().toString().trim();
        String topicDiscussionStringfinallater =  topicDiscussionString.replace(" ", "%20");

        LinkforBroadCaster = "https://www.quicktalk.io/?" +  topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__1"  ;
        LinkforAudience =  "https://www.quicktalk.io/?" + topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__2" ;
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setMessage(R.string.invite_message);

        alertDialogBuilder.setPositiveButton(R.string.broadcaster, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // finish();

                String shareMessage = "Inviting you for Discussion\n\n" + LinkforBroadCaster;
                share(shareMessage);


            }
        });
        alertDialogBuilder.setNegativeButton(R.string.audience, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                String shareMessage = "Inviting you for Discussion\n\n" + LinkforAudience;
                share(shareMessage);

            }
        });
        alertDialogBuilder.show();
    }

    public void share(String shareMessage) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, "Quick Talk");
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            //e.toString();
            //Toast.makeText(this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkifanythingisEmpty() {
        topicDiscussionString = topicDiscussion.getText().toString().trim();
        if (topicDiscussionString.equals("")) {
            topicDiscussion.setError("Enter Topic");



        }
        inviteParticipants.setBackgroundColor(getResources().getColor(R.color.buttonblue));
        inviteParticipants.setEnabled(true);


    }
    private void sendBroadCastDatatoFirebaseforNow(){


        topicDiscussionString = topicDiscussion.getText().toString().trim();
        String topicDiscussionStringfinallater =  topicDiscussionString.replace(" ", "%20");

        LinkforBroadCaster = "https://www.quicktalk.io/?" +  topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__1" + "__"+ randomname ;
        LinkforAudience =  "https://www.quicktalk.io/?" + topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__2" + "__" + randomname;
        BroadcastModel broadcast = new BroadcastModel(topicDiscussion.getText().toString().trim(),currentDateandTime,randomname,LinkforBroadCaster, LinkforAudience);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        // databaseReference.child("Users").child(Uid).child("Discussions").child(randomname).setValue(broadcast);
        databaseReference.child("Discussions").child(Uid).child(randomname).setValue(broadcast);


    }



    private void sendBroadCastDatatoFirebase() {


        topicDiscussionString = topicDiscussion.getText().toString().trim();
        String topicDiscussionStringfinallater =  topicDiscussionString.replace(" ", "%20");

        LinkforBroadCaster = "https://www.quicktalk.io/?" +  topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__1" + "__"+ randomname ;
        LinkforAudience =  "https://www.quicktalk.io/?" + topicDiscussionStringfinallater + "__" + liveActivity.getRandomString()+"__2"  + "__"+ randomname;
        BroadcastModel broadcast = new BroadcastModel(topicDiscussion.getText().toString().trim(),datetext,randomname,LinkforBroadCaster, LinkforAudience);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
       // databaseReference.child("Users").child(Uid).child("Discussions").child(randomname).setValue(broadcast);
        databaseReference.child("Discussions").child(Uid).child(randomname).setValue(broadcast);


    }
  public void uploadImage(){
      if (localFileUri != null) {
          //displaying progress dialog while image is uploading
          final ProgressDialog progressDialog = new ProgressDialog(context);
          progressDialog.setTitle("Uploading");
          progressDialog.show();
          storageReference = FirebaseStorage.getInstance().getReference();
          mDatabase = FirebaseDatabase.getInstance().getReference(Constants.DATABASE_PATH_UPLOADS);
          //getting the storage reference
          mAuth = FirebaseAuth.getInstance();
          user = mAuth.getCurrentUser();
          uid = user.getUid();
          StorageReference sRef = storageReference.child(Constants.STORAGE_PATH_UPLOADS + randomname + uid + topicDiscussion.getText().toString().trim());

          //adding the file to reference
          sRef.putFile(localFileUri)
                  .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                          //dismissing the progress dialog
                          progressDialog.dismiss();

                      }
                  })
                  .addOnFailureListener(new OnFailureListener() {
                      @Override
                      public void onFailure(@NonNull Exception exception) {
                          progressDialog.dismiss();
                          Toast.makeText(context, exception.getMessage(), Toast.LENGTH_LONG).show();
                      }
                  })
                  .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                      @Override
                      public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                          //displaying the upload progress
                          double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                          progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
                          HomeFragment fragment = new HomeFragment();
                          FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                          FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                          fragmentTransaction.replace(R.id.fragment_container, fragment);
                          fragmentTransaction.addToBackStack(null);
                          fragmentTransaction.commit();
                      }
                  });
      } else {
          //display an error if no file is selected
      }
  }
  }


