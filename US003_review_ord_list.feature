<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=utf-8">
  <meta http-equiv="Content-Style-Type" content="text/css">
  <title></title>
  <meta name="Generator" content="Cocoa HTML Writer">
  <meta name="CocoaVersion" content="2299.4">
  <style type="text/css">
    p.p1 {margin: 0.0px 0.0px 12.0px 0.0px; font: 12.0px Times; -webkit-text-stroke: #000000}
    span.s1 {font-kerning: none}
  </style>
</head>
<body>
<p class="p1"><span class="s1">Feature: Review Order List</span></p>
<p class="p1"><span class="s1">User Story: As an Employee,</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space"> </span>I want to see a list of all orders and their status<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1">so that I can ensure their completion.</span></p>
<p class="p1"><span class="s1">Scenario Outline: View all orders. (Normal flow)<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>Given I am signed in as an Employee</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>When I navigate to the orders page<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>Then I see a list of all orders with their status.</span></p>
<p class="p1"><span class="s1">Scenario Outline: Filter orders by status. (Normal flow)</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>Given I am signed in as an Employee</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>When I filter orders by a specific status<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">            </span>Then I only see orders that match the selected status.</span></p>
<p class="p1"><span class="s1">Scenario Outline: Search for an order. (Normal flow)<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Given I am signed in as an Employee<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>When I search for an order using Order ID or Customer Name<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Then I see the relevant order in the list.</span></p>
<p class="p1"><span class="s1">Scenario Outline: View order details. (Normal flow)<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Given I am signed in as an Employee<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>When I click on an order<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Then I see detailed information about the order including items, payment status, and shipping information.</span></p>
<p class="p1"><span class="s1">Scenario Outline: Mark an order as completed. (Normal flow)<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Given I am signed in as an Employee</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>When I mark an order as completed<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">             </span>Then the order status is updated to "Completed."<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1">Scenario Outline: Access restriction. (Error flow)</span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">              </span>Given I am not signed in as an Employee<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">              </span>When I try to access the orders page<span class="Apple-converted-space"> </span></span></p>
<p class="p1"><span class="s1"><span class="Apple-converted-space">              </span>Then I receive an error</span></p>
</body>
</html>
