package com.digitaldetox.aww.ui.main.root

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.UsersavingrequestRoom
import com.digitaldetox.aww.util.GenericViewHolder
import kotlinx.android.synthetic.main.fragment_usersavingrequest.view.*
import kotlinx.android.synthetic.main.layout_usersavingrequest_filter.view.*
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.*
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.subreddit_title
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.usersavingrequest_authorsender
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.usersavingrequest_body
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.usersavingrequest_savingamount
import kotlinx.android.synthetic.main.layout_usersavingrequest_list_item.view.usersavingrequest_title

class UsersavingrequestListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var total = 0;
    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val USERSAVINGREQUEST_ITEM = 0
    private val NO_MORE_RESULTS_USERSAVINGREQUEST_MARKER = UsersavingrequestRoom(
        pk = NO_MORE_RESULTS,
        title = "",

        body = "",

        savingamount = -1,

        authorsender = "",
        subreddit = "",

        albumId = ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UsersavingrequestRoom>() {

        override fun areItemsTheSame(
            oldItem: UsersavingrequestRoom,
            newItem: UsersavingrequestRoom
        ): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(
            oldItem: UsersavingrequestRoom,
            newItem: UsersavingrequestRoom
        ): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            UsersavingrequestRecyclerChangeCallback(this),
            AsyncDifferConfig.Builder(DIFF_CALLBACK).build()
        )


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when (viewType) {

            NO_MORE_RESULTS -> {
                Log.e(TAG, "onCreateViewHolder: No more results...")
                return GenericViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_no_more_results,
                        parent,
                        false
                    )
                )
            }

            USERSAVINGREQUEST_ITEM -> {
                return UsersavingrequestViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_usersavingrequest_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return UsersavingrequestViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_usersavingrequest_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class UsersavingrequestRecyclerChangeCallback(
        private val adapter: UsersavingrequestListAdapter
    ) : ListUpdateCallback {

        override fun onChanged(position: Int, count: Int, payload: Any?) {
            adapter.notifyItemRangeChanged(position, count, payload)
        }

        override fun onInserted(position: Int, count: Int) {
            adapter.notifyItemRangeChanged(position, count)
        }

        override fun onMoved(fromPosition: Int, toPosition: Int) {
            adapter.notifyDataSetChanged()
        }

        override fun onRemoved(position: Int, count: Int) {
            adapter.notifyDataSetChanged()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is UsersavingrequestViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return USERSAVINGREQUEST_ITEM
        }
        return differ.currentList.get(position).pk
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<UsersavingrequestRoom>
    ) {

        for (element in list) {
            total += element.savingamount
            Log.d(TAG, "preloadGlideImages: totoal: ${total}")
        }
        Log.d(TAG, "preloadGlideImages: ssssssssssssssssssssssssssssss")
        for (usersavingrequest in list) {

            Log.d(TAG, "preloadGlideImages: ${usersavingrequest.savingamount}")

        }
    }

    fun submitList(
        usersavingrequestList: List<UsersavingrequestRoom>?,
        isQueryExhausted: Boolean
    ) {
        val newList = usersavingrequestList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_USERSAVINGREQUEST_MARKER)
        val commitCallback = Runnable {


            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class UsersavingrequestViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("LongLogTag")
        fun bind(item: UsersavingrequestRoom) = with(itemView) {


            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }

            itemView.update_saving_button.setOnClickListener {
                interaction?.onUpdateSavingButtonSelected(adapterPosition, item)
            }



            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val TAG = "lgx_UsersavingrequestListAdapter"
            Log.d(TAG, "bind: 186: usersavingrequestitem $item ")
            Log.d(TAG, "bind: 186: usersavingrequestitem item.body ${item.body} ")

            itemView.usersavingrequest_title.text = item.title

            itemView.usersavingrequest_authorsender.text = item.authorsender

//            itemView.friends_count_text_view.text = total

            itemView.subreddit_title.text = item.subreddit

            itemView.usersavingrequest_body.text = item.body
            itemView.usersavingrequest_savingamount.text = item.savingamount.toString()
//            itemView.usersavingrequest_update_date.text = item.pk.toString()
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: UsersavingrequestRoom)
        fun onItemLongSelected(position: Int, item: UsersavingrequestRoom)
        fun onUpdateSavingButtonSelected(position: Int, item: UsersavingrequestRoom)

        fun restoreListPosition()
    }
}