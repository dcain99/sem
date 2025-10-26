# USE CASE 1: Produce Report on Salary of All Employees

### Goal in Context
As an HR advisor, I want to produce a report on the salary of all employees so that I can support the financial reporting of the organization.

### Scope

Company.

### Level

Primary task.

### Primary Actor

HR Advisor.

### Trigger
A request for finance information is sent to HR for total company salary data.

### Preconditions
Database contains current employee salary data.

## MAIN SUCCESS SCENARIO
A request from Finance is received for the total company salary report.

The HR advisor opens the HR system's reporting dashboard.

The HR advisor selects the report titled "All Employee Salaries" (or similar).

The HR advisor generates the list of all active employees and their salaries.

The HR advisor exports the report and forwards it to Finance.

## EXTENSIONS
5. **System error occurs**:

The system does not generate the report because of a software bug.

The system shows an error message due to the bug.

The HR advisor reports the bug to IT for support.

## SUB-VARIATIONS
None.


### Success End Condition
A report is available for HR to provide to finance.

### Failed End Condition
No report is produced.

## SCHEDULE
**DUE DATE**: Release 1.0
