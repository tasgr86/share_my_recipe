package tasos.grigoris.sharemyrecipe

import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormat
import java.text.DecimalFormat

class Utils{

    val fmt = DateTimeFormat.forPattern("yyyyMMdd")

    fun formatPosotita(input : Double) : String{

        println("formatting: ".plus(input))

        if (input == 0.0)
            return ""

        val df = DecimalFormat("###.#")
        return df.format(input)

    }

    fun changeQuantity(input : Double, times : Int) : String{

        if (input == 0.0)
            return ""

        val df = DecimalFormat("###.#")
        return df.format(input * times)

    }

    fun getCurrentDate() : String{

        return fmt.print(LocalDate.now())

    }


}