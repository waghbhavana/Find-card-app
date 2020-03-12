package singtel.assignment.findcardapp.utils

import singtel.assignment.findcardapp.Updatable

/**
 * Created by Bhavana Wagh on 12/03/2020.
 */
class util(){

    // Custom method to generate list of color name value pair
    fun numberList(size:Int):List<Int>{
        val numList=ArrayList<Int>()
        var x = 0
        while (x < size) {
            var num = (0..10).random()
            numList.add(num)
            numList.add(num)
            x++
        }
        return numList.shuffled()
    }


}