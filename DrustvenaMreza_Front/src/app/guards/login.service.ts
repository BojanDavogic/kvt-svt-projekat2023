import { Injectable } from '@angular/core';
import { Router} from '@angular/router';
import { AuthService } from '../services/auth.service';

@Injectable({
  providedIn: 'root'
})
export class LoginGuard {

  constructor(public auth: AuthService, public router: Router) { }

  canActivate(): boolean {
		if (this.auth.isLoggedIn()) {
			this.router.navigate(['/home-page']);
			return false;
		}
		return true;
	}
}
