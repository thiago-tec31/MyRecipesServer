package com.br.domain.services.user

import com.br.domain.model.UserFactory
import com.br.infra.repository.user.UserReadOnlyRepository
import com.google.common.truth.Truth.assertThat
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetUserByIdServiceTest {

    private lateinit var userReadOnlyRepository: UserReadOnlyRepository

    private lateinit var getUserByIdService: GetUserByIdService

    private val userAnna = UserFactory().create(UserFactory.UserFake.Anna)

    @BeforeTest
    fun setUp() {
        userReadOnlyRepository = mockk()
        getUserByIdService = GetUserByIdService(userReadOnlyRepository)
    }

    @AfterTest
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `should return user when valid ID is provide`() = runBlocking {
        // GIVEN
        val userId = userAnna.id
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns userAnna

        // WHEN
        val result = getUserByIdService.getUserById(userId)

        // THEN
        assertThat(result).isEqualTo(userAnna)
    }

    @Test
    fun `should return null when no exist user ID is provide`() = runBlocking {
        // GIVEN
        val userId = userAnna.id
        coEvery { userReadOnlyRepository.findUserById(eq(userId)) } returns null

        // WHEN
        val result = getUserByIdService.getUserById(userId)

        // THEN
        assertThat(result).isNull()
    }
}