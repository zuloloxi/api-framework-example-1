package net.nosegrind.apiframework


class ErrorController {

    def index() {
        def message = request.exception.cause.message
        def status = message.status
        render( status: status, text: "${message}")
    }
}
