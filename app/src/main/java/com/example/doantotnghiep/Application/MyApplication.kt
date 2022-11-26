package com.example.doantotnghiep.Application

import android.app.Application
import com.example.doantotnghiep.Repository.ShareReference

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        ShareReference.init(this);
    }
}