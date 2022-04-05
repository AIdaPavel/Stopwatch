package pavel.ivanov.stopwatch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MainActivity : AppCompatActivity() {
    lateinit var flowOne: Flow<String>
    lateinit var flowTwo: Flow<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getFlow()
        startFlow()
        findViewById<Button>(R.id.button_search).setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
    }

    private fun getFlow(): Flow<Int> = flow {
        Log.d(TAG, "Start flow")
        (0..10).forEach {
            delay(500)
            Log.d(TAG, "Emitting $it")
            emit(it)
        }
    }
        .map { it * 2 }
        .flowOn(Dispatchers.Default)

    private fun startFlow() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                //import kotlinx.coroutines.flow.collect
                getFlow().collect {
                    Log.d(TAG, it.toString())
                }
            }
        }
    }

    private fun setupFlows() {
        flowOne = flowOf("Юрий", "Александр", "Иван").flowOn(Dispatchers.Default)
        flowTwo = flowOf("Гагарин", "Пушкин", "Грозный").flowOn(Dispatchers.Default)
    }

    private fun zipFlows() {
        findViewById<Button>(R.id.button).setOnClickListener {
            CoroutineScope(Dispatchers.Main).launch {
                flowOne.zip(flowTwo)
                { firstString, secondString ->
                    "$firstString $secondString"
                }.collect {
                    Log.d(TAG, it)
                }
            }
        }
    }

    private fun catchError() {
        CoroutineScope(Dispatchers.Main).launch {
            (1..5).asFlow()
                .map {
                    //выбрасывается ошибка, если значение == 3
                    check(it != 3) { "Значение == $it" } //текст ошибки
                    it * it
                }
                .onCompletion {
                    Log.d(TAG, "onCompletion")
                }
                .catch { e ->
                    Log.d(TAG, "Ошибка: $e")
                }
                .collect {
                    Log.d(TAG, it.toString())
                }
        }
    }

    companion object {
        private const val TAG = "###"
    }
}