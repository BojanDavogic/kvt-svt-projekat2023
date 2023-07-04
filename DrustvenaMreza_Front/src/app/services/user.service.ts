import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { Observable } from 'rxjs';
import { User } from '../model/user.model';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class UserService {

  private registerUrl = environment.baseUrl + '/register';
  private apiUrl = environment.baseUrl + '/users';
  private headers = this.authService.getAuthenticatedHeaders();

  constructor(private http: HttpClient, private authService: AuthService) { }

  registerUser(user: any) {
    return this.http.post(this.registerUrl, user);
  }

  getUserProfile(): Observable<User> {
    const url = `${this.apiUrl}/profile`;
    return this.http.get<User>(url, { headers: this.headers });
  }

  updateUserProfile(user: User): Observable<void> {
    const url = `${this.apiUrl}/profile`;
    return this.http.put<void>(url, user, { headers: this.headers });
  }

  changePassword(userId: number, currentPassword: string, newPassword: string, newPasswordConfirm: string): Observable<void> {
    const url = `${this.apiUrl}/${userId}/change-password`;
    const body = {
      currentPassword,
      newPassword,
      newPasswordConfirm
    };
    return this.http.put<void>(url, body, { headers: this.headers });
  }
}
