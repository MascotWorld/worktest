package com.example.napoleonittest.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.napoleonittest.R
import com.example.napoleonittest.models.Offer
import com.squareup.picasso.Picasso

class OfferAdapter(list: List<Any>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var offers: List<Any> = list

    var groupCount = 0

    private class TitleViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val headerView: TextView = itemView.findViewById(R.id.header)

        fun onBind(position: Int, header: String) {

            headerView.text = header

        }
    }

    private class OfferViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        val ivImg: ImageView = itemView.findViewById(R.id.ivImg)
        val nameItem: TextView = itemView.findViewById(R.id.name)
        val desc: TextView = itemView.findViewById(R.id.desc)

        val discount: TextView = itemView.findViewById(R.id.discount)

        val new_cost: TextView = itemView.findViewById(R.id.new_cost)
        val old_cost: TextView = itemView.findViewById(R.id.old_cost)

        val ivBusket = itemView.findViewById<ImageView>(R.id.ivBusket)

        fun onBind(position: Int, offer: Offer) {

            Picasso.get().load(offer.image).into(ivImg)

            nameItem.text = offer.name ?: ""
            desc.text = offer.desc ?: itemView.context.getString(R.string.offer_empty_desc)

            offer.discount?.let { disc->

                discount.text = "-${(disc * 100).toInt()}%"
                discount.visibility = View.VISIBLE


                offer.price?.let { price->
                    old_cost.visibility = View.VISIBLE
                    new_cost.visibility = View.VISIBLE

                    old_cost.text = itemView.context.getString(R.string.offer_price_rub, price)
                    old_cost.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    new_cost.text = itemView.context.getString(R.string.offer_price_rub, (price * (1 - disc)).toInt())

                }

            }


        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_OFFER)
            OfferViewHolder(
                LayoutInflater.from(parent.context).inflate(
                    R.layout.offer_item,
                    parent,
                    false
                )
            )
        else TitleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.divider,
                parent,
                false
            )
        )

    }

    override fun getItemCount(): Int {
        return offers.size
    }

    override fun getItemViewType(position: Int): Int {

        return when (offers[position]) {
            is Offer -> TYPE_OFFER
            else -> TYPE_HEADER
        }

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is TitleViewHolder) {
            holder.onBind(position, offers[position] as String)
        } else if (holder is OfferViewHolder){
            holder.onBind(position, offers[position] as Offer)
        }


    }

    companion object {
        const val TYPE_HEADER = 1
        const val TYPE_OFFER = 0
    }

}