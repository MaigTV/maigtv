package tv.maig.launcher.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Data repository interface providing launcher items and application data.
 */
interface DataRepository {
  val data: Flow<List<String>>
}

/**
 * Default production implementation of [DataRepository].
 */
class DefaultDataRepository : DataRepository {
  override val data: Flow<List<String>> = flow { emit(listOf("Android TV", "Google TV", "Fire OS")) }
}

/**
 * Test and preview fake implementation of [DataRepository].
 */
class FakeDataRepository(
  private val initialData: List<String> = listOf("Sample TV App 1", "Sample TV App 2")
) : DataRepository {
  override val data: Flow<List<String>> = flow { emit(initialData) }
}

