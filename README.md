# aplikacja_dyzury

## Purpose of this app
This is a rostering app. It enables doctors to plan new duties as well as join duties planned by other doctors (max. 8 doctors per duty). 
Users can define where (name of the hospital and name of the department) and when the duty takes place. 
This app also enables users to switch duties between one another.
Another functionality is 

The app was a university project and was submitted in February 2020.

## Used technologies
* Java 1.8,
* Vaadin 14,
* Spring Boot,
* Spring Security,
* PostgreSQL database.

## Live demo
Link to live demo is available below:

[https://aplikacja-dyzury-postgres.herokuapp.com/](https://aplikacja-dyzury-postgres.herokuapp.com/)

Please be aware that the  page may take up to 30 seconds to initially load. This is due to free account limitations on Heroku - unused apps are hibernated and only resumed when someone is trying to use them. After that initial waiting period, the app will work normally.

## Implementation
The app was implemented using technologies mentioned in **Used technologies** section. 

Login form was secured with Spring Security.
Forms requiring user input were validated using [Vaadin Binder](https://vaadin.com/docs/v14/flow/binding-data/tutorial-flow-components-binder) in order to prevent invalid or incomplete data.
All the tables were paginated using Pageable interface from Spring Data JPA. 










