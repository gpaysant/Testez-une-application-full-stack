/**
* @jest-environment jsdom
*/

import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';

import { AppComponent } from './app.component';

import { SessionService } from 'src/app/services/session.service';
import { of } from 'rxjs';
import { Router } from '@angular/router';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: ComponentFixture<AppComponent>;
  let sessionService: SessionService;
  let router: Router;
  const mockSessionService = {
    $isLogged: () =>
      of(true),
    logOut : () => {}
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      providers:
      [
        { provide: SessionService, useValue: mockSessionService }
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();
    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  it('should be logged', () => {
    const isLoggedMock = jest.spyOn(sessionService, "$isLogged");
    component.$isLogged();
    fixture.detectChanges();
    expect(isLoggedMock).toHaveBeenCalled();

    const loggedLink = document.querySelectorAll(".link");
    expect(loggedLink.item(0).textContent).toBe('Sessions');
    expect(loggedLink.item(1).textContent).toBe('Account');
    expect(loggedLink.item(2).textContent).toBe('Logout');
  });

  it('should be not logged', () => {
    const isLoggedMock = jest.spyOn(sessionService, "$isLogged").mockReturnValue(of(false));
    component.$isLogged();
    fixture.detectChanges();
    expect(isLoggedMock).toHaveBeenCalled();

    const loggedLink = document.querySelectorAll(".link");
    expect(loggedLink.item(0).textContent).toBe('Login');
    expect(loggedLink.item(1).textContent).toBe('Register');
  });

  it('should be logout', () => {
    const mockLogout = jest.spyOn(sessionService, 'logOut');
    const mockRouter = jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));
    component.logout();
    expect(mockLogout).toHaveBeenCalled();
    expect(mockRouter).toHaveBeenCalledWith(['']);
  });

});
