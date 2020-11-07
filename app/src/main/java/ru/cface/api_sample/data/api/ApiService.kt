package ru.cface.api_sample.data.api

import io.reactivex.Single
import ru.cface.api_sample.data.model.User

interface ApiService {
    fun getUsers(): Single<List<User>>
}