import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { tap, catchError } from 'rxjs';
import { throwError } from 'rxjs';
import { JwtUtilsService } from './jwt-utils.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
  private headers = new HttpHeaders({'Content-Type': 'application/json'});

	constructor(
		private http: HttpClient,
		private router: Router
	) { }

	login(auth: any): Observable<any> {
		return this.http.post(environment.baseUrl + '/login', {username: auth.username, password: auth.password}, {headers: this.headers}).pipe(
		  tap(response => console.log(response)),
		  catchError(error => {
			console.error(error);
			throw error;
		  })
		);
	  }

	  logout(): void {
		localStorage.clear();
		this.router.navigate(['/login']);
	  }

	isLoggedIn(): boolean {
		if (!localStorage.getItem('user')) {
				return false;
		}
		return true;
	}

	getToken(): string | null {
		const user = localStorage.getItem('user');
		if (user) {
			const userData = JSON.parse(user);
			return userData.accessToken;
		}
		return null;
	}

	getAuthenticatedHeaders(): HttpHeaders {
		const token = this.getToken();
		console.log(token)
		if (token) {
			return this.headers.set('Authorization', `Bearer ${token}`);
		}
		return this.headers;
	}
}
