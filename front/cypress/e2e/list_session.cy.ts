describe('list session spec', () => {
    beforeEach(() => {
          cy.intercept(
            {
              method: 'GET',
              url: '/api/session',
            },
            []).as('session')

            
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

      });

    it('list session admin successfull', () => {

    cy.intercept('POST', '/api/auth/login', {
        body: {
            id: 1,
            username: 'userName',
            firstName: 'firstName',
            lastName: 'lastName',
            admin: true
        },
        })


      cy.visit('/login')
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')
      
      cy.get('mat-card .item')
        .should('have.length', 2)
        .each(($session) => {
            cy.wrap($session).find('mat-card-title').should('be.visible');
            cy.wrap($session).find('mat-card-subtitle').should('be.visible');
            cy.wrap($session).find('[alt="Yoga session"]').should('be.visible');
            cy.wrap($session).find('mat-card-content p').should('be.visible');
            cy.wrap($session).find('span[class=ml1]').contains("Detail")
            cy.wrap($session).find('span[class=ml1]').contains("Edit")
      })

    })

    it('list session with user successfull', () => {

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName',
                firstName: 'firstName',
                lastName: 'lastName',
                admin: false
            },
            })
    
    
          cy.visit('/login')
      
          cy.get('input[formControlName=email]').type("yoga@studio.com")
          cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
      
          cy.url().should('include', '/sessions')
          
          cy.get('mat-card .item')
            .should('have.length', 2)
            .each(($session) => {
                cy.wrap($session).find('mat-card-title').should('be.visible');
                cy.wrap($session).find('mat-card-subtitle').should('be.visible');
                cy.wrap($session).find('[alt="Yoga session"]').should('be.visible');
                cy.wrap($session).find('mat-card-content p').should('be.visible');
                cy.wrap($session).find('span[class=ml1]').contains("Detail")
                cy.wrap($session).find('span[class=ml1]').should('not.contain', 'Edit')
          })
    
        })
  });