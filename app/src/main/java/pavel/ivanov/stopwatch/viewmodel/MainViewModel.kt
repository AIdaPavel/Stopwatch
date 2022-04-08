package pavel.ivanov.stopwatch.viewmodel

import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import pavel.ivanov.stopwatch.model.Data
import pavel.ivanov.stopwatch.model.Repository

internal class MainViewModel(
    repository: Repository = Repository()
) : ViewModel() {

    val liveData: MutableLiveData<Data> = MutableLiveData()

    init {
        viewModelScope.launch {
            repository.userData.flowOn(Dispatchers.Main)
                .collect { data ->
                    liveData.value = data
                }
        }
    }
}