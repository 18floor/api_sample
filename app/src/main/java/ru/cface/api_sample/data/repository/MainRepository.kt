package ru.cface.api_sample.data.repository

import io.reactivex.Single
import ru.cface.api_sample.data.api.ApiHelper
import ru.cface.api_sample.data.model.User

class MainRepository(private val apiHelper: ApiHelper) {
    fun getUsers(): Single<List<User>> = apiHelper.getUsers()
}