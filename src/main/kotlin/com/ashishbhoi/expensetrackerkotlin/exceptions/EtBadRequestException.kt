package com.ashishbhoi.expensetrackerkotlin.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class EtBadRequestException(message: String?) : RuntimeException(message)