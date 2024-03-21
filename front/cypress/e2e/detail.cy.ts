describe('Detail tests', () => {

    it('should display the details of a session for user', () => {

        // User login

        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 2,
                username: 'userName2',
                firstName: 'firstName2',
                lastName: 'lastName2',
                admin: false
            },
        })

        // Display of sessions 

        const teacher1 = {
            id: 1,
            lastName: "teacher1LastName",
            firstName: "teacher1FirstName",
            createdAt: new Date(),
            updatedAt: new Date()
        }

        const session1 = {
            id: 1,
            name: "session1",
            date: "2024-04-05T08:00:00.000+00:00",
            teacher_id: 1,
            description: "session 1",
            users: [],
            createdAt: "2024-03-15T08:23:02",
            updatedAt: "2024-03-15T08:23:02"
        }

        const session2 = {
            id: 2,
            name: "session2",
            date: "2024-04-05T08:00:00.000+00:00",
            teacher_id: 1,
            description: "session 2",
            users: [],
            createdAt: "2024-03-15T08:23:02",
            updatedAt: "2024-03-15T08:23:02"
        }

        const sessions = [session1, session2]

        cy.intercept('GET', '/api/session', {
            body: sessions
        }).as("getsession")

        cy.get('input[formControlName=email]').type("user1@email.com")
        cy.get('input[formControlName=password]').type(`${"password"}{enter}{enter}`)

        // Display of the first session (click on "Detail")

        cy.intercept('GET', '/api/session/1', {
            body: session1
        })

        cy.intercept('GET', '/api/teacher/1', {
            body: teacher1
        })
    
        cy.contains('Detail').click()

        // Verify 

        cy.url().should('include', 'sessions/detail/1')
        cy.contains('attendees')
        cy.contains('Session1')
        cy.contains('Delete').should('not.exist')
        cy.contains('Participate').should('be.visible')

    });

    it('should display the details of a session for admin', () => {

        // Admin login

        cy.visit('/login')

        cy.intercept('POST', '/api/auth/login', {
            body: {
                id: 1,
                username: 'userName2',
                firstName: 'firstName2',
                lastName: 'lastName2',
                admin: true
            },
        })

        // Display of sessions 

        const teacher1 = {
            id: 1,
            lastName: "teacher1LastName",
            firstName: "teacher2FirstName",
            createdAt: new Date(),
            updatedAt: new Date()
        }

        const session1 = {
            id: 1,
            name: "session1",
            date: "2024-04-05T08:00:00.000+00:00",
            teacher_id: 1,
            description: "session 1",
            users: [],
            createdAt: "2024-03-15T08:23:02",
            updatedAt: "2024-03-15T08:23:02"
        }

        const session2 = {
            id: 2,
            name: "session2",
            date: "2024-04-05T08:00:00.000+00:00",
            teacher_id: 1,
            description: "session 2",
            users: [],
            createdAt: "2024-03-15T08:23:02",
            updatedAt: "2024-03-15T08:23:02"
        }

        const sessions = [session1, session2]

        cy.intercept('GET', '/api/session', {
            body: sessions
        })

        cy.get('input[formControlName=email]').type("yoga@studio.com")
        cy.get('input[formControlName=password]').type(`${"test!1234"}{enter}{enter}`)

        // Click on "Detail"

        cy.intercept('GET', '/session/detail/1', {
            body: session1
        })

        cy.intercept('GET', '/api/session/1', session1)
        cy.intercept('GET', '/api/teacher/1', teacher1)

        cy.contains('Detail').click()

        // Verify 

        cy.url().should('include', 'sessions/detail')
        cy.contains('attendees')
        cy.contains('Session1')
        cy.contains('Delete').should('be.visible')
        cy.contains('Participate').should('not.exist')
        cy.contains('Do not participate').should('not.exist')

    });

});