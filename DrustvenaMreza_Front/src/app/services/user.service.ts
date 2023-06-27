import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private registerUrl = environment.baseUrl + '/register';

  constructor(private http: HttpClient) { }

  registerUser(user: any) {
    return this.http.post(this.registerUrl, user);
  }
}
