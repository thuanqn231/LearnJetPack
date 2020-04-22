package com.example.learnjetpack.view

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.learnjetpack.R
import com.example.learnjetpack.util.PERMISSION_SEND_SMS
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private  lateinit var navControler: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navControler = Navigation.findNavController(this, R.id.fragment2)
        NavigationUI.setupActionBarWithNavController(this,navControler)
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navControler,null)
    }
    fun checkSmsPermission(){
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.SEND_SMS)!=PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){
                AlertDialog.Builder(this).setTitle("Send SMS permission")
                    .setMessage("This app requires assess to send and SMS")
                    .setPositiveButton("Ask Me"){
                        dialog, which ->
                        requestSmsPermision()
                    }.setNegativeButton("No"){dialog, which ->  notifyDetailFragment(false) }
                    .show()
            }else{
                requestSmsPermision()
            }
        }else{
            notifyDetailFragment(true)
        }
    }
    private  fun requestSmsPermision() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.SEND_SMS),
            PERMISSION_SEND_SMS
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            PERMISSION_SEND_SMS->{
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    notifyDetailFragment(true)
                }else{
                    notifyDetailFragment(false)
                }
            }
        }
    }
    private  fun notifyDetailFragment(permissionGrated:Boolean){
        val activeFragment: Fragment? = fragment2.childFragmentManager.primaryNavigationFragment
        if(activeFragment is DetailFragment){
            (activeFragment as DetailFragment).onPermisionResult(permissionGrated)
        }
    }
}
