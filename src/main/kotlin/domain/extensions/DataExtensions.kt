package com.br.domain.extensions

import kotlinx.datetime.Instant
import kotlinx.datetime.toJavaInstant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

private const val DATE_FORMAT_TIME = "dd/MM/yyyy HH:mm"

fun Instant.formatInstantForBrazilian() : String =
    DateTimeFormatter
        .ofPattern(DATE_FORMAT_TIME)
        .withZone(ZoneId.of("America/Sao_Paulo"))
        .format(this.toJavaInstant())