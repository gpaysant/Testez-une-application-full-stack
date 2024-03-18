describe('Register spec', () => {
      it('Register successfull', () => {
        cy.visit('/register')
    
        cy.intercept('POST', '/api/auth/register', {
          body: {
            message: "User registered successfully!"
          },
        })

        cy.get('input[formControlName=firstName]').type("john")
        cy.get('input[formControlName=lastName]').type("doe")
        cy.get('input[formControlName=email]').type("johndoe@gmail.com")
        cy.get('input[formControlName=password]').type("Password1234")

        cy.get('button[type=submit]').click();
    
        cy.url().should('include', '/login')
      })
    });