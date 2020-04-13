package com.example.napoleonittest.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.napoleonittest.R
import com.example.napoleonittest.models.Banner
import com.squareup.picasso.Picasso

class BannerAdapter(list: List<Banner>) : RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    var banners: List<Banner> = list

    class BannerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val ivBack: ImageView = itemView.findViewById(R.id.ivBack)
        val rlContent: RelativeLayout = itemView.findViewById(R.id.rlContent)
        val head: TextView = itemView.findViewById(R.id.banner_head)
        val content: TextView = itemView.findViewById(R.id.banner_content)

        fun onBind(position: Int, banner: Banner) {

            Picasso.get().load(banner.image).into(ivBack)

            banner.title.let { title ->
                rlContent.visibility = View.VISIBLE
                head.text = title
                banner.desc.let { desc ->
                    content.text = desc
                }
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.banner_item, parent, false)
        return BannerViewHolder(v)
    }

    override fun getItemCount(): Int {
        return banners.size
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        holder.onBind(position, banners[position])
    }

}