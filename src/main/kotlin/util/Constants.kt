package com.br.util

object Constants {

    const val DATABASE_NAME = "DATABASE_NAME"
    const val MONGODB_URI_LOCAL = "MONGODB_URI_LOCAL"
    const val MONGODB_URI_REMOTE = "MONGODB_URI_REMOTE"
    const val SECRET = "SECRET"

    const val COST_FACTOR = 16
    const val DURATION_IN_SECONDS_QRCODE = 40

    const val PATH_ID = "/{id}"
    const val PATH_SEARCH = "/search"
    const val PARAM_ID = "id"
    const val PARAM_CATEGORY = "category"
    const val PARAM_NAME_OR_INGREDIENTS = "nameOrIngredient"

    const val CREATE_QR_CODE_ROUTE = "/create-qrcode"
    const val READ_QR_CODE_ROUTE = "/read-qrcode"

    private const val API_PREFIX = "/api/v1"
    const val USER_ROUTE = "${API_PREFIX}/users"
    const val RECIPE_ROUTE = "${API_PREFIX}/recipes"
    const val WEBSOCKETS_ROUTE = "${API_PREFIX}/addConnection"
    const val USERS_CONNECTIONS_ROUTE = "${API_PREFIX}/users-connections"

    const val COLLECTION_NAME_USERS = "users"
    const val COLLECTION_NAME_RECIPES = "recipes"
    const val COLLECTION_NAME_QR_CODES = "qrCodes"
    const val COLLECTION_NAME_USERS_CONNECTIONS = "usersConnections"

    const val TYPE_QRCODE_GENERATED = "TYPE_QRCODE_GENERATED"
    const val TYPE_USERS_CONNECTIONS = "TYPE_USERS_CONNECTIONS"
    const val TYPE_READ_QRCODE = "TYPE_READ_QRCODE"
    const val TYPE_CANCEL_CONNECTION = "TYPE_CANCEL_CONNECTION"
    const val TYPE_TIME_REMAINING = "TYPE_TIME_REMAINING"
    const val TYPE_QRCODE_EXPIRATION_MESSAGE = "TYPE_QRCODE_EXPIRATION_MESSAGE"
    const val TYPE_ERROR_MESSAGE = "TYPE_ERROR_MESSAGE"
    const val TYPE_AWAITING_CONNECTION = "TYPE_AWAITING_CONNECTION"
    const val TYPE_CONNECTION_STATUS = "TYPE_CONNECTION_STATUS"
    const val TYPE_SEND_QRCODE = "TYPE_SEND_QRCODE"
    const val TYPE_USER_ACTION = "TYPE_USER_ACTION"

    const val OPERATION_CANCELED_BY_USER_MESSAGE = "Operação cancelada pelo usuário."

    const val MONGODB_ID = "_id"

}