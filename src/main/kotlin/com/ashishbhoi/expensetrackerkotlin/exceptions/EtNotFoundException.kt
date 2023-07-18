package com.ashishbhoi.expensetrackerkotlin.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.NOT_FOUND)
class EtNotFoundException(message: String?) : RuntimeException(message)