package com.sepidehmiller.drivetime.ui.drivelogdetail

import androidx.lifecycle.SavedStateHandle
import app.cash.turbine.test
import com.sepidehmiller.drivetime.MainDispatcherRule
import com.sepidehmiller.drivetime.data.source.LocalRepository
import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import com.sepidehmiller.drivetime.ui.navigation.NavDestinationArgs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DriveLogDetailViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<LocalRepository>()
    private val savedStateHandle = SavedStateHandle(mapOf(NavDestinationArgs.DRIVE_LOG_ID_ARG to 123))

    private val fakeDriveTime = LocalDriveTime(
        id = 123,
        date = 1718020800000L, // June 10, 2024
        dayHours = 5,
        dayMinutes = 30,
        nightHours = 2,
        nightMinutes = 15,
        comments = "Night drive test"
    )

    @Test
    fun `uiState initializes with data from repository`() = runTest {
        every { repository.observeDriveTime(123) } returns flowOf(fakeDriveTime)

        val viewModel = DriveLogDetailViewModel(repository, savedStateHandle)

        viewModel.uiState.test {
            // First item should be loading or the loaded state immediately due to UnconfinedTestDispatcher
            val state = awaitItem()
            assertTrue(state is DriveTimeDetailUiState.DriveTimeDetailUi)
            val uiData = state as DriveTimeDetailUiState.DriveTimeDetailUi
            assertEquals("5", uiData.dayHours)
            assertEquals("30", uiData.dayMinutes)
            assertEquals("2", uiData.nightHours)
            assertEquals("15", uiData.nightMinutes)
            assertEquals("Night drive test", uiData.comments)
        }
    }

    @Test
    fun `deleteDriveLog calls repository and triggers Delete event`() = runTest {
        every { repository.observeDriveTime(123) } returns flowOf(fakeDriveTime)
        coEvery { repository.deleteDriveTime(123) } returns Unit

        val viewModel = DriveLogDetailViewModel(repository, savedStateHandle)

        viewModel.uiEvent.test {
            viewModel.deleteDriveLog()
            assertEquals(DetailUiEvent.Delete, awaitItem())
        }

        coVerify { repository.deleteDriveTime(123) }
    }
}
