package com.irvanyale.app.footballapp.network

import org.junit.Test

import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

class ApiRepositoryTest {

    @Test
    fun testDoRequest() {
        val apiRepository = mock(ApiRepository::class.java)
        val url = "https://www.thesportsdb.com/api/v1/json/1/eventspastleague.php?id=4346"
        apiRepository.doRequest(url)
        verify(apiRepository).doRequest(url)
    }
}