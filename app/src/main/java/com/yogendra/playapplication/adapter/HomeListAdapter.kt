package com.yogendra.playapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yogendra.playapplication.data.Itemdetail
import com.yogendra.playapplication.databinding.HomeFragmentListItemBinding
import com.yogendra.playapplication.ui.home.HomeFragmentDirections
import com.yogendra.socialmediamvvm.utils.ui_components.MultilineSnackbar

class HomeListAdapter :
    PagedListAdapter<Itemdetail, HomeListAdapter.ViewHolder>(
        ArticlesDiffCallback()
    ) {
    private lateinit var recyclerView: RecyclerView

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            HomeFragmentListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.apply { bind(it) } }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    class ViewHolder(private val binding: HomeFragmentListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Itemdetail) {
            binding.apply {
                binding.itemModel = item

                binding.userClickListener = View.OnClickListener { v ->

                    item.url?.let {

                        navigateToWebScreen(
                            view = v,
                            web_link = item.url, title = item.title
                        )
                    }
                    if (item.url == null || item.url.length < 1) {
                        MultilineSnackbar(binding.root, "No url found").show()
                    }

                }
                executePendingBindings()

            }
        }
    }


}

fun navigateToWebScreen(view: View, web_link: String, title: String?) {
    val directions =
        HomeFragmentDirections.actionHomeFragmentToDetailsFragment(title, web_link)

    view.findNavController().navigate(
        directions
    )
}

private class ArticlesDiffCallback : DiffUtil.ItemCallback<Itemdetail>() {

    override fun areItemsTheSame(oldItem: Itemdetail, newItem: Itemdetail): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Itemdetail, newItem: Itemdetail): Boolean {
        return oldItem == newItem
    }

}