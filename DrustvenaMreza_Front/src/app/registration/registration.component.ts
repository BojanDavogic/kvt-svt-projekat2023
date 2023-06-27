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

  constructor(private userService: UserService, private router: Router) {}

  ngOnInit() {}

  register() {
    // Provera da li forma ispravna
    if (this.isValidForm()) {
      const user = {
        firstName: this.firstname,
        lastName: this.lastname,
        username: this.username,
        email: this.email,
        password: this.password
      };

      this.userService.registerUser(user).subscribe(
        () => {
          // Uspesna registracija - prikazi popup prozor sa porukom
          this.showSuccessPopup();
        },
        (error) => {
          // Greska prilikom registracije - prikazi poruku korisniku
          console.error('Greška prilikom registracije:', error);
        }
      );
    }
  }

  private isValidForm() {
    // Implementirajte logiku za proveru validnosti forme (npr. da li su sva polja popunjena, da li lozinke odgovaraju, itd.)
    return true; // Vratite true ako je forma ispravna, inace false
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

