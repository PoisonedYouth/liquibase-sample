package com.poisonedyouth

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

private object CustomerTable : LongIdTable(name = "customer", "id") {
    val firstName: Column<String> = varchar("firstname", 255)
    val lastName: Column<String> = varchar("lastname", 255)
    val age: Column<Int> = integer(("age"))
    val city: Column<String> = varchar("city", 255)
}

@kotlinx.serialization.Serializable
data class Customer(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val city: String
) {

    companion object {
        fun saveCustomer(customer: Customer) {
            transaction {
                CustomerTable.insert {
                    it[firstName] = customer.firstName
                    it[lastName] = customer.lastName
                    it[age] = customer.age
                    it[city] = customer.city
                }
            }
        }

        fun getAllCustomer() = transaction {
            CustomerTable.selectAll().map {
                mapRowToCustomer(it)
            }
        }

        private fun mapRowToCustomer(it: ResultRow) = Customer(
            id = it[CustomerTable.id].value,
            firstName = it[CustomerTable.firstName],
            lastName = it[CustomerTable.lastName],
            age = it[CustomerTable.age],
            city = it[CustomerTable.city]
        )
    }
}

