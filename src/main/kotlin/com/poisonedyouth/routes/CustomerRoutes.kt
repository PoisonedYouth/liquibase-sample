package com.poisonedyouth.routes

import com.poisonedyouth.Customer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route

fun Route.customerRouting() {
    route("/customer") {
        get {
            call.respond(Customer.getAllCustomer())
        }
        get("{id?}") {
            // TODO
        }
        post {
            val customer = call.receive<Customer>()
            Customer.saveCustomer(customer)
            call.respondText("Customer stored correctly", status = HttpStatusCode.Created)
        }
        delete("{id?}") {
            // TODO
        }
    }
}