package singtel.assignment.findcardapp.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import singtel.assignment.findcardapp.R

/**
 * Created by Bhavana Wagh on 11/03/2020.
 */
class CardBaseAdapter(private val context: Context,private  val randomValues:List<Int>) : BaseAdapter() {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val inflater = parent?.context?.
            getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view = inflater.inflate(R.layout.card,null)
        val tv = view.findViewById<TextView>(R.id.front_text)
        tv.text = randomValues[position].toString()
        return view
    }

    override fun getItem(p0: Int): Any {
       return randomValues[p0]
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
       return randomValues.size
    }



}