import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrl = 'http://localhost:8080/api/posts/search';

  constructor(private http: HttpClient) { }

  searchPosts(params: any): Observable<any> {
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key]) {
        httpParams = httpParams.append(key, params[key]);
      }
    }
    return this.http.get<any>(this.apiUrl, { params: httpParams });
  }
}
