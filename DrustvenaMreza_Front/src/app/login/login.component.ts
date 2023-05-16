import { Component } from '@angular/core';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent {
  email: string;
  password: string;

  constructor() {
    this.email = '';
    this.password = '';
   }

  login() {
    // Ovde bi se pozvao servis za prijavljivanje, a email i password bi se prosledili kao parametri
    console.log(`Prijavljen korisnik sa email adresom: ${this.email}`);
  }

  clear() {
    this.email = '';
    this.password = '';
  }
}
