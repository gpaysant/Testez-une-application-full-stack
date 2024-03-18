describe('me spec', () => {
    it('me admin successfull', () => {
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
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.get('input[formControlName=email]').type("yoga@studio.com")
      cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')

      cy.get('span[routerLink=me]').click();

      cy.url().should('include', '/me')
      cy.wait('@userRetrieved')

      cy.get('.ng-star-inserted').contains('Name: firstName LASTNAME')
      cy.get('.ng-star-inserted').contains('Email: email@ditou.com')
      cy.get('.ng-star-inserted').contains('You are admin')

      cy.get('[data-testid=back-button]').click()
      cy.url().should('include', '/sessions')
    })

    it('me user successfull', () => {
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
          id: 2,
          username: 'userName',
          firstName: 'john',
          lastName: 'doe',
          email: 'johndoe@gmail.com',
          admin: false,
          createdAt: '04/03/2024',
          updatedAt: '05/03/2024'
        },
      })
      .as('userRetrieved')

      cy.intercept('DELETE', `/api/user/${id}`, {
        body: {
          id: 1,
          username: 'userName',
          firstName: 'john',
          lastName: 'doe',
          admin: false
        },
      })
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.get('input[formControlName=email]').type("johndoe@gmail.com")
      cy.get('input[formControlName=password]').type(`${"anotherTest!1234"}{enter}{enter}`)
  
      cy.url().should('include', '/sessions')

      cy.get('span[routerLink=me]').click();

      cy.url().should('include', '/me')
      cy.wait('@userRetrieved')

      cy.get('.ng-star-inserted').contains('Name: john DOE')
      cy.get('.ng-star-inserted').contains('Email: johndoe@gmail.com')
      cy.get('.ng-star-inserted').contains('You are admin').should('not.exist')

      cy.get('[data-testid=delete-button]').click()
      cy.url().should('include', '/')
    })
  });

