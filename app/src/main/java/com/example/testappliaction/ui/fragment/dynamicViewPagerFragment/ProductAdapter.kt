package com.example.testappliaction.ui.fragment.dynamicViewPagerFragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.testappliaction.R
import com.example.testappliaction.data.model.Product
import com.example.testappliaction.databinding.ProductItemviewBinding

class ProductAdapter(private var items:MutableList<Product>, private val interaction: Interaction)
    : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: ProductItemviewBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.context), R.layout.product_itemview, parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(items[holder.adapterPosition])
        holder.itemView.setOnClickListener {
            interaction.onItemSelected(holder.adapterPosition, items[holder.adapterPosition])
        }
    }


    override fun getItemCount(): Int {
        return items.size
    }


    class ProductViewHolder(private val binding: ProductItemviewBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Product){
            var qtyValue = 0
            /*binding.img.setImageResource(R.drawable.ic_delivery)*/
            if (!item.product_image.isNullOrEmpty()){
                Glide.with(binding.root.context).load(item.product_image).into(binding.productImg)
            }
            binding.increment.setOnClickListener {
                qtyValue = qtyValue.plus(1)
                binding.qty.text = qtyValue.toString()
            }
            binding.decrement.setOnClickListener {
                if (qtyValue > 0) qtyValue = qtyValue.minus(1)
                binding.qty.text = qtyValue.toString()
            }

            binding.product = item
            binding.executePendingBindings()
        }
    }

    interface Interaction {
        fun onItemSelected(position: Int, item: Product)
    }

    fun setData(list: MutableList<Product>){
        items = list
        notifyDataSetChanged()
    }

}