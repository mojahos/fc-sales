
#  "Foreign Currency Sales" Program

_Mohammed J. Hossain_

This was a class project in which I create a program which takes the records of:
any sale made with many currencies, in any date.

### Description:

+ The currency is selected by using ISO-3 country codes. The currencies are pulled off of "exchangeratesapi.io" API. See [this](https://unstats.un.org/unsd/tradekb/knowledgebase/country-code) for country codes.
+ The price any value greater than 0.
+ The date when the sale took place.
+ 2 date range fields on the top to display only sales taken place within a specific date range.
+ Whenever a record is added, the total sum of sales is calculated in US Dollars in the bottom right corner.
+ A customized JList displays all records


### How to Run:
_(Requires Java 10 or later)_
```
javac CurrencySalesApp.java CurrencyUtil.java Sale.java
java CurrencySalesApp
```

# Example Input:
```
POL | 3200.50 | 9/9/2017
IND | 8612.9 | 9/9/2019
```