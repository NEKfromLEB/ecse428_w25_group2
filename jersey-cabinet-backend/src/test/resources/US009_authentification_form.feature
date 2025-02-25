Feature: Submit Jersey Authenticity form
As a manager,  
I want clients to submit a form to confirm the authenticity of their jersey,  
So that I can verify its legitimacy before purchasing it from them.  

  Background: 
    Given the store has an online platform for authenticity verification  

  Scenario: Successfully Submit a Jersey Authentication Form  
    When the client submits a jersey authentication form with brand "<brand>", sport "<sport>", description "<description>",color "<color>", and proof of authenticity "<proof>"    
    Then the jersey with brand "<brand>", sport "<sport>", description "<description>", color "<color>", and proof of authenticity "<proof>" shall exist in the system  
    Examples: 
      | brand        | sport       | description               | color          | proof                       | 
      | Nike         | Soccer      | 2019 Manchester City      | Light blue     | Receipt & Photos            | 
      | Adidas       | Basketball  | 2016 Cleveland Cavaliers  | Black and red  | None                        |  
      | Coq Sportif  | Rubby       | 2024 France National Team | Dark blue      | Receipt & Photos            | 
      | Kappa        | Soccer      | 2023 Fiorentina           | Purple         | Manufacturer Cert.          | 
      | Nike         | Baseball    | 2018 NY Yankees           | White and blue | Certificate of authenticity |  

  Scenario Outline: Submit Form Without Proof of Authenticity (Alternative Flow)  
    When the client submits a jersey authentication form with brand "<brand>", sport "<sport>", description "<description>", color "<color>"  
    Then the system warns: "Proof of authenticity is missing. Submitting without proof may result in a longer verification process or rejection."  
    And the client can proceed with submission  
    And the store owner is notified of a submission requiring manual verification  

  Examples:  
      | brand  | sport       | description  | color         | proof |  
      | Puma   | Soccer      | 2017 Arsenal | Red and white |       |   
      | Nike   | Football    | 2022 Patriots| Blue          |       | 
      | Adidas | Hockey      | 2014 Habs    | Red and blue  |       |   
    
  Scenario: Error - Incomplete Form Submission (Error flow) 
    When client submits a jersey authentication form with brand "<brand>", sport "<sport>", description "<description>", color "<color>", and proof of authenticity "<proof>"  
    Then the system prevents submission if any of those fields are empty 
    And an error message is displayed: "Please complete all required fields before submitting."  
    And the form remains on the page for correction  

  Examples:  
    | brand  | sport      | description      | color             | proof              |  
    | Nike   | Football   |                  | Green             | Receipt & Photos   |  
    | Puma   |            | 2006 Italy       | Blue              | Manufacturer Cert. |  
    |        | Basketball | 2013 Lakers      | Yellow and purple | Receipt & Photos   |  
