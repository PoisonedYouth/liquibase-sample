package com.poisonedyouth

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertAndGetId
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

private object CustomerTable : LongIdTable(name = "customer", "id") {
    val firstName: Column<String> = varchar("firstname", 255)
    val lastName: Column<String> = varchar("lastname", 255)
    val age: Column<Int> = integer(("age"))
    val address: Column<Long> = long("address_id").references(AddressTable.id)
}

@kotlinx.serialization.Serializable
data class Customer(
    val id: Long?,
    val firstName: String,
    val lastName: String,
    val age: Int,
    val address: Address
) {

    companion object {
        fun saveCustomer(customer: Customer) {
            transaction {
                CustomerTable.insert { customerInsert ->
                    customerInsert[firstName] = customer.firstName
                    customerInsert[lastName] = customer.lastName
                    customerInsert[age] = customer.age
                    customerInsert[address] = AddressTable.insertAndGetId { addressInsert ->
                        addressInsert[city] = customer.address.city
                        addressInsert[zipCode] = customer.address.zipCode
                        addressInsert[street] = customer.address.street
                        addressInsert[streetNumber] = customer.address.streetNumber
                    }.value
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
            address = AddressTable
                .select { AddressTable.id eq it[CustomerTable.address] }
                .map { Address.mapRowToAddress(it) }.first()
        )
    }

}

private object AddressTable : LongIdTable(name = "address", "id") {
    val city: Column<String> = varchar("city", 255)
    val zipCode: Column<String> = varchar("zip_code", 255)
    val street: Column<String> = varchar("street", 255)
    val streetNumber: Column<Int?> = integer("street_number").nullable()
    val country: Column<String> = varchar("country", 255)
}

@kotlinx.serialization.Serializable
data class Address(
    val id: Long?,
    val city: String,
    val zipCode: String,
    val street: String,
    val streetNumber: Int?,
    val country: String
) {

    companion object {
        fun mapRowToAddress(it: ResultRow) = Address(
            id = it[AddressTable.id].value,
            city = it[AddressTable.city],
            zipCode = it[AddressTable.zipCode],
            street = it[AddressTable.street],
            streetNumber = it[AddressTable.streetNumber],
            country = it[AddressTable.country]
        )
    }
}

