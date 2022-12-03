package com.app.trustlink.ui.ocr.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.app.trustlink.databinding.ItemPerhatianBinding

class ItemAdapter(var items: List<String>): RecyclerView.Adapter<ItemAdapter.PerhatianViewHolder>() {

    class PerhatianViewHolder(val binding: ItemPerhatianBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PerhatianViewHolder {
        val binding = ItemPerhatianBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PerhatianViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PerhatianViewHolder, position: Int) {
        with(holder) {
            with(items[position]) {
                binding.tvPerhatian.text = this
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}