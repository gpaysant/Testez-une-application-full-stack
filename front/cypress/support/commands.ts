// ***********************************************
// This example namespace declaration will help
// with Intellisense and code completion in your
// IDE or Text Editor.
// ***********************************************
// declare namespace Cypress {
//   interface Chainable<Subject = any> {
//     customCommand(param: any): typeof customCommand;
//   }
// }
//
// function customCommand(param: any): void {
//   console.warn(param);
// }
//
// NOTE: You can use it like so:
// Cypress.Commands.add('customCommand', customCommand);
//
// ***********************************************
// This example commands.js shows you how to
// create various custom commands and overwrite
// existing commands.
//
// For more comprehensive examples of custom
// commands please read more here:
// https://on.cypress.io/custom-commands
// ***********************************************
//
//
// -- This is a parent command --
// Cypress.Commands.add("login", (email, password) => { ... })
//
//
// -- This is a child command --
// Cypress.Commands.add("drag", { prevSubject: 'element'}, (subject, options) => { ... })
//
//
// -- This is a dual command --
// Cypress.Commands.add("dismiss", { prevSubject: 'optional'}, (subject, options) => { ... })
//
//
// -- This will overwrite an existing command --
// Cypress.Commands.overwrite("visit", (originalFn, url, options) => { ... })



Cypress.Commands.add("login", () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
        },
    })

    const id = '1';

    cy.intercept('GET', `/api/user/${id}`, {
        statusCode: 200,
        body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            email: 'email@ditou.com',
            admin: true,
            createdAt: '04/03/2024',
            updatedAt: '05/03/2024'
        },
    })
        .as('userRetrieved')

    cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [{
            id: 1,
            name: "first session",
            description: "Learn basics",
            date: '06/03/2024',
            teacher_id: 1,
            users: [2, 3],
            createdAt: '06/03/2024',
            updatedAt: '06/03/2024',
        },
        {
            id: 2,
            name: "Second session",
            description: "Learn basics again",
            date: '07/03/2024',
            teacher_id: 1,
            users: [2, 3],
            createdAt: '06/03/2024',
            updatedAt: '06/03/2024',
        }
        ],
    })
        .as('sessionRetrieved')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

});

Cypress.Commands.add("loginsimpleUser", () => {
    cy.visit('/login')

    cy.intercept('POST', '/api/auth/login', {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'john',
          lastName: 'doe',
          admin: false
        },
      })

      const id = '1';

    cy.intercept('GET', `/api/user/${id}`, {
        statusCode: 200,
        body: {
          id: 1,
          username: 'userName',
          firstName: 'john',
          lastName: 'doe',
          email: 'johndoe@gmail.com',
          admin: false,
          createdAt: '04/03/2024',
          updatedAt: '05/03/2024'
        },
      })
      .as('userRetrieveds')
      
      cy.intercept('DELETE', `/api/user/${id}`, {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'john',
          lastName: 'doe',
          admin: false
        },
      })

    cy.intercept('GET', '/api/session', {
        statusCode: 200,
        body: [{
            id: 1,
            name: "first session",
            description: "Learn basics",
            date: '06/03/2024',
            teacher_id: 1,
            users: [2, 3],
            createdAt: '06/03/2024',
            updatedAt: '06/03/2024',
        },
        {
            id: 2,
            name: "Second session",
            description: "Learn basics again",
            date: '07/03/2024',
            teacher_id: 1,
            users: [2, 3],
            createdAt: '06/03/2024',
            updatedAt: '06/03/2024',
        }
        ],
    })
        .as('sessionRetrieved')

    cy.get('input[formControlName=email]').type("yoga@studio.com")
    cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

});

declare namespace Cypress {
    interface Chainable {
        /**
         * Custom command to bypass the tutorial screens in the app
         */
        login(): Chainable<JQuery<HTMLElement>>;
        /**
         * Custom command to avoid bypassing the tutorial screens in the app
         */

        loginsimpleUser(): Chainable<JQuery<HTMLElement>>;
}
}