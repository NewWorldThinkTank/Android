package com.cesta.cesta;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_GOOGLE_SIGN_OUT = 0x01;
    private static final int REQUEST_CODE_FB_LOG_OUT = 0x02;
    private static final String TAG = "ProfileActivity";

    private View googleBtn;
    private View fbBtn;
    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        account = (Account) getIntent().getSerializableExtra("ac");
        final Toolbar actionBar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = (RecyclerView) this.findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        // use a linear layout manager
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        ProfileContentAdapter adapter = new ProfileContentAdapter(this);
        recyclerView.setAdapter(adapter);

        /*googleBtn = findViewById(R.id.sign_out);
        googleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, GoogleSignActivity.class);
                i.setAction(GoogleSignActivity.SIGN_OUT);
                startActivityForResult(i, REQUEST_CODE_GOOGLE_SIGN_OUT);
            }
        });

        fbBtn = findViewById(R.id.log_out);
        fbBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ProfileActivity.this, FBLoginActivity.class);
                i.setAction(FBLoginActivity.LOG_OUT);
                startActivityForResult(i, REQUEST_CODE_FB_LOG_OUT);
            }
        });*/

        TextView name = (TextView) findViewById(R.id.name);
        if (name != null) {
            name.setText(account.getName());
        } else
            Log.e(TAG, "onCreate: name is null");
        TextView email = (TextView) findViewById(R.id.email_nav);
        if (email != null) {
            email.setText(account.getEmail());
        } else
            Log.e(TAG, "onCreate: email is null");

        Bitmap profilePic = BitmapFactory.decodeFile(account.getImagePath());
        ImageView profileImage = (ImageView) findViewById(R.id.profile_pic_nav);
        if (profileImage != null) {
            profileImage.setImageBitmap(profilePic);
        } else
            Log.e(TAG, "onCreate: profileImage is null");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_GOOGLE_SIGN_OUT) {
            if (resultCode == RESULT_OK) {
                SharedPreferences p = getSharedPreferences(MainActivity.PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = p.edit();
                editor.putBoolean(MainActivity.SIGNED_IN, false);
                editor.apply();

                Toast.makeText(this, "Log out successful.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: Log out successful.");
                Snackbar.make(googleBtn, "Log out successful", Snackbar.LENGTH_SHORT).show();
            }
        } else if (requestCode == REQUEST_CODE_FB_LOG_OUT) {
            if (resultCode == RESULT_OK) {
                SharedPreferences p = getSharedPreferences(MainActivity.PREF, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = p.edit();
                editor.putBoolean(MainActivity.SIGNED_IN, false);
                editor.apply();

                Toast.makeText(this, "Log out successful.", Toast.LENGTH_SHORT).show();
                Log.d(TAG, "onActivityResult: Log out successful.");
                Snackbar.make(fbBtn, "Log out successful", Snackbar.LENGTH_SHORT).show();
            }
        }
    }

    private class ProfileContentAdapter extends RecyclerView.Adapter<ProfileContentAdapter
            .ViewHolder>{
        public ProfileContentAdapter(ProfileActivity profileActivity) {
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.content_profile, parent, false);
            return new ViewHolder(v);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            ImageView profile = (ImageView) holder.c.findViewById(R.id.profile_pic);
        }

        @Override
        public int getItemCount() {
            return 1;
        }

        public class ViewHolder extends  RecyclerView.ViewHolder {
            CardView c;
            public ViewHolder(View itemView) {
                super(itemView);
                c = (CardView) itemView.findViewById(R.id.profile_cardview);
                googleBtn = itemView.findViewById(R.id.sign_out);
                googleBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ProfileActivity.this, GoogleSignActivity.class);
                        i.setAction(GoogleSignActivity.SIGN_OUT);
                        startActivityForResult(i, REQUEST_CODE_GOOGLE_SIGN_OUT);
                    }
                });

                fbBtn = itemView.findViewById(R.id.log_out);
                fbBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(ProfileActivity.this, FBLoginActivity.class);
                        i.setAction(FBLoginActivity.LOG_OUT);
                        startActivityForResult(i, REQUEST_CODE_FB_LOG_OUT);
                    }
                });
            }
        }
    }
}