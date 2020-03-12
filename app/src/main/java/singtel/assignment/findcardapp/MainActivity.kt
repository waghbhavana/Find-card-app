package singtel.assignment.findcardapp

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.AdapterView.OnItemClickListener
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.card.*
import kotlinx.android.synthetic.main.card_front.view.*
import singtel.assignment.findcardapp.adapter.CardBaseAdapter
import singtel.assignment.findcardapp.data.Card
import singtel.assignment.findcardapp.utils.util

class MainActivity : AppCompatActivity(), Updatable {

    //Initialize required attributes
    private var mSetRightOut: AnimatorSet? = null
    private var mSetLeftIn: AnimatorSet? = null
    private var openCardList: MutableList<Card> = ArrayList()
    private var clickCounter = 0
    private val dataClass = util()
    private var cardNumList: List<Int> = ArrayList()
    private lateinit var adapter: CardBaseAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Intialize the adapter here to assign card data to grid view and here you can set X number of pair
        restartGame()
        // Configure the grid view
        grid_view.numColumns = 3
        grid_view.horizontalSpacing = 10
        grid_view.verticalSpacing = 10

        // Set an item click listener for GridView widget
        grid_view.onItemClickListener = OnItemClickListener { _, view, position, id ->

            val frontView: View = view.findViewById(R.id.card_front)
            val backView: View = view.findViewById(R.id.card_back)
            val currentValue = frontView.front_text.text.toString()
            clickCounter++
            // Display number of steps
            steps.text = clickCounter.toString()
            compareItems(currentValue.toInt(), frontView, backView)
        }

        //On restart button click
        restart_btn.setOnClickListener { restartGame() }
    }

    // function to compare the cards and decide whether the should open or close as per conditions
    private fun compareItems(value: Int, frontView: View, backView: View) {
        if (openCardList.isEmpty() || clickCounter % 2 != 0) {
            loadAnimations()
            changeCameraDistance()
            flipfront(frontView, backView)
            openCardList.add(Card(value, frontView, backView, true))
        } else if (openCardList.size > 0 && clickCounter % 2 == 0) {
            val index = openCardList.lastIndex
            loadAnimations()
            changeCameraDistance()
            flipfront(frontView, backView)
            if (openCardList[index].value == value) {
                openCardList.add(Card(value, frontView, backView, true))
                openCardList[index].isMatch = true
            } else {
                openCardList.add(Card(value, frontView, backView, false))
                openCardList[index].isMatch = false
            }

            // Flip the tow cards together which are not match, after 1 sec of latter
            Handler().postDelayed({
                val iterator = openCardList.iterator()
                for (it in iterator) {
                    if (!it.isMatch) {
                        loadAnimations()
                        changeCameraDistance()
                        flipBack(frontView, backView)
                        flipBack(it.frontView, it.backView)
                        iterator.remove()
                    }
                }
            }, 1000)

            if (openCardList.size == cardNumList.size) {
                Handler().postDelayed({
                    checkResult()
                },1000)
            }
        }

    }


    //change camera distance to show animation  more smooth
    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        card_front.cameraDistance = scale
        card_back.cameraDistance = scale
    }

    //load the animation
    private fun loadAnimations() {
        mSetRightOut = AnimatorInflater.loadAnimator(this, R.animator.out_animation) as AnimatorSet?
        mSetLeftIn = AnimatorInflater.loadAnimator(this, R.animator.in_animation) as AnimatorSet?
    }

    //flip the front card back
    private fun flipBack(frontView: View?, backView: View?) {
        mSetRightOut?.setTarget(frontView)
        mSetLeftIn?.setTarget(backView)
        mSetRightOut?.start()
        mSetLeftIn?.start()
    }

    //flip the back card to front
     private  fun flipfront(frontView: View?, backView: View?) {
        mSetRightOut?.setTarget(backView)
        mSetLeftIn?.setTarget(frontView)
        mSetRightOut?.start()
        mSetLeftIn?.start()
    }
    //Check the result
    private fun checkResult() {
        var check = false
        openCardList.forEach {
            check = it.isMatch
        }
        if (check)
            displayResult(this, "Congratulation", "You won game by $clickCounter steps", "Try another round")
        else
            displayResult(this, "Sorry", "You lost the game ", "Try again")

    }

   private fun displayResult(context: Context, tittle: String, msg: String, again: String) {
        val builder = AlertDialog.Builder(context)
        builder.setTitle(tittle)
        builder.setMessage(msg)
        builder.setPositiveButton(again) { _, which ->
            restartGame()
        }
        builder.show()
   }


    private fun restartGame() {
        clickCounter = 0
        steps.text = clickCounter.toString()
        openCardList.clear()
        //Get the card data you can change size if pairs from here by passing parameter
        cardNumList = dataClass.numberList(5)
        adapter = CardBaseAdapter(this, cardNumList)
        grid_view.adapter = adapter
        Handler(Looper.getMainLooper()).post {
            adapter.notifyDataSetChanged()
            grid_view.invalidate()
        }
    }

    override fun update() {

            adapter.notifyDataSetChanged()
            adapter.notifyDataSetInvalidated()
    }
}
