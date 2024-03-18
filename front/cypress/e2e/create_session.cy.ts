describe('create session spec', () => {
    beforeEach(() => {
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
    
          cy.intercept('GET', '/api/teacher', {
            statusCode: 200,
            body: [{
                id: 1,
                lastName: "doe",
                firstName: "john",
                createdAt: '04/03/2024',
                updatedAt: '05/03/2024'
            },
            {
                id: 2,
                lastName: "dae",
                firstName: "jane",
                createdAt: '06/03/2024',
                updatedAt: '07/03/2024'
            }]
            ,
          })
          .as('teacherRetrieved')
      
          cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []).as('session')
      });

    it('me successfull', () => {
      cy.visit('/login')
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')

      cy.get('span[routerLink=sessions]').click()
      cy.get('button[routerLink=create]').click()

      cy.url().should('include', '/sessions/create')

      cy.get('input[formControlName=name]').type("first session")
      cy.get('input[formControlName=date]').type("2022-06-10")

      cy.wait('@teacherRetrieved')
      cy.get('mat-select[formControlName=teacher_id]').click()
      cy.contains('.mat-option-text', 'john doe').click()
      cy.get('textarea[formControlName=description]').type("Learn basics")

      cy.intercept('POST', '/api/session', {
        statusCode: 200,
        body: {
            id: 1,
            name: "first session",
            description: "Learn basics",
            date: new Date(),
            teacher_id: 1,
            users: [2, 3],
            createdAt: '06/03/2024',
            updatedAt: '06/03/2024',
        },
      })
      .as('userRetrieved')

      cy.get('button[type=submit]').click()

      cy.url().should('include', '/sessions')
    })
  });