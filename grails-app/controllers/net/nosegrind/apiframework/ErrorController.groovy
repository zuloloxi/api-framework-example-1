package net.nosegrind.apiframework


class ErrorController {

    def index() {
        def error = (request.exception?.cause?.message)?request.exception.cause.message:"NullPointerException"
        def status = (response?.status)? response?.status: id
        response.status = status
        render( status: status, text: "${error}")
    }
}
