import { Component, OnInit } from '@angular/core';
import { UserService } from '../services/user.service';
import { User } from '../model/user.model';
import { Router } from '@angular/router';

@Component({
  selector: 'app-user-profile',
  templateUrl: './user-profile.component.html',
  styleUrls: ['./user-profile.component.css']
})
export class UserProfileComponent implements OnInit {
  user: User = {
    username: '',
    password: '',
    email: '',
    firstName: '',
    lastName: '',
    isDelete: false
  };
  
  password: string = '';
  isEditingImage: boolean = false;
  isSaving: boolean = false;
  errorMessage: string = '';
  successMessage: string = '';
  editingField: string = '';

  constructor(private userService: UserService, private router: Router) { }

  ngOnInit(): void {
    this.getUserProfile();
  }

  getUserProfile(): void {
    this.userService.getUserProfile().subscribe(
      (user: User) => {
        this.user = user;
      },
      (error) => {
        this.errorMessage = 'Greška prilikom učitavanja korisničkih podataka.';
      }
    );
  }

  editImage(): void {
    this.isEditingImage = true;
  }

  handleImageChange(event: any): void {  
  }

  editField(fieldName: string): void {
    if (fieldName === 'password') {
      this.router.navigate(['change-password']);
    } else {
      this.editingField = fieldName;
    }
  }

  isEditingField(fieldName: string): boolean {
    return this.editingField === fieldName;
  }

  saveChanges(): void {
    this.isSaving = true;
    this.userService.updateUserProfile(this.user).subscribe(
      (response) => {
        this.isSaving = false;
        console.log(response);
        this.getUserProfile();
        this.successMessage = 'Uspesno ste izmenili zeljene podatke.';
        this.editingField = '';
      },
      (error) => {
        console.log(error);
        this.isSaving = false;
        this.errorMessage = 'Greška prilikom čuvanja izmena.';
      }
    );
  }

  cancelChanges(): void {
    this.getUserProfile();
    this.editingField = '';
  }
}
