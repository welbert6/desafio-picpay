
package com.picpay.desafio.android.presentation.robot

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import com.picpay.desafio.R
import com.picpay.desafio.android.presentation.RecyclerViewMatchers

class MainActivityRobot {

    fun verificaItemComTextoEsperado(texto: String): MainActivityRobot {
        RecyclerViewMatchers.checkRecyclerViewItem(
            resId = R.id.recyclerView,
            position = 0,
            withMatcher = withText(texto)
        )
        return this
    }


    fun verificaRecyclerInvisivel(): MainActivityRobot {
        onView(withId(R.id.recyclerView)).check(matches(withEffectiveVisibility(Visibility.GONE)))
        return this
    }
}
