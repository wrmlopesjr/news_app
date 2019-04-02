package dev.wilsonjr.newsapp.base.mock.endpoint

import dev.wilsonjr.newsapp.base.mock.MockedEndpointService
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

class EndpointMock(val url: String, val endpointService: MockedEndpointService?) {
    protected var responseHandler: ResponseHandler? = null
    protected var responseHandlerWithCode: ResponseHandlerWithCode? = null
    protected var response: String? = null
    private var code = 200
    var error: HttpException? = null
        private set
    private var method: String? = null

    fun getCode(request: Request): Int {
        if (responseHandlerWithCode != null) {
            val (_, code1) = responseHandlerWithCode!!.getResponseWithCode(request, url)
            return code1
        }
        return code
    }

    fun getResponse(request: Request): String {
        if (responseHandlerWithCode != null) {
            val (body) = responseHandlerWithCode!!.getResponseWithCode(request, url)
            return body
        }
        if (responseHandler != null) {
            return responseHandler!!.getResponse(request, url)
        }
        return response ?: ""
    }

    fun throwConnectionError(): EndpointMock {
        this.code = FORCED_MOCK_EXCEPTION_CODE
        return this
    }

    fun code(code: Int): EndpointMock {
        this.code = code
        return this
    }

    fun body(response: String): EndpointMock {
        this.response = response
        return this
    }

    fun bodyAndCode(handlerWithCode: ResponseHandlerWithCode): EndpointMock {
        this.responseHandlerWithCode = handlerWithCode
        return this
    }

    fun body(handler: ResponseHandler): EndpointMock {
        this.responseHandler = handler
        return this
    }

    fun httpError(code: Int, errorMessage: String): EndpointMock {
        this.code(code)
        this.error = HttpException(errorResponse(code, errorMessage))
        return this
    }

    protected fun errorResponse(code: Int, errorMessage: String): Response<Any> {
        return Response.error(code, ResponseBody.create(TEXT_MEDIA_TYPE, errorMessage))
    }

    fun apply() {
        endpointService?.let {
            var path = url
            if (method != null) {
                path = "$method $path"
            }
            endpointService.addMockedEndpoint(path, this)
        } ?: throw RuntimeException("EndpointService not mocked!")

    }

    fun setMethod(method: String): EndpointMock {
        this.method = method
        return this
    }

    companion object {

        val FORCED_MOCK_EXCEPTION_CODE = 999
        private val TEXT_MEDIA_TYPE = MediaType.parse("text/plain")
    }
}
