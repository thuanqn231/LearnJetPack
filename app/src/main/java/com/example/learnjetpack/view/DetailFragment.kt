package com.example.learnjetpack.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation

import com.example.learnjetpack.R
import com.example.learnjetpack.viewmodel.DetailViewModel
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */
class DetailFragment : Fragment() {
    lateinit var viewModel:DetailViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
//        return null
        return inflater.inflate(R.layout.fragment_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        buttonLists.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.detailFragment)
        }
        viewModel = ViewModelProvider(this).get(DetailViewModel::class.java)
        viewModel.fetch()
        observeViewModel()
        //        buttonChild.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.action_detailFragment_to_childFragment)
//        }
    }
    @SuppressLint("FragmentLiveDataObserve")
    fun observeViewModel(){
        viewModel.dogs?.let {dogs->
            dogs.observe(this, Observer {
                dogName.text = it.dogBreed
                dogLifeSpan.text =it.lifeSpan.toString()
                breedGroup.text= it.breedGroup

            })

        }
    }

}
