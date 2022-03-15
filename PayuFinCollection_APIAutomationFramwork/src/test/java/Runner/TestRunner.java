package Runner;


import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;


@CucumberOptions(
        features = {"src/test/resources/FeaturesFiles/APITEST.feature"},
        glue = {"StepDefinationFiles"},
        plugin = {"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
        dryRun = false,
        publish = true
)
public class TestRunner extends AbstractTestNGCucumberTests {

}
