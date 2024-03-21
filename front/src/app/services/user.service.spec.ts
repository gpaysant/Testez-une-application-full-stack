import { TestBed } from '@angular/core/testing';
import { expect } from '@jest/globals';

import { UserService } from './user.service';

import { User } from '../interfaces/user.interface';
import {  HttpTestingController, HttpClientTestingModule } from '@angular/common/http/testing';

describe('UserService', () => {
  let service: UserService;
  let httpTestingController: HttpTestingController;
  let expectedUser: User;

  beforeEach((): void => {
    TestBed.configureTestingModule({
      imports:[
        HttpClientTestingModule 
      ]
    });
    service = TestBed.inject(UserService);
    httpTestingController = TestBed.inject(HttpTestingController);
    
    expectedUser = {
      id: 1,
      email: 'toto@gmail.com',
      lastName: 'Macco',
      firstName: 'peter',
      admin: true,
      password: '467890',
      createdAt: new Date()
    };
  });

  afterEach(() => {
    httpTestingController.verify()
  })

  it('should be created', () => {
    expect(service).toBeTruthy();
  });


  it('should return expected user ', () => {
    const userId = '1';
    service.getById(userId).subscribe(
      user => expect(user).toEqual(expectedUser)
    )

    const req = httpTestingController.expectOne(`api/user/${userId}`);
    expect(req.request.method).toEqual('GET');

    // Respond with the mock 
    req.flush(expectedUser);
  });

  it('should call delete ', () => {
    const userId = '1';
    service.delete(userId).subscribe();

    const req = httpTestingController.expectOne(`api/user/${userId}`);
    expect(req.request.method).toEqual('DELETE');

    // Simulate successful response
    req.flush({});
  });

});
