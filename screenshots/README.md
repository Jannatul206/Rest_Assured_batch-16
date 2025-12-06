# Allure Report Screenshots

## Instructions
After running your tests and generating the Allure report, take screenshots and place them in this directory:

1. **allure-overview.png** - Screenshot of the Allure report overview page showing test execution summary
2. **allure-suites.png** - Screenshot of test suites page showing detailed test cases
3. **allure-behaviors.png** - Screenshot of behaviors page showing tests organized by features

### How to Generate Allure Report:
```bash
mvn clean test
allure serve target/allure-results
```

### How to Take Screenshots:
1. Run the above commands
2. When the Allure report opens in your browser
3. Take screenshots of the overview, suites, and behaviors pages
4. Save them in this directory with the names mentioned above
5. Update the README.md to reflect the actual screenshots

---

### Expected Screenshots:

**1. Allure Overview**
- Shows total tests count
- Pass/Fail ratio
- Execution time
- Test status distribution

**2. Allure Suites**
- List of all test classes
- Test methods within each class
- Individual test status
- Execution details

**3. Allure Behaviors**
- Tests organized by Epic
- Tests organized by Feature
- Tests organized by Story
- Severity levels
