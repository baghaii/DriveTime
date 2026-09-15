package com.sepidehmiller.drivetime.ui.driveloginput

import com.sepidehmiller.drivetime.MainDispatcherRule
import com.sepidehmiller.drivetime.data.source.LocalRepository
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class DriveLogInputViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository = mockk<LocalRepository>()

    @Test
    fun `addDriveLog parses strings and saves log to repository`() = runTest {
        coEvery { repository.updateDriveTime(any()) } returns Unit

        val viewModel = DriveLogInputViewModel(repository)

        viewModel.addDriveLog(
            dateString = "06/10/2024",
            dayHoursString = "2",
            dayMinutesString = "45",
            nightHoursString = "1",
            nightMinutesString = "15",
            comments = "Testing input"
        )

        coVerify {
            repository.updateDriveTime(match {
                it.dayHours == 2L &&
                it.dayMinutes == 45L &&
                it.nightHours == 1L &&
                it.nightMinutes == 15L &&
                it.comments == "Testing input"
            })
        }
    }
}
