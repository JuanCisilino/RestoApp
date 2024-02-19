package com.starlabs.restoapp.ui.adapters

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.starlabs.restoapp.databinding.ItemBinding
import com.starlabs.restoapp.model.MenuItem

class ItemAdapter (
    private val itemMenuList: List<MenuItem>) : RecyclerView.Adapter<ItemAdapter.ViewHolder>(){

    var onItemMenuClickCallback : ((product: MenuItem) -> Unit)? = null

    inner class ViewHolder(val binding: ItemBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder){
            with(itemMenuList[position]) {
                val itemMenu = this
                if (!imagen.isNullOrBlank()) Picasso.get().load(Uri.parse(imagen)).into(binding.image)
                with(binding){
                    nameTextView.text = titulo
                    descriptionTextView.text = descripcion
                    priceText.text = "$ $precio"
                    cardLayout.setOnClickListener { onItemMenuClickCallback?.invoke(itemMenu) }
                }
            }
        }
    }

    override fun getItemCount() = itemMenuList.size
}