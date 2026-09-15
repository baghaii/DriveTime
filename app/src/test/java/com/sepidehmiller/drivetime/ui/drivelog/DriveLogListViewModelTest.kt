package com.sepidehmiller.drivetime.ui.drivelog

import app.cash.turbine.test
import com.sepidehmiller.drivetime.MainDispatcherRule
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DriveLogListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<LocalRepository>()

    @Test
    fun `driveTimes emits Empty when repository returns no items`() = runTest {
        every { repository.observeDriveTimes() } returns flowOf(emptyList())

        val viewModel = DriveLogListViewModel(repository)

        viewModel.driveTimes.test {
            assertEquals(DriveTimeState.Empty, awaitItem())
        }
    }

    @Test
    fun `driveTimes emits Loaded with totals when repository returns items`() = runTest {
        val items = listOf(
            LocalDriveTime(
                id = 1,
                date = 1000L,
                dayHours = 2,
                dayMinutes = 30,
                nightHours = 1,
                nightMinutes = 15,
                comments = "First"
            ),
            LocalDriveTime(
                id = 2,
                date = 2000L,
                dayHours = 1,
                dayMinutes = 45,
                nightHours = 0,
                nightMinutes = 30,
                comments = "Second"
            )
        )
        every { repository.observeDriveTimes() } returns flowOf(items)

        val viewModel = DriveLogListViewModel(repository)

        viewModel.driveTimes.test {
            val state = awaitItem()
            assertTrue(state is DriveTimeState.Loaded)
            val loadedState = state as DriveTimeState.Loaded
            assertEquals(2, loadedState.driveTimes.size)
            
            // Total day hours: (2 + 30/60) + (1 + 45/60) = 2.5 + 1.75 = 4.25
            assertEquals(4.25, loadedState.daySum)
            
            // Total night hours: (1 + 15/60) + (0 + 30/60) = 1.25 + 0.5 = 1.75
            assertEquals(1.75, loadedState.nightSum)
        }
    }
}
