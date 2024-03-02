import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';

import { LoginComponent } from './login.component';

import { AuthService } from '../../services/auth.service';
import { of, throwError } from 'rxjs';
import { Router } from '@angular/router';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { By } from '@angular/platform-browser';


describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: AuthService;
  let router: Router;
  let sessionService: SessionService;

  const sessionInformationMock: SessionInformation = {
    token: "12345",
    type: "new",
    id: 1,
    username: "jd",
    firstName: "john",
    lastName: "doe",
    admin: true
  }

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        { provide: SessionService }
        ],
      imports: [
        RouterTestingModule,
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    sessionService = TestBed.inject(SessionService);
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should successed on validation', () => {
    component.form.controls['email'].setValue("johndoe@gmail.com");
    component.form.controls['password'].setValue("1234567");
    expect(component.form.valid).toBeTruthy();
  });

  it('should failed on validation', () => {
    expect(component.form.valid).toBeFalsy();
    component.form.controls['email'].setValue("johndoegmail.com");
    component.form.controls['password'].setValue("1234567");
    expect(component.form.valid).toBeFalsy();
    component.form.controls['email'].setValue("johndoe@gmail.com");
    component.form.controls['password'].setValue("1");
    expect(component.form.valid).toBeFalsy();
  });

  it('should successed on submit', () => {
    const mockLogin = jest.spyOn(authService, 'login').mockReturnValue(of(sessionInformationMock));
    const mockSessionService =  jest.spyOn(sessionService, 'logIn');
    const mockRouter = jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true));
    
    component.submit();

    expect(mockLogin).toHaveBeenCalled();
    expect(mockSessionService).toHaveBeenCalledWith(sessionInformationMock);
    expect(mockRouter).toHaveBeenCalledWith(['/sessions']);
  });

  it('should failed on submit ', () => {
    const mockSessionService =  jest.spyOn(sessionService, 'logIn');
    const mockRouter = jest.spyOn(router, 'navigate').mockReturnValue(Promise.resolve(true)); 
    const mockLogin = jest.spyOn(authService, 'login').mockReturnValue(throwError(() => new Error('test')));

    component.submit();
    fixture.detectChanges();

    expect(mockLogin).toHaveBeenCalled();
    expect(mockSessionService).not.toHaveBeenCalled();
    expect(mockRouter).not.toHaveBeenCalled();
    expect(component.onError).toBe(true);

    const errorElement = fixture.debugElement.query(By.css('.error')).nativeElement;
    expect(errorElement.innerHTML).toBe("An error occurred");
  });


});
