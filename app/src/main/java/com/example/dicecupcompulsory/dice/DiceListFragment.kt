package com.example.dicecupcompulsory.dice

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicecupcompulsory.R
import kotlin.math.sqrt

private const val TAG = "DiceListFragment"
class DiceListFragment: Fragment() {

    //region vals and vars

    private lateinit var diceRecyclerView: RecyclerView
    private var adapter: DiceAdapter? = null

    private val diceViewModel: DiceViewModel by lazy {
        ViewModelProvider(this).get(DiceViewModel::class.java)
    }

    //endregion

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "Total dice: ${diceViewModel.dice.size}")

        diceViewModel.updateDice()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_dice_list, container, false)

        diceRecyclerView = view.findViewById(R.id.dice_recycler_view) as RecyclerView
        var layoutCol = 3
        if(sqrt(diceViewModel.currentDiceAmount.toDouble()).toInt() > 3){
            layoutCol = sqrt(diceViewModel.currentDiceAmount.toDouble()).toInt();
        }
        diceRecyclerView.layoutManager = GridLayoutManager(context, layoutCol)

        updateUI()

        return view
    }
    //endregion


    //region dice

    fun setDiceAmount(diceAmount: Int){
        diceViewModel.currentDiceAmount = diceAmount
        diceViewModel.updateDice()
        updateUI()
    }

    fun rollDice(diceHistoryManager: DiceHistoryManager){
        diceViewModel.rollDice(diceHistoryManager)
        updateUI()
    }

    fun setDiceFromRoll(diceRollLog: DiceRollLog){
        diceViewModel.updateDiceFromHistory(diceRollLog)
    }

    private fun updateUI() {
        val dices = diceViewModel.dice
        adapter = DiceAdapter(dices)
        diceRecyclerView.adapter = adapter
    }

    //endregion


    private inner class DiceHolder(view:View): RecyclerView.ViewHolder(view) {
        val diceImageView : ImageView = itemView.findViewById(R.id.diceImage)

    }

    private inner class DiceAdapter(var dice:List<Dice>) : RecyclerView.Adapter<DiceHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DiceHolder {
            val view = layoutInflater.inflate(R.layout.dice_fragment, parent, false)
            return DiceHolder(view)
        }

        override fun getItemCount(): Int {
            return dice.size
        }

        override fun onBindViewHolder(holder: DiceHolder, position: Int) {
            val dice = dice[position]
            holder.apply {
                diceImageView.setImageResource(diceViewModel.diceImages[dice.currentEyes])
            }
        }
    }

    companion object{
        fun newInstance(): DiceListFragment {
            return DiceListFragment()
        }
    }
}