package com.cesta.cesta;

import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Calendar;
import java.util.List;

public class Account implements Serializable {
    private String name;
    private String email;
    private Calendar dob;
    private Type type;
    private String imagePath;
    private int age;
    private String gender = "M";
    private List<Pref> p;

    public Account(Type type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Calendar getDob() {
        return dob;
    }

    public void setDob(Calendar dob) {
        this.dob = dob;
    }

    public Type getType() {
        return type;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public List<Pref> getP() {
        return p;
    }

    public void setP(List<Pref> p) {
        this.p = p;
    }

    public void downloadImage(final Context c, String path, final Callback cb) {
        new AsyncTask<String, Void, Bitmap>() {

            public static final String TAG = "Account: AsyncTask";

            @Override
            protected Bitmap doInBackground(String... params) {

                try {
                    URL url = new URL(params[0]);
                    InputStream in = url.openStream();
                    return BitmapFactory.decodeStream(in);
                } catch (Exception e) {
                    Log.d(TAG, "Error getting image");
                }
                return null;
            }

            @Override
            protected void onPostExecute(Bitmap bitmap) {
                ContextWrapper cw = new ContextWrapper(c);
                // path to /data/data/yourapp/app_data/imageDir
                File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
                // Create imageDir
                File path = new File(directory, "profile.png");

                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(path);
                    // Use the compress method on the BitMap object to write image to the OutputStream
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                    String pathS = path.getAbsolutePath();
                    Account.this.setImagePath(directory.getAbsolutePath());
                    Log.d(TAG, "onPostExecute: File saved as " + pathS);
                    cb.call();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        fos.close();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }.execute(path);
    }

    public Bitmap getImage(Context c) {
        try {
            File f=new File(getImagePath(), "profile.png");
            return BitmapFactory.decodeStream(new FileInputStream(f));
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return BitmapFactory.decodeResource(c.getResources(), R.drawable.some_cesta);
    }

    enum Type {
        FaceBook, Google;
    }

    static interface Callback {
        void call();
    }
}
