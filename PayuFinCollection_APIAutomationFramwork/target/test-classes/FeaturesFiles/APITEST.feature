Feature: PayuFin Automation POC

  Scenario: Get the loan details
    Given resources and End point available for Get loan details in Env "sbox" and domain "paysense"
    When get the loan details with Master User ID "10108488" and loan ID "2028827"
    Then validate the status code 200, loan status open
    And Save the loan emi amount and loan amount availed

  Scenario: Create Payment using Loan ID
    Given Payment details available to create the payment using Loan ID "2028827"
      | upi | emi | 13 |
    When create payment using loan payment api
    Then validate the output response status code
    And  validate the payment create in payment db "payments_sandbox" same loan ID with expected details
      | select * from core_payment cp where loan_id  = '2028827' limit 1 | processing | 13 |
