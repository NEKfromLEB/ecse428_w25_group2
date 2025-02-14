Feature: Submit Jersey Authenticity form
As a manager,  
I want clients to submit a form to confirm the authenticity of their jersey,  
So that I can verify its legitimacy before purchasing it from them.  

  Background: 
    Given the store has an online platform for authenticity verification  
    And a client wants to sell a jersey 

  Scenario: Successfully Submit a Jersey Authentication Form  
    When the client submits a jersey authentication form with brand "<brand>", club "<club>", serial number "<serialNumber>", year "<year>", and proof of authenticity "<proof>"  
    Then the number of pending authentication requests in the system shall be "<totalRequests>"  
    And the jersey with brand "<brand>", club "<club>", serial number "<serialNumber>", year "<year>", and proof of authenticity "<proof>" shall exist in the system  
    Examples: 
      | brand  | club      | serialNumber | year | proof              | totalRequests |  
      | Nike   | PSG Home  | ABC1234      | 2021 | Receipt & Photos   | 3             |  
      | Adidas | Bayern    | XYZ5678      | 2020 | None               | 4             |  
      | Puma   | Dortmund  | LMN7890      | 2019 | Receipt & Photos   | 2             |  
      | Kappa  | Napoli    | PQR3456      | 2022 | Manufacturer Cert. | 5             |  
      | Nike   | Barcelona | DEF0987      | 2018 | None               | 6             |  

  Scenario Outline: Submit Form Without Proof of Authenticity (Alternative Flow)  
    When the client submits a jersey authentication form with brand "<brand>", club "<club>", serial number "<serialNumber>", year "<year>", and proof of authenticity "<proof>"  
    Then the system warns: "Proof of authenticity is missing. Submitting without proof may result in a longer verification process or rejection."  
    And the client can proceed with submission  
    And the store owner is notified of a submission requiring manual verification  
    Examples:  
      | brand  | club        | serialNumber | year | proof | totalRequests |  
      | Nike   | Inter Milan | XYZ4567      | 2021 | None  | 7             |  
      | Puma   | Arsenal     | PQR9999      | 2020 | None  | 8             |  
      | Adidas | Juventus    | LMN3333      | 2022 | None  | 9             |  
    
  Scenario: Error - Incomplete Form Submission (Error flow) 
    When the client submits a jersey authentication form with brand "<brand>", club "<club>", serial number "<serialNumber>", year "<year>", and proof of authenticity "<proof>"  
    Then the system prevents submission if any of those fields are empty 
    And an error message is displayed: "Please complete all required fields before submitting."  
    And the form remains on the page for correction  

  Examples:  
    | brand  | club   | serialNumber | year | proof              |  
    | Nike   | PSG    |              | 2021 | Receipt & Photos   |  
    | Puma   |        | ABC1234      | 2020 | Manufacturer Cert. |  
    |        | Bayern | XYZ5678      | 2019 | None               |  
