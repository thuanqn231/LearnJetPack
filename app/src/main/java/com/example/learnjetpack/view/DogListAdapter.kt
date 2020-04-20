package com.example.learnjetpack.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.learnjetpack.R
import com.example.learnjetpack.databinding.ItemDogBinding
import com.example.learnjetpack.model.DogBreed
import com.example.learnjetpack.util.getProgressDrawable
import com.example.learnjetpack.util.loadImage
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(private val dogsList: ArrayList<DogBreed>) :
    RecyclerView.Adapter<DogListAdapter.DogViewHolder>(),DogClickListener {
    fun updateDogList(newDogList: List<DogBreed>) {
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }

    class DogViewHolder(var view: ItemDogBinding) : RecyclerView.ViewHolder(view.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = DataBindingUtil.inflate<ItemDogBinding>(inflater,R.layout.item_dog,parent,false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dogsList.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.dog = dogsList[position]
        holder.view.listener = this
//        holder.view.name.text = dogsList[position].dogBreed
//        holder.view.lifespan.text = dogsList[position].lifeSpan.toString()
//        holder.view.setOnClickListener {
//            val action = ListFragmentDirections.listFragment(dogUuid = dogsList[position].uuid)
//            Navigation.findNavController(it).navigate(action)
//        }
//        holder.view.imageView.loadImage(
//            dogsList[position].urlImage,
//            getProgressDrawable(holder.view.imageView.context)
//        )

    }

    override fun onDogClicked(v: View) {
        val dogID = v.dogId.text.toString().toInt()
        val action = ListFragmentDirections.listFragment(dogUuid =dogID)
            Navigation.findNavController(v).navigate(action)
    }
}