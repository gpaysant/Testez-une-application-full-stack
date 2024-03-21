describe('me spec', () => {
    it('me admin successfull', () => {
      cy.login()
  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
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
      cy.loginsimpleUser()



  
      cy.intercept(
        {
          method: 'GET',
          url: '/api/session',
        },
        []).as('session')
  
      cy.url().should('include', '/sessions')

      cy.get('span[routerLink=me]').click();

      cy.url().should('include', '/me')
      cy.wait('@userRetrieveds')

      cy.get('.ng-star-inserted').contains('Name: john DOE')
      cy.get('.ng-star-inserted').contains('Email: johndoe@gmail.com')
      cy.get('.ng-star-inserted').contains('You are admin').should('not.exist')

      cy.get('[data-testid=deleteButton]').click()
      cy.url().should('include', '/')
    })
  });

