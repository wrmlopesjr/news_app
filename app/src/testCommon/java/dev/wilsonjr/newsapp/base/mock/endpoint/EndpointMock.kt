package dev.wilsonjr.newsapp.base.mock.endpoint

import dev.wilsonjr.newsapp.base.mock.MockedEndpointService
import okhttp3.MediaType
import okhttp3.Request
import okhttp3.ResponseBody
import retrofit2.HttpException
import retrofit2.Response

//this is the object which mocks an URL
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

    //the response code this mock should send
    fun code(code: Int): EndpointMock {
        this.code = code
        return this
    }

    //the response body this mock should send
    fun body(response: String): EndpointMock {
        this.response = response
        return this
    }

    //a handler to mock dynamically with multiple cases
    fun body(handler: ResponseHandler): EndpointMock {
        this.responseHandler = handler
        return this
    }

    //set the EndpointMock and make it active
    fun apply() {
        endpointService?.let {
            var path = url
            if (method != null) {
                path = "$method $path"
            }
            endpointService.addMockedEndpoint(path, this)
        } ?: throw RuntimeException("EndpointService not mocked!")

    }

    companion object {
        val FORCED_MOCK_EXCEPTION_CODE = 999
    }
}
