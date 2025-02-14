Feature: Review Order List

As an Employee,
I want to see a list of all orders and their status 
so that I can ensure their completion.


Scenario Outline: View all orders. (Normal flow) 
            Given I am signed in as an Employee
            When I navigate to the orders page 
            Then I see a list of all orders with their status.
Scenario Outline: Filter orders by status. (Normal flow)
            Given I am signed in as an Employee
            When I filter orders by a specific status 
            Then I only see orders that match the selected status.
Scenario Outline: Search for an order. (Normal flow) 
             Given I am signed in as an Employee 
             When I search for an order using Order ID or Customer Name 
             Then I see the relevant order in the list.
Scenario Outline: View order details. (Normal flow) 
             Given I am signed in as an Employee 
             When I click on an order 
             Then I see detailed information about the order including items, payment status, and shipping information.
Scenario Outline: Mark an order as completed. (Normal flow) 
             Given I am signed in as an Employee
             When I mark an order as completed 
             Then the order status is updated to "Completed." 
Scenario Outline: Access restriction. (Error flow)
              Given I am not signed in as an Employee 
              When I try to access the orders page 
              Then I receive an error
