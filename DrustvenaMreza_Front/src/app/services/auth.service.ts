import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable, map } from 'rxjs';
import { environment } from 'src/environments/environment';
import { tap, catchError } from 'rxjs';
import { throwError } from 'rxjs';
import { JwtUtilsService } from './jwt-utils.service';
import { Router } from '@angular/router';
import { User } from '../model/user.model';
import { LoginResponse } from '../model/login.response';

@Injectable({
  providedIn: 'root'
})
export class AuthService {
	private currentUser: User | null = null;
  	private headers = new HttpHeaders({'Content-Type': 'application/json'});

	constructor(
		private http: HttpClient,
		private router: Router,
		private jwtUtilsService: JwtUtilsService,
	) { 
		this.currentUser = this.getCurrentUser();
	}

	getCurrentUsername() {
		const token = this.getToken();
		if (token) {
		  const decodedToken = this.jwtUtilsService.decodeToken(token);
		  if (decodedToken && decodedToken.sub) {
			return decodedToken.sub;
		  }
		}
		return null;
	}

	getCurrentUser(): User | null {
		const token = this.getToken();
		if (token) {
		  const decodedToken = this.jwtUtilsService.decodeToken(token);
		  if (decodedToken && decodedToken.sub) {
			return decodedToken;
		  }
		}
		return null;
	}

	login(auth: any): Observable<any> {
		return this.http.post(environment.baseUrl + '/login', { username: auth.username, password: auth.password }, { headers: this.headers }).pipe(
		  map((response: any) => {
			const loginResponse = response as LoginResponse;
			this.currentUser = loginResponse.user;
			localStorage.setItem('user', JSON.stringify(loginResponse.user));
			return response;
		  }),
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
		if (token) {
			return this.headers.set('Authorization', `Bearer ${token}`);
		}
		return this.headers;
	}
}
