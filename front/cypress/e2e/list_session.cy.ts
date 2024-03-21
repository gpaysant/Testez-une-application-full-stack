describe('list session spec', () => {
    it('list session admin successfull', () => {

      cy.login()
  
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

      cy.loginsimpleUser()
      
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