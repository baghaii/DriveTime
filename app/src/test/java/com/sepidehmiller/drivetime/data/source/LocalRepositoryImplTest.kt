package com.sepidehmiller.drivetime.data.source

import app.cash.turbine.test
import com.sepidehmiller.drivetime.data.source.local.DriveTimeDao
import com.sepidehmiller.drivetime.data.source.local.LocalDriveTime
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class LocalRepositoryImplTest {
    private val fakeDriveTime = LocalDriveTime(
        date = 1,
        dayHours = 1,
        dayMinutes = 1,
        nightHours = 1,
        nightMinutes = 1,
        comments = "test"
    )

    @Test
    fun `observeDriveTime emits value from dao`() = runTest {
        val dao = mockk<DriveTimeDao>()
        every { dao.observeDriveTime(any()) } returns flowOf(fakeDriveTime)

        val subj = LocalRepositoryImpl(dao)

        subj.observeDriveTime(1).test {
            assertEquals(fakeDriveTime, awaitItem())
            awaitComplete()
        }
    }

    @Test
    fun `observeDriveTime completes when dao emits null`() = runTest {
        val dao = mockk<DriveTimeDao>()
        every { dao.observeDriveTime(any()) } returns flowOf(null)

        val subj = LocalRepositoryImpl(dao)

        subj.observeDriveTime(1).test {
            awaitComplete()
        }
    }
}
