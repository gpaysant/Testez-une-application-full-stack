/**
* @jest-environment jsdom
*/

import { HttpClient, HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed, fakeAsync } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBarModule, MatSnackBar } from '@angular/material/snack-bar';
import { SessionService } from 'src/app/services/session.service';

import { MeComponent } from './me.component';
import { User } from '../../interfaces/user.interface';
import { of } from 'rxjs';
import { UserService } from 'src/app/services/user.service';

import { Router } from '@angular/router';

import { getByTestId } from '@testing-library/dom'
import { By } from '@angular/platform-browser';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let userService: UserService;
  let sessionService: SessionService;
  let matSnackBar: MatSnackBar;
  let router: Router;

  const mockSessionService = {
    sessionInformation: {
      admin: true,
      id: 1
    },
    logOut : ()=> {}
  }
  const mockUser: User = {
    id: 1,
    email: 'toto@gmail.com',
    lastName: 'totoa',
    firstName: 'peter',
    admin: true,
    password: 'Password',
    createdAt: new Date()
  };
  const userServiceMock = {
    getById : () =>
      of(mockUser)
    ,
    delete : () => 
      of(mockUser)
  };
  const matSnackBarMock = {
    open : () => {}    
  };
  const routerMock = {
    navigate : () => {}   
  };

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: //[{ provide: SessionService, useValue: mockSessionService }]
      [
        { provide: UserService, useValue: userServiceMock },
        { provide: SessionService, useValue: mockSessionService },
        { provide: MatSnackBar, useValue: matSnackBarMock },
        { provide: Router, useValue: routerMock }
      ]
    })
      .compileComponents();


    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    userService = TestBed.inject(UserService);
    sessionService = TestBed.inject(SessionService);
    matSnackBar = TestBed.inject(MatSnackBar);
    router = TestBed.inject(Router);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it("should call ngOnInit and return user", () => {
    component.ngOnInit;
    expect(component.user).toEqual(mockUser);
  });


  it('should display User Informations for admin', () => {
    expect(getByTestId(document.body, "name").textContent).toEqual("Name: " + mockUser.firstName + " " + mockUser.lastName.toUpperCase());
    expect(getByTestId(document.body, "email").textContent).toEqual("Email: " + mockUser.email);
    expect(getByTestId(document.body, "adminText").textContent).toEqual("You are admin");
   
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="deleteButton"]'));
    expect(deleteButton).toBeFalsy();
    
  })

  it('should display User Informations for not admin', () => {
    const mockUser: User = {
      id: 1,
      email: 'john.doe@example.com',
      lastName: 'Doe',
      firstName: 'John',
      admin: false,
      password: 'password',
      createdAt: new Date()
    };
    
    component.user = mockUser;
    fixture.detectChanges();
    expect(getByTestId(document.body, "name").textContent).toEqual("Name: " + mockUser.firstName + " " + mockUser.lastName.toUpperCase());
    expect(getByTestId(document.body, "email").textContent).toEqual("Email: " + mockUser.email);
    const deleteButton = fixture.debugElement.query(By.css('[data-testid="deleteButton"]'));
    expect(deleteButton).toBeTruthy();
    
  })

  it('should call window.history.back() when back() is called', () => {
    const mockBack = jest.spyOn(window.history, 'back');
    component.back();
    expect(mockBack).toHaveBeenCalled();
  });

  it('should delete user and logout', () => {
    const mockMatSnack = jest.spyOn(matSnackBar, 'open');
    const mockLogout = jest.spyOn(sessionService, 'logOut');
    const mockRouter = jest.spyOn(router, 'navigate');

    component.delete();
    expect(mockMatSnack).toHaveBeenCalled();
    expect(mockLogout).toHaveBeenCalled();
    expect(mockRouter).toHaveBeenCalledWith(['/']);
  });

});
