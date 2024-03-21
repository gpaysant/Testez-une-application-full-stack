describe('Logout spec', () => {
  beforeEach(() => {
    cy.login();
  });
    it('Logout successfull', () => {
      cy.get('span[class=link]').contains("Logout").click()
      cy.url().should('include', '/')
    })

  });