package se.activout.kronslott.jdbi2

import co.wrisk.jdbi.KotlinPlugin
import dagger.Module
import dagger.Provides
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.jdbi.DBIFactory
import io.dropwizard.setup.Environment
import org.skife.jdbi.v2.DBI
import javax.inject.Singleton


@Module
class Jdbi2Module() {

    @Singleton
    @Provides
    fun provideDBIFactory(): DBIFactory {
        return DBIFactory()
    }

    @Singleton
    @Provides
    fun provideDBI(factory: DBIFactory, dataSourceFactory: DataSourceFactory, environment: Environment): DBI {
        val dbi = factory.build(environment, dataSourceFactory, "kronslott-jdbi2")
        return KotlinPlugin().customizeDBI(dbi)
    }
}