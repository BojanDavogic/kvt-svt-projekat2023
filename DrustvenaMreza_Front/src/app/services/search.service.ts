import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrlPosts = environment.baseUrl + '/posts';
  private apiUrlGroups = environment.baseUrl + '/groups/search';
  private headers = this.authService.getAuthenticatedHeaders();

  constructor(private http: HttpClient, private authService: AuthService) { }

  searchPosts(params: any): Observable<any> {
    const url = this.apiUrlPosts+'/searchByTitle'
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key]) {
        httpParams = httpParams.append(key, params[key]);
      }
    }
    return this.http.get<any>(url, { params: httpParams, headers: this.headers });
  }

  simpleSearchPosts(params: any): Observable<any> {
    const url = this.apiUrlPosts + '/search/simple';
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key] && key !== 'type') {
        httpParams = httpParams.append('query', params[key]);
      }
    }

    httpParams = httpParams.append('page', params.page || '0');
    httpParams = httpParams.append('size', params.size || '10');

    console.log("URL: " + httpParams);

    return this.http.get<any>(url, { params: httpParams, headers: this.headers });
  }

  advancedSearchPosts(params: any): Observable<any> {
    const url = this.apiUrlPosts + '/search/advanced';
    let httpParams = new HttpParams();
    let expressions: string[] = [];

    if (params.title) expressions.push(`title:${params.title}`);
    if (params.text) expressions.push(`full_content:${params.text}`);
    if (params.pdfContent) expressions.push(`file_content:${params.pdfContent}`);
    if (params.comments) expressions.push(`comment_content:${params.comments}`);
    if (params.likesMin) expressions.push(`number_of_likes:${params.likesMin}`);
    if (params.likesMax) expressions.push(`number_of_likes:${params.likesMax}`);

    if (expressions.length > 1) {
      for (let i = 1; i < expressions.length; i++) {
        expressions.splice(i, 0, 'OR');
        i++;
      }
    }

    expressions.forEach(expression => {
      httpParams = httpParams.append('expression', expression);
    });

    httpParams = httpParams.append('page', params.page || '0');
    httpParams = httpParams.append('size', params.size || '10');

    console.log(httpParams);

    return this.http.get<any>(url, { params: httpParams, headers: this.headers });
  }

  searchGroups(params: any): Observable<any> {
    let httpParams = new HttpParams();
    for (let key in params) {
      if (params[key]) {
        httpParams = httpParams.append(key, params[key]);
      }
    }
    return this.http.get<any>(this.apiUrlGroups, { params: httpParams, headers: this.headers});
  }
}
