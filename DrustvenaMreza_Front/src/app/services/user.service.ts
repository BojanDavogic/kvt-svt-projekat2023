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

  constructor(private http: HttpClient, private authService: AuthService) { }

  registerUser(user: any) {
    return this.http.post(this.registerUrl, user);
  }

  getUserProfile(): Observable<User> {
    const url = `${this.apiUrl}/profile`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<User>(url, { headers, responseType: 'json' });
  }

  updateUserProfile(user: User): Observable<void> {
    const url = `${this.apiUrl}/profile`;
    const headers = this.authService.getAuthenticatedHeaders();
    const payload = { user };
    return this.http.put<void>(url, user, { headers, responseType: 'json' });
  }

  changePassword(userId: number, currentPassword: string, newPassword: string, newPasswordConfirm: string): Observable<void> {
    const url = `${this.apiUrl}/${userId}/change-password`;
    const headers = this.authService.getAuthenticatedHeaders();
    const body = {
      currentPassword,
      newPassword,
      newPasswordConfirm
    };
    return this.http.put<void>(url, body, { headers });
  }
}
