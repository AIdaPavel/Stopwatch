package pavel.ivanov.stopwatch.view

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import pavel.ivanov.stopwatch.R
import pavel.ivanov.stopwatch.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val textView = findViewById<TextView>(R.id.message)
        ViewModelProvider(this).get(MainViewModel::class.java).liveData.observe(
            this,
            { dataFromDataBase ->
                textView.text = dataFromDataBase.data
            })
    }
}