package com.example.trabalhopratico1;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("eCDmqOWuUrWRzCVSt8ydKZ6ZmKjw5arnpYa0oEDq")
                .clientKey("pYL6kNMMgL7T9Dm38lHAJFRtM0PIDmW5qoSrjiZX")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
