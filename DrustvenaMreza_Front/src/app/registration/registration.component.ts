import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { Router } from '@angular/router';
import { trigger, transition, animate, style } from '@angular/animations';

@Component({
  selector: 'app-registration',
  templateUrl: './registration.component.html',
  styleUrls: ['./registration.component.css'],
  animations: [
    trigger('fadeInOut', [
      transition(':enter', [
        style({ opacity: 0 }),
        animate('300ms', style({ opacity: 1 }))
      ]),
      transition(':leave', [
        animate('300ms', style({ opacity: 0 }))
      ])
    ])
  ]
})
export class RegistrationComponent implements OnInit {
  firstname?: string;
  lastname?: string;
  username?: string;
  email?: string;
  password?: string;
  confirmPassword?: string;
  showSuccess: boolean = false;
  errorMessage: string | null = null;

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {}

  register() {
    if (this.isValidForm()) {
      const user = {
        firstName: this.firstname,
        lastName: this.lastname,
        username: this.username,
        email: this.email,
        password: this.password
      };

      this.userService.registerUser(user).subscribe(
        (response) => {
          this.showSuccessPopup();
        },
        (error) => {
          this.errorMessage = "Greška prilikom registracije";
          console.error('Greška prilikom registracije:', error);
        }
      );
    }
  }

  private isValidForm() {
    this.firstname = this.firstname?.trim();
    this.lastname = this.lastname?.trim();
    this.username = this.username?.trim();
    this.email = this.email?.trim();
    this.password = this.password?.trim();
    this.confirmPassword = this.confirmPassword?.trim();

    if (!this.firstname || !this.lastname || !this.username || !this.email || !this.password || !this.confirmPassword) {
      this.errorMessage = 'Sva polja moraju biti popunjena.';
      return false;
    }
  
    if (this.password !== this.confirmPassword) {
      this.errorMessage = 'Lozinke se ne podudaraju.';
      return false;
    }

    if (/\s/.test(this.password)) {
      this.errorMessage = 'Lozinka ne sme sadržati razmake.';
      return false;
    }
  
    this.errorMessage = null;
    return true;
  }
  

  showSuccessPopup() {
    this.showSuccess = true;
    setTimeout(() => {
      this.router.navigate(['login']);
    }, 3000);
  }

  hideSuccessPopup() {
    this.showSuccess = false;
  }

  clear() {
    this.firstname = '';
    this.lastname = '';
    this.username = '';
    this.email = '';
    this.password = '';
    this.confirmPassword = '';
  }
}

