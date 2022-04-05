package pavel.ivanov.stopwatch

import android.os.Bundle
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class SearchActivity : AppCompatActivity() {

    private val job: Job = Job()
    private val queryStateFlow = MutableStateFlow("")
    private lateinit var searchView: SearchView
    private lateinit var textView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        setUpSearchStateFlow()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    private fun setUpSearchStateFlow() {
        searchView = findViewById(R.id.search_view)
        textView = findViewById(R.id.result_text_view)

        CoroutineScope(Dispatchers.Main + job).launch {
            queryStateFlow.debounce(500)
                .filter { query ->
                    if (query.isEmpty()) {
                        textView.text = ""
                        return@filter false
                    } else {
                        return@filter true
                    }
                }
                .distinctUntilChanged()
                .flatMapLatest { query ->
                    dataFromNetwork(query)
                        .catch {
                            emit("")
                        }
                }
                .collect { result -> textView.text = result }
        }

        searchView.setOnQueryTextListener(object :
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { queryStateFlow.value = it }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                queryStateFlow.value = newText
                return true
            }
        })
    }

    //Имитируем загрузку данных по результатам ввода
    private fun dataFromNetwork(query: String): Flow<String> {
        return flow {
            delay(2000)
            emit(query)
        }
    }
}