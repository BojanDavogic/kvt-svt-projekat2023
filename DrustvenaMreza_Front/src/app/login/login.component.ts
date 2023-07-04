import { Component, OnInit } from '@angular/core';
import { FormGroup, FormBuilder, Validators, FormControl } from '@angular/forms';
import { AuthService } from '../services/auth.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ToastrService } from 'ngx-toastr';
// import { ToastrService } from 'ngx-toastr';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {
	form: FormGroup;
	errorMessage: string = '';

	constructor(
		private fb: FormBuilder,
		private authenticationService: AuthService,
		private router: Router,
		private toastr: ToastrService
	) {
		this.form = this.fb.group({
			username : [null, Validators.required],
			password: [null, Validators.required]
		});
	}

	ngOnInit() {}

	get username(): FormControl {
		return this.form.get('username') as FormControl;
	  }
	
	  get password(): FormControl {
		return this.form.get('password') as FormControl;
	  }

	submit() {
		const auth: any = {};
		auth.username = this.form.value.username;
		auth.password = this.form.value.password;

		this.authenticationService.login(auth).subscribe(
			result => {
				localStorage.setItem('user', JSON.stringify(result));
				this.authenticationService.isLoggedIn();
				this.router.navigate(['home-page']);
				
			},
			error => {
				this.errorMessage = 'Pogrešno uneti korisničko ime ili lozinka.';
				console.error(error);
			}
		);
	}

	clear() {
		this.form.reset();
	  }
}
