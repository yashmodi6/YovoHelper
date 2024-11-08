package com.example.yovohelper.views.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.yovohelper.R
import com.example.yovohelper.databinding.ActivityWhatsAppBinding
import com.example.yovohelper.utils.Constants
import com.example.yovohelper.utils.SharedPrefKeys
import com.example.yovohelper.utils.SharedPrefUtils
import com.example.yovohelper.utils.replaceFragment
import com.example.yovohelper.views.fragments.FragmentSettings
import com.example.yovohelper.views.fragments.FragmentStatus

class WhatsAppActivity : AppCompatActivity() {
    private val activity = this
    private val binding by lazy {
        ActivityWhatsAppBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        SharedPrefUtils.init(activity)
        binding.apply {

            requestPermissions()

            val fragmentWhatsAppStatus = FragmentStatus()
            val bundle = Bundle()
            bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
            replaceFragment(fragmentWhatsAppStatus, bundle)

            bottomNavigationView.setOnItemSelectedListener {

                when(it.itemId){
                    R.id.menu_status ->{
                        // whatsapp status
                        val fragmentWhatsAppStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(Constants.FRAGMENT_TYPE_KEY, Constants.TYPE_WHATSAPP_MAIN)
                        replaceFragment(fragmentWhatsAppStatus, bundle)
                    }
                    R.id.menu_business_status ->{
                        val fragmentWhatsAppStatus = FragmentStatus()
                        val bundle = Bundle()
                        bundle.putString(
                            Constants.FRAGMENT_TYPE_KEY,
                            Constants.TYPE_WHATSAPP_BUSINESS
                        )
                        replaceFragment(fragmentWhatsAppStatus, bundle)

                    }
                    R.id.menu_settings ->{
                        // settings
                        replaceFragment(FragmentSettings())


                    }
                }


                return@setOnItemSelectedListener true
            }

        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        fragment?.onActivityResult(requestCode, resultCode, data)
    }
    private val PERMISSION_REQUEST_CODE = 50
    private fun requestPermissions() {
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.Q) {
            val isPermissionsGranted = SharedPrefUtils.getPrefBoolean(
                SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                false
            )
            if (!isPermissionsGranted) {
                ActivityCompat.requestPermissions(
                    /* activity = */ activity,
                    /* permissions = */ arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    /* requestCode = */ PERMISSION_REQUEST_CODE
                )
                Toast.makeText(activity, "Please Grant Permissions", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CODE) {
            val isGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (isGranted) {
                SharedPrefUtils.putPrefBoolean(SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED, true)
            } else {
                SharedPrefUtils.putPrefBoolean(
                    SharedPrefKeys.PREF_KEY_IS_PERMISSIONS_GRANTED,
                    false
                )

            }
        }
    }

}