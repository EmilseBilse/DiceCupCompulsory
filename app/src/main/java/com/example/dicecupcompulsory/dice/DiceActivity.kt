package com.example.dicecupcompulsory.dice

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.github.nisrulz.sensey.Sensey
import com.github.nisrulz.sensey.ShakeDetector.ShakeListener
import com.example.dicecupcompulsory.R
import kotlinx.android.synthetic.main.activity_dice.*


private const val TAG = "DiceActivity"
private const val INTENT_EXTRA_DiceHistoryManager = "diceActivity_intentExtra_HistoryManager"

class DiceActivity : AppCompatActivity() {

    //region Vars and vals
    private val orientation: Int by lazy { resources.configuration.orientation }
    private val SHAKE_THRESHOLD = 60F;
    private val TIME = 300L;
    private val diceViewModel : DiceViewModel by lazy {
        ViewModelProvider(this).get(DiceViewModel::class.java)
    }



    //endregion

    //region override

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "OnCreate(Bundle?) called")
        setContentView(R.layout.activity_dice)


        diceViewModel.diceHistoryManager = intent.getSerializableExtra(
            INTENT_EXTRA_DiceHistoryManager
        ) as DiceHistoryManager

        Sensey.getInstance().init(this);

        val shakeListener: ShakeListener = object : ShakeListener {
            override fun onShakeDetected() {
            }

            override fun onShakeStopped() {
                onClickRoll()
            }
        }
        Sensey.getInstance().startShakeDetection(SHAKE_THRESHOLD, TIME,shakeListener);

        setupOrientation()
        addListeners()
        setupFragments()

        loadDice()

    }

    override fun onSaveInstanceState(savedInstanceState: Bundle) {
        super.onSaveInstanceState(savedInstanceState)
        Log.i(TAG, "onSaveInstanceState")

    }
    //endregion

    //region setup orientation

    private fun setupOrientation() {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            supportActionBar?.hide()
        } else {
            // In portrait
        }
    }
    //endregion

    //region setup fragments

    private fun setupFragments() {
        val currentDiceListFragment = supportFragmentManager.findFragmentById(R.id.diceFragment)
        if(currentDiceListFragment == null) {
            diceViewModel.diceListFragment = DiceListFragment.newInstance()
            supportFragmentManager.beginTransaction()
                .add(R.id.diceFragment, diceViewModel.diceListFragment).commit()
        }
        else{
            diceViewModel.diceListFragment = currentDiceListFragment as DiceListFragment
        }
    }

    //endregion

    //region Setup Listeners

    private fun addListeners() {
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            btnStory.setOnClickListener { onCLickStory() }
        }else{
            btn_clear.setOnClickListener { onClickClear() }
            for (history in diceViewModel.diceHistoryManager.historyList){
                addRollToHistoryUI(history, historyview)
            }
        }
        btnRoll.setOnClickListener { onClickRoll() }
        seekBarDiceAmount.setOnSeekBarChangeListener(diceAmountSetupListener())
    }


    private fun diceAmountSetupListener(): SeekBar.OnSeekBarChangeListener{
        return object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seek: SeekBar,
                                           progress: Int, fromUser: Boolean) {

                updateDiceVisibility(seek.progress)
                diceViewModel.diceListFragment.setDiceAmount(seek.progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        }
    }

    //endregion

    //region OnClick Methods
    private fun onClickRoll(){

        // set dices

        diceViewModel.roll()
        Log.d(TAG, "Roll")

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            historyview.removeAllViews()
            for (history in diceViewModel.diceHistoryManager.historyList) {
                addRollToHistoryUI(history, historyview)
            }
        }

    }

    private fun onCLickStory(){

        val intent = DiceStoryActivity.newIntent(this, diceViewModel.diceHistoryManager)
        startActivity(intent)
        //finish()

    }

    private fun onClickClear(){
        diceViewModel.diceHistoryManager.historyList.clear()
        historyview.removeAllViews()
    }

    //endregion

    //region Dice

    private fun loadDice() {
        diceViewModel.updateFragmentDiceFromHistory()
        updateDiceVisibility(diceViewModel.currentDiceAmount)
    }

    private fun updateDiceVisibility(diceAmount:Int) {
        diceViewModel.currentDiceAmount = diceAmount
        val currentDiceAmount = diceViewModel.currentDiceAmount
        tvDiceCount.text = currentDiceAmount.toString()

    }

    //endregion


    @SuppressLint("ResourceAsColor")
    fun addRollToHistoryUI(rollLog: DiceRollLog, v: LinearLayout){

        val view = layoutInflater.inflate(R.layout.roll, null)

        diceViewModel.addDiceRollToView(view, rollLog)
        v.addView(view)
    }


    companion object {
        fun newIntent(packageContext: Context, dHM: DiceHistoryManager) : Intent {
            return Intent(packageContext, DiceActivity:: class.java).apply {
                putExtra(INTENT_EXTRA_DiceHistoryManager, dHM) }
        }
    }
}