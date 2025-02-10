package com.dfinery.attribution.api.exceptionhandler

import com.dfinery.attribution.api.model.response.ResponseDTO
import com.dfinery.attribution.common.exception.*
import mu.KotlinLogging
import org.springframework.http.*
import org.springframework.stereotype.Component
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@Component
@ControllerAdvice
class GlobalErrorHandler : ResponseEntityExceptionHandler() {
    companion object {
        private val log = KotlinLogging.logger {}
    }

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {
//        return super.handleMethodArgumentNotValid(ex, headers, status, request)
        log.error("MethodArgumentNotValidException observed : ${ex.message}", ex)
        val errors = ex.bindingResult.allErrors
            .map { error -> error.defaultMessage!! }
            .sorted()

        log.info("errors : $errors")

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(errors.joinToString(", ") { it })
    }

    @ExceptionHandler(AdtouchNotFoundException::class)
    fun handleAdtouchNotFoundExceptions(ex: AdtouchNotFoundException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.NOT_FOUND.value(),
                    ex.message ?: HttpStatus.NOT_FOUND.reasonPhrase
                )
            )
    }

    @ExceptionHandler(InvalidEventItemException::class)
    fun handleInvalidEventItemExceptions(ex: InvalidEventItemException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.message ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }

    @ExceptionHandler(InvalidEventLogIdException::class)
    fun handleInvalidEventLogIdExceptions(ex: InvalidEventLogIdException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.message ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }

    @ExceptionHandler(InvalidEventTypeException::class)
    fun handleInvalidEventTypeExceptions(ex: InvalidEventTypeException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.message ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }

    @ExceptionHandler(PostbackFailedException::class)
    fun handlePostbackFailedExceptions(ex: PostbackFailedException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                )
            )
    }

    @ExceptionHandler(ProfileNotFoundException::class)
    fun handleProfileNotFoundExceptions(ex: ProfileNotFoundException, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.BAD_REQUEST.value(),
                    ex.message ?: HttpStatus.BAD_REQUEST.reasonPhrase
                )
            )
    }

    @ExceptionHandler(Exception::class)
    fun handleAllExceptions(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        log.error("Exception observed : ${ex.message}", ex)
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(
                ResponseDTO(
                    HttpStatus.INTERNAL_SERVER_ERROR.value(),
                    ex.message ?: HttpStatus.INTERNAL_SERVER_ERROR.reasonPhrase
                )
            )
    }
}