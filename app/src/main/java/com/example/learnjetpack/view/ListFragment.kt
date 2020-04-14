package com.example.learnjetpack.view

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.ListFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.LinearLayoutManager

import com.example.learnjetpack.R
import com.example.learnjetpack.databinding.FragmentListBinding
import com.example.learnjetpack.viewmodel.ListViewModel
import kotlinx.android.synthetic.main.fragment_list.*

/**
 * A simple [Fragment] subclass.
 */

class ListFragment : Fragment() {
    private val adapterDogList = DogListAdapter(arrayListOf())
    private  lateinit var viewModel: ListViewModel
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        buttonDetails.setOnClickListener {
//            Navigation.findNavController(it).navigate(R.id.listFragment)
//        }

        viewModel = ViewModelProviders.of(this).get(ListViewModel::class.java)
        viewModel.refresh()
        dogsList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = adapterDogList

        }
        refreshLayout.setOnRefreshListener {
            dogsList.visibility = View.GONE
            listError.visibility = View.GONE
            loadingView.visibility = View.VISIBLE
            viewModel.refresh()
            refreshLayout.isRefreshing = false

        }
        observeViewModel()
    }
    @SuppressLint("FragmentLiveDataObserve")
    fun  observeViewModel(){
        viewModel.dogs.observe(this, Observer {dogs->
            dogs?.let {
                dogsList.visibility = View.VISIBLE
                adapterDogList.updateDogList(dogs)
            }
        })
        viewModel.dogsLoadError.observe(this, Observer { isError->
            isError?.let {
                listError.visibility  = if(it) View.VISIBLE else View.GONE
            }
        })
        viewModel.loading.observe(this, Observer { isLoading->
            isLoading?.let {
                loadingView.visibility = if(it) View.VISIBLE else View.GONE
            }
        })

    }

}
