import { Component } from '@angular/core';
import { UserService } from '../services/user.service';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-change-password',
  templateUrl: './change-password.component.html',
  styleUrls: ['./change-password.component.css']
})
export class ChangePasswordComponent {
  user!: User;
  currentPassword: string = '';
  newPassword: string = '';
  confirmPassword: string = '';
  isSubmitting: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';

  constructor(private userService: UserService, private authService: AuthService, private router: Router) { }

  changePassword(): void {
    this.errorMessage = '';
    this.successMessage = '';

    // Provera da li su nova lozinka i potvrda nove lozinke iste
    if (this.newPassword !== this.confirmPassword) {
      this.errorMessage = 'Nova lozinka se ne podudara s potvrdom lozinke.';
      return;
    }

    this.isSubmitting = true;

    // Poziv metode za promenu lozinke
    this.userService.changePassword(this.user.id, this.currentPassword, this.newPassword, this.confirmPassword)
      .subscribe(
        (response) => {
          console.log(response);
          this.isSubmitting = false;
          this.successMessage = 'Lozinka je uspešno promenjena.';
          setTimeout(() => {
            this.router.navigate(['user-profile']);
          }, 2000);
        },
        (error) => {
          console.log(error);
          this.isSubmitting = false;
          if (error.status === 400) {
            this.errorMessage = error.error;
          } else {
            this.errorMessage = 'Došlo je do greške prilikom promene lozinke. Pokušajte ponovo.';
          }
        }
      );
  }

  ngOnInit(): void {
    this.userService.getUserProfile().subscribe(
      (user) => {
        this.user = user;
      },
      (error) => {
        console.error('Greška prilikom dobavljanja korisničkog profila.', error);
      }
    );
  }
}
