package com.example.assignmentapplication.models


class UserInfoData(
    val name: String?,
    val mobile: String?,
    val age: String?,
    val email: String
) {
    constructor() : this("", "", "", "")
}
