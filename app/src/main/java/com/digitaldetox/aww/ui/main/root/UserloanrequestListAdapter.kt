package com.digitaldetox.aww.ui.main.root

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.*
import com.bumptech.glide.RequestManager
import com.digitaldetox.aww.R
import com.digitaldetox.aww.models.UserloanrequestRoom
import com.digitaldetox.aww.util.GenericViewHolder
import kotlinx.android.synthetic.main.layout_subreddit_list_item.view.*
import kotlinx.android.synthetic.main.layout_userloanrequest_list_item.view.*
import kotlinx.android.synthetic.main.layout_userloanrequest_list_item.view.subreddit_title

class UserloanrequestListAdapter(
    private val requestManager: RequestManager,
    private val interaction: Interaction? = null
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TAG: String = "AppDebug"
    private val NO_MORE_RESULTS = -1
    private val USERLOANREQUEST_ITEM = 0
    private val NO_MORE_RESULTS_USERLOANREQUEST_MARKER = UserloanrequestRoom(
        pk = NO_MORE_RESULTS,
        title = "",

        body = "",
        loanamount = -1,

        authorsender = "",
        subreddit = "",

        albumId = ""
    )

    val DIFF_CALLBACK = object : DiffUtil.ItemCallback<UserloanrequestRoom>() {

        override fun areItemsTheSame(oldItem: UserloanrequestRoom, newItem: UserloanrequestRoom): Boolean {
            return oldItem.pk == newItem.pk
        }

        override fun areContentsTheSame(oldItem: UserloanrequestRoom, newItem: UserloanrequestRoom): Boolean {
            return oldItem == newItem
        }

    }
    private val differ =
        AsyncListDiffer(
            UserloanrequestRecyclerChangeCallback(this),
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

            USERLOANREQUEST_ITEM -> {
                return UserloanrequestViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_userloanrequest_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
            else -> {
                return UserloanrequestViewHolder(
                    LayoutInflater.from(parent.context).inflate(
                        R.layout.layout_userloanrequest_list_item,
                        parent,
                        false
                    ),
                    interaction = interaction,
                    requestManager = requestManager
                )
            }
        }
    }

    internal inner class UserloanrequestRecyclerChangeCallback(
        private val adapter: UserloanrequestListAdapter
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
            is UserloanrequestViewHolder -> {
                holder.bind(differ.currentList.get(position))
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (differ.currentList.get(position).pk > -1) {
            return USERLOANREQUEST_ITEM
        }
        return differ.currentList.get(position).pk
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    fun preloadGlideImages(
        requestManager: RequestManager,
        list: List<UserloanrequestRoom>
    ) {
        for (userloanrequest in list) {

        }
    }

    fun submitList(
        userloanrequestList: List<UserloanrequestRoom>?,
        isQueryExhausted: Boolean
    ) {
        val newList = userloanrequestList?.toMutableList()
        if (isQueryExhausted)
            newList?.add(NO_MORE_RESULTS_USERLOANREQUEST_MARKER)
        val commitCallback = Runnable {


            interaction?.restoreListPosition()
        }
        differ.submitList(newList, commitCallback)
    }

    class UserloanrequestViewHolder
    constructor(
        itemView: View,
        val requestManager: RequestManager,
        private val interaction: Interaction?
    ) : RecyclerView.ViewHolder(itemView) {

        @SuppressLint("LongLogTag")
        fun bind(item: UserloanrequestRoom) = with(itemView) {

            itemView.setOnLongClickListener {
                interaction?.onItemLongSelected(adapterPosition, item)
                return@setOnLongClickListener true
            }

            itemView.update_loan_button.setOnClickListener {
                interaction?.onUpdateLoanButtonSelected(adapterPosition, item)
            }

            itemView.setOnClickListener {
                interaction?.onItemSelected(adapterPosition, item)
            }
            val TAG = "lgx_UserloanrequestListAdapter"
            Log.d(TAG, "bind: 186: userloanrequestitem $item ")
            Log.d(TAG, "bind: 186: userloanrequestitem item.body ${item.body} ")

            itemView.userloanrequest_title.text = item.title

            itemView.userloanrequest_authorsender.text = item.authorsender

            itemView.subreddit_title.text = item.subreddit

            itemView.userloanrequest_body.text = item.body
            itemView.userloanrequest_loanamount.text = item.loanamount.toString()
//            itemView.userloanrequest_update_date.text = item.pk.toString()
        }
    }

    interface Interaction {

        fun onItemSelected(position: Int, item: UserloanrequestRoom)
        fun onItemLongSelected(position: Int, item: UserloanrequestRoom)
        fun onUpdateLoanButtonSelected(position: Int, item: UserloanrequestRoom)
        fun restoreListPosition()
    }
}