package dev.wilsonjr.newsapp.base.mock.endpoint

import okhttp3.Request

interface ResponseHandlerWithCode {
    fun getResponseWithCode(request: Request, path: String): Response
}
