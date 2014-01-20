# Expense Tracker Web Application

Expense Tracker is a web application that allows a group of people to manage their expenses. 

## What does it do?

Consider a group of room-mates who live together and share common expenses. Such a group can manage expenses and obtain detailed reports on how much individual expenses amount to. You get to know easily how much you owe to others or how much others owe you. You get to specify if a particular expense is to be broken down equally or some specific people need to share that expense. You could override individual shares as well. 

## Running Expense Tracker locally
```
	git clone https://github.com/arunkpatra/expense-tracker.git
	cd expense-tracker
	mvn tomcat7:run
```

## Working with Expense Tracker in Eclipse/STS

### prerequisites
The following items should be installed in your system:
* Maven 3 (http://www.sonatype.com/books/mvnref-book/reference/installation.html)
* git command line tool (https://help.github.com/articles/set-up-git)
* Eclipse with the m2e plugin (m2e is installed by default when using the STS (http://www.springsource.org/sts) distribution of Eclipse)

Note: when m2e is available, there is an m2 icon in Help -> About dialog.
If m2e is not there, just follow the install process here: http://eclipse.org/m2e/download/


### Steps:

1) In the command line
```
git clone https://github.com/arunkpatra/expense-tracker.git
```
2) Inside Eclipse
```
File -> Import -> Maven -> Existing Maven project
```

[![Build Status](https://travis-ci.org/arunkpatra/expense-tracker.png?branch=master)](https://travis-ci.org/arunkpatra/expense-tracker)
[![Coverage Status](https://coveralls.io/repos/arunkpatra/expense-tracker/badge.png)](https://coveralls.io/r/arunkpatra/expense-tracker)

