import { Component } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { environment } from 'src/environments/environment';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'DrustvenaMreza_Front';
  message = '';

  constructor(private http: HttpClient) {}

  ngOnInit() {
    // this.http.get(environment.baseUrl + '/hello', {responseType: 'text'}).subscribe((data: string) => {
    //   this.message = data;
    // });
  }
}

// template: '<div>{{ message }}</div>'
