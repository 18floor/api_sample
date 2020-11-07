package ru.cface.api_sample.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_user.view.*
import ru.cface.api_sample.R
import ru.cface.api_sample.data.model.User

class MainAdapter(
    private val users: ArrayList<User>
) : RecyclerView.Adapter<MainAdapter.DataViewHolder>(), Filterable {

    var usersFilterList = ArrayList<User>()

    init {
        usersFilterList = users
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder =
        DataViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_user, parent, false
            )
        )

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) =
        holder.bind(usersFilterList[position])

    override fun getItemCount(): Int = usersFilterList.size

    class DataViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(user: User) {
            itemView.txtUserNameView.text = user.name
            itemView.txtUserEmailView.text = user.email
            Glide.with(itemView.imgAvatarView.context)
                .load(user.avatar)
                .into(itemView.imgAvatarView)
        }
    }

    fun addData(list: List<User>) {
        usersFilterList.addAll(list)
    }

    override fun getFilter(): Filter {
        return object : Filter() {

            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    usersFilterList = users
                } else {
                    val resultList = ArrayList<User>()
                    for (row in users) {
                        if (row.name.contains(charSearch)) {
                            resultList.add(row)
                        }
                    }
                    usersFilterList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = usersFilterList
                return filterResults
            }

            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                usersFilterList = results?.values as ArrayList<User>
                notifyDataSetChanged()
            }
        }
    }
}