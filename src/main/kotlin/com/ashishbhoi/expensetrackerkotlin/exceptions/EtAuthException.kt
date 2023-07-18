package com.ashishbhoi.expensetrackerkotlin.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.UNAUTHORIZED)
class EtAuthException(message: String?) : RuntimeException(message)
