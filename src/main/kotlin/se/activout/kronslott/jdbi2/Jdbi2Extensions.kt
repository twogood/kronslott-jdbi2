package se.activout.kronslott.jdbi2

import org.skife.jdbi.v2.DBI

inline fun <reified T> DBI.onDemand(): T = this.onDemand(T::class.java)
