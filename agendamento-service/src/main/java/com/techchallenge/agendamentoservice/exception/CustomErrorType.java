package com.techchallenge.agendamentoservice.exception;

import graphql.ErrorClassification;

public enum CustomErrorType implements ErrorClassification {
    BAD_REQUEST,
    VALIDATION_ERROR,
    NOT_FOUND,
    INTERNAL_ERROR
}
