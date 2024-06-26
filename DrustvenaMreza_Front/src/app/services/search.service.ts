import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrlPosts = environment.baseUrl + 'posts/search';
  private apiUrlGroups = environment.baseUrl + 'groups/search';

  constructor(private http: HttpClient) { }

  searchPosts(params: any): Observable<any> {
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key]) {
        httpParams = httpParams.append(key, params[key]);
      }
    }
    return this.http.get<any>(this.apiUrlPosts, { params: httpParams });
  }

  searchGroups(params: any): Observable<any> {
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key]) {
        httpParams = httpParams.append(key, params[key]);
      }
    }
    return this.http.get<any>(this.apiUrlGroups, { params: httpParams });
  }
}
