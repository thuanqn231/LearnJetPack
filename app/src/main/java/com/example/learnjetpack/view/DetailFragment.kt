package com.example.learnjetpack.view

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import com.example.learnjetpack.model.DogPalette
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
    lateinit var viewModel: DetailViewModel
    var dogUuid = 0
    private lateinit var dataBinding: FragmentDetailBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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

}
