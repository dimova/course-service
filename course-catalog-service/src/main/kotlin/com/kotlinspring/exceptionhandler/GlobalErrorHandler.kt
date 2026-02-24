package com.kotlinspring.exceptionhandler

import com.kotlinspring.exception.InstructorNotValidException
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode

@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {

    companion object {
        private val log = LoggerFactory.getLogger(GlobalErrorHandler::class.java)
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
        log.error("MethodArgumentNotValidException observed: ${ex.message}", ex)
        val errors = ex.bindingResult.allErrors
            .map { it.defaultMessage ?: "Validation error" }
            .sorted()

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .headers(headers)
            .body(errors.joinToString(", "))
    }

    @ExceptionHandler(InstructorNotValidException::class)
    fun handleInputRequestError(ex: InstructorNotValidException, request: WebRequest): ResponseEntity<Any> {
        log.info("Exception occurred: ${ex.message} on request: $request")
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(
                ex.message
            )
    }

    @ExceptionHandler(java.lang.Exception::class)
    fun handleAllExceptions(ex: java.lang.Exception, request: WebRequest): ResponseEntity<Any> {
        log.info("Exception occurred: ${ex.message} on request: $request", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(
                ex.message
            )
    }
}