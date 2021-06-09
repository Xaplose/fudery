package bangkit.xaplose.fudery.ui.fooddetails

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import bangkit.xaplose.fudery.R
import bangkit.xaplose.fudery.data.source.remote.response.NutrientsItem
import bangkit.xaplose.fudery.databinding.ItemRowNutrientBinding

class FoodNutrientAdapter(
    private val context: Context
): RecyclerView.Adapter<FoodNutrientAdapter.ListViewHolder>() {

    private val nutrients = ArrayList<NutrientsItem>()

    fun setData(data: List<NutrientsItem>) {
        nutrients.clear()
        nutrients.addAll(data)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_nutrient, parent, false)
        )

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val item = nutrients[position]
        holder.bind(item)
    }

    override fun getItemCount() = nutrients.size

    inner class ListViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        private val binding = ItemRowNutrientBinding.bind(itemView)
        fun bind(item: NutrientsItem) {
            with(binding) {
                tvNutrientName.text = item.name
                tvNutrientAmount.text = context.getString(R.string.desc_nutrient_amount, item.amount, item.unit)
            }
        }
    }
}