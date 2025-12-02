There is a simple test for amazon.com (java+JUnit+Selenide+Allure).
searchTest:
1. open amazon.com
2. write product query in search field and click enter
3. find product in the list of products
ER: verify Title, Price, Brand are as expected

There are some tests to implement:
* Sign In / Sign Up
* Search returns relevant results
* Add to cart, Checkout verifies, Payments
* Correct parameters: Price, Description, Shipping, etc.
* Removing items from cart
* Currency / Language selection
* Performance: home page, search response, ...
* Verify top menu, links
* History
* No results page behaves correctly
* Review
* Wishlist
* Security
