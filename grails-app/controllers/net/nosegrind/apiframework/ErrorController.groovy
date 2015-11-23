package net.nosegrind.apiframework

/**
 * Created by owenrubel on 11/23/15.
 */
class ErrorController {

    def index() {
        def exception = request.exception.cause
        def message = ExceptionMapper.mapException(exception)
        def status = message.status
        
        render(status:status, text: message)
    }
}
