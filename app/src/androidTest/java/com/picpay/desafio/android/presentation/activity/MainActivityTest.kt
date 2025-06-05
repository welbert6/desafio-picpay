
package com.picpay.desafio.android.presentation.activity

import androidx.lifecycle.Lifecycle
import androidx.test.core.app.launchActivity
import androidx.test.platform.app.InstrumentationRegistry
import com.picpay.desafio.android.di.testModules
import com.picpay.desafio.android.presentation.robot.MainActivityRobot
import okhttp3.mockwebserver.*
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

class MainActivityTest {

    private val server = MockWebServer()
    private val context = InstrumentationRegistry.getInstrumentation().targetContext

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            androidContext(context)
            modules(testModules)
        }

        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/users" -> successResponse
                    else -> errorResponse
                }
            }
        }

        server.start(serverPort)
    }

    @After
    fun tearDown() {
        stopKoin()
        server.shutdown()
    }

    @Test
    fun shouldShowFirstItemSuccess() {
        launchActivity<MainActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            MainActivityRobot().verificaItemComTextoEsperado("Eduardo Santos")
        }
    }

    @Test
    fun shouldNotShowRecyclerIfError() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return errorResponse
            }
        }

        launchActivity<MainActivity>().apply {
            moveToState(Lifecycle.State.RESUMED)
            MainActivityRobot().verificaRecyclerInvisivel()
        }
    }

    companion object {
        private const val serverPort = 8080

        private val successResponse by lazy {
            val body =
                "[{\"id\":1001,\"name\":\"Eduardo Santos\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@eduardo.santos\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy {
            MockResponse().setResponseCode(404)
        }
    }
}
