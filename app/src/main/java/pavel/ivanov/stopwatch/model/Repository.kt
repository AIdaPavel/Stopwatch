package pavel.ivanov.stopwatch.model

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class Repository(dataSource: DataSource = DataSource()) {

    val userData: Flow<Data> =
        dataSource.data.map { data -> Data(data) }
}