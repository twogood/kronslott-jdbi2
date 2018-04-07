package se.activout.kronslott.jdbi2

import com.fasterxml.jackson.annotation.JsonProperty
import dagger.Component
import dagger.Module
import dagger.Provides
import io.dropwizard.Application
import io.dropwizard.Configuration
import io.dropwizard.ConfiguredBundle
import io.dropwizard.db.DataSourceFactory
import io.dropwizard.setup.Bootstrap
import io.dropwizard.setup.Environment
import io.dropwizard.testing.ResourceHelpers
import io.dropwizard.testing.junit.DropwizardAppRule
import org.junit.ClassRule
import org.junit.Test
import org.skife.jdbi.v2.DBI
import org.skife.jdbi.v2.sqlobject.SqlQuery
import javax.inject.Singleton
import javax.validation.Valid

interface TestDao {
    @SqlQuery("SELECT true FROM dual")
    fun test(): Boolean
}

@Module
class TestModule(private val configuration: TestConfiguration, private val environment: Environment) {
    @Provides
    fun provideEnvironment(): Environment = environment

    @Provides
    @Singleton
    fun provideDataSourceFactory(): DataSourceFactory = configuration.database

    @Provides
    fun provideTestDao(dbi: DBI): TestDao = dbi.onDemand()
}

@Singleton
@Component(modules = [TestModule::class, Jdbi2Module::class])
interface TestComponent {
    fun getTestDao(): TestDao
}

class TestConfiguration : Configuration() {
    @Valid
    @JsonProperty("database")
    var database = DataSourceFactory()
}

class TestApp : Application<TestConfiguration>() {
    private lateinit var component: TestComponent

    override fun initialize(bootstrap: Bootstrap<TestConfiguration>) {
        bootstrap.addBundle(object : ConfiguredBundle<TestConfiguration> {

            override fun run(configuration: TestConfiguration, environment: Environment) {
                component = DaggerTestComponent.builder()
                        .testModule(TestModule(configuration, environment))
                        .build()
            }

            override fun initialize(bootstrap: Bootstrap<*>) {}
        })
    }

    override fun run(configuration: TestConfiguration, environment: Environment) {
        check(component.getTestDao().test())
    }
}

class IntegrationTest {
    companion object {
        @JvmField
        @ClassRule
        val rule = DropwizardAppRule<TestConfiguration>(TestApp::class.java, ResourceHelpers.resourceFilePath("IntegrationTest.yml"))
    }

    @Test
    fun startTestApplication() {
    }

}