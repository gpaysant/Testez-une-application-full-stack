describe('Page not found tests', () => {
    it('sould display error page when url is incorrect', () => {
        cy.visit('/wrongurl')
        cy.contains('Page not found !')
        cy.url().should('eq', 'http://localhost:4200/404')

    });
});