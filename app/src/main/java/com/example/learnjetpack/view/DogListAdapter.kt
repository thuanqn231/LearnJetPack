package com.example.learnjetpack.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.learnjetpack.R
import com.example.learnjetpack.model.DogBreed
import kotlinx.android.synthetic.main.item_dog.view.*

class DogListAdapter(private val dogsList:ArrayList<DogBreed>): RecyclerView.Adapter<DogListAdapter.DogViewHolder>()  {
    fun updateDogList(newDogList: List<DogBreed>){
        dogsList.clear()
        dogsList.addAll(newDogList)
        notifyDataSetChanged()
    }
    class DogViewHolder(var view:View):RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DogViewHolder {
        val inflater= LayoutInflater.from(parent.context)
        val view: View = inflater.inflate(R.layout.item_dog,parent,false)
        return DogViewHolder(view)
    }

    override fun getItemCount(): Int {
      return dogsList.size
    }

    override fun onBindViewHolder(holder: DogViewHolder, position: Int) {
        holder.view.name.text = dogsList[position].dogBreed
        holder.view.lifespan.text = dogsList[position].lifeSpan.toString()
        holder.view.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.listFragment)
        }

    }
}