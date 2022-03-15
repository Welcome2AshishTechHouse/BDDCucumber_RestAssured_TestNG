package StepDefinationFiles;

import AppBase.baseClass;
import CollectionAPIs.forclosureAPIInterface;
import CollectionAPIs.loanHttpMethods;
import CollectionPayloads.loanPayload;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.testng.Assert;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import static org.hamcrest.Matchers.equalTo;

public class MyStepdefs extends baseClass {
    protected Response response;
    public baseClass baseObj;
    private String loan_status="";
    private float emi= 0.0F;
    private String reqObj;

    @Given("resources and End point available for Get loan details in Env {string} and domain {string}")
    public void resourcesAndEndPointAvailable( String env, String portal) {
        baseObj=new baseClass();
        setbaseURL(env, portal);
    }

    @When("get the loan details with Master User ID {string} and loan ID {string}")
    public void createTheRequestAndExecuteTheRequest(String MasterUserID, String LoanID) // Will pass the test data from scenarios
    {
        int Loan_ID=Integer.parseInt(LoanID);
       response = loanHttpMethods.getLoanDetails("zJhNuEnr7cdX6N0X", MasterUserID, Loan_ID);
    }


    @Then("validate the status code 200, loan status open")
    public void validateTheStatusCodeLoanStatusOpen() {
        verifyStatusCode_OK(response);

    }

    @And("Save the loan emi amount and loan amount availed")
    public void saveTheLoanEmiAmountAndLoanAmountAvailed() {
        emi  = jsonpath(response).getFloat("loan_details[0].emi");
        loan_status = jsonpath(response).get("loan_details[0].loan_status");
        Assert.assertEquals(loan_status,"open");
        System.out.println("Loan status is open state");
    }


    @Given("Payment details available to create the payment using Loan ID {string}")
    public void paymentDetailsAvailableToCreateThePaymentUsingLoanID(String loan,DataTable table) {
        int loan_id = Integer.parseInt(loan);
        List<List<String>> lobj= table.asLists();
        String payment_mode =  lobj.get(0).get(0);
        String payment_type=lobj.get(0).get(1);
        int amount = Integer.parseInt(lobj.get(0).get(2));
        String reference_number = generateRandomNumber("AutoTest");
        reqObj = loanPayload.postPaymentBody(reference_number,amount,payment_mode,loan_id,payment_type);
    }

    @When("create payment using loan payment api")
    public void createPaymentUsingLoanPaymentApi() {
        response = loanHttpMethods.postLoanPayment(reqObj,"zJhNuEnr7cdX6N0X");
    }

    @Then("validate the output response status code")
    public void validateTheOutputResponseStatusCode() {

        verifyStatusCode_OK(response);
    }

    @And("validate the payment create in payment db {string} same loan ID with expected details")
    public void validateThePaymnetCreateInPaymentDbUsingLoanIDWithExpectedDetails(String db_name,DataTable table) {
        List<List<String>> lobj= table.asLists();
        String query =  lobj.get(0).get(0);
        String status=lobj.get(0).get(1);
        int amount = Integer.parseInt(lobj.get(0).get(2));
        ResultSet result = executeQuery(query,db_name);
        try {
            while (result.next()) {
                String paymentStatusInDb = result.getString("status");
                float nDB=result.getFloat("amount");
                int paymentInDB=Math.round(nDB);
                Assert.assertEquals(paymentStatusInDb,status);
                Assert.assertEquals(paymentInDB,amount);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

}
