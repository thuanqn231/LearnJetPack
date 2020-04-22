package com.example.learnjetpack.view

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.Intent
import android.database.DatabaseUtils
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.telephony.SmsManager
import android.text.TextUtils
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition

import com.example.learnjetpack.R
import com.example.learnjetpack.databinding.FragmentDetailBinding
import com.example.learnjetpack.databinding.SendSmsDialogBinding
import com.example.learnjetpack.model.DogBreed
import com.example.learnjetpack.model.DogPalette
import com.example.learnjetpack.model.SmsInfo
import com.example.learnjetpack.util.getProgressDrawable
import com.example.learnjetpack.util.loadImage
import com.example.learnjetpack.viewmodel.DetailViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.fragment_detail.*

/**
 * A simple [Fragment] subclass.
 */
abstract class AbsFragment : Fragment() {

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initView()
        initObservable()
    }

    abstract fun initView()
    abstract fun initObservable()

}


class DetailFragment : AbsFragment() {
    private  var sendSmsStarted = false
    lateinit var viewModel: DetailViewModel
    var dogUuid = 0
    private lateinit var dataBinding: FragmentDetailBinding
    private  var currentDog: DogBreed ?= null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        // Inflate the layout for this fragment
        dataBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_detail, container, false)

        return dataBinding.root
    }

    override fun initView() {
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)

        initData()
        initListener()
    }

    private fun initListener() {
        buttonLists.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.detailFragment)
        }
    }

    override fun initObservable() {
        viewModel.fetch(dogUuid)
        viewModel.dogs.observe(this, Observer { dog ->

            Log.i("===", "=== dog :=="+Gson().toJson(dog))
            currentDog = dog
            dog?.let {
                dataBinding.dog = it

                Log.i("Palette", it.urlImage)
                it.urlImage?.let { color ->
                    setupBackgroundColor(color)
                }
            }

            //                dogName.text = dog.dogBreed
            //                dogLifeSpan.text =dog.lifeSpan.toString()
            //                breedGroup.text= dog.breedGroup
            //                context?.let { imageView2.loadImage(dog.urlImage, getProgressDrawable(it)) }
        })
    }

    private fun initData() {
        arguments.let {
            dogUuid = DetailFragmentArgs.fromBundle(it!!).dogUuid
        }
    }


    private fun setupBackgroundColor(url: String?) {
        if (!TextUtils.isEmpty(url)) {
            Glide.with(this)
                .asBitmap().load(url).into(object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        Palette.from(resource).generate { _palette ->
                            _palette?.apply {
                                val inColor = vibrantSwatch?.rgb ?: 0
                                val myPalette = DogPalette(inColor)

                                Log.i("Palette", myPalette.toString())

                                dataBinding.palette = myPalette

//                                myPalette?.color?.let {
//                                    dataBinding.containerLayout.setBackgroundColor(
//                                        it
//                                    )
//                                }


                            }
                        }
                    }
                })
        }


    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.detail_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_send_sms->{
                    sendSmsStarted = true
                (activity as MainActivity).checkSmsPermission()
            }
            R.id.action_share->{
                val intent = Intent(Intent.ACTION_SEND)
                intent.type="text/plain"
                intent.putExtra(Intent.EXTRA_SUBJECT,"Check out this dog breed")
                intent.putExtra(Intent.EXTRA_TEXT,"${currentDog?.dogBreed} bred for ${currentDog?.bredFor}")
                intent.putExtra(Intent.EXTRA_STREAM,currentDog?.urlImage)
                startActivity(Intent.createChooser(intent,"Share with"))
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun onPermisionResult(permissionGranted:Boolean){
        if(sendSmsStarted && permissionGranted){
            context?.let {
                val smsInfo = SmsInfo("","${currentDog?.dogBreed} bred for ${currentDog?.bredFor}",currentDog?.urlImage)
                val  dialogBinding = DataBindingUtil.inflate<SendSmsDialogBinding>(LayoutInflater.from(it),R.layout.send_sms_dialog,null,false)
                AlertDialog.Builder(it).setView(dialogBinding.root)
                    .setPositiveButton("Send SMS"){
                        dialog, which ->
                        if(!dialogBinding.smdDestination.text.isNullOrEmpty()){
                            smsInfo.to = dialogBinding.smdDestination.text.toString()
                            sendSms(smsInfo)
                        }
                    }
                    .setPositiveButton("Cancel"){
                        dialog, which ->
                    }.show()
                dialogBinding.smsInfo = smsInfo
            }
          
        }
    }
    fun sendSms(smsInfo:SmsInfo){
        val intent = Intent(context,MainActivity::class.java)
        val pi = PendingIntent.getActivity(context,0,intent,0)
        val sms = SmsManager.getDefault()
        sms.sendTextMessage(smsInfo.to, null,smsInfo.text,pi,null)

    }

}
