import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';
import { AuthService } from './services/auth.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'DrustvenaMreza_Front';
  message = '';

  constructor(private router: Router, public authService: AuthService) {}

  logout(): void {
    localStorage.removeItem('user');
    this.router.navigate(['login']);
  }

  ngOnInit() {}
}
