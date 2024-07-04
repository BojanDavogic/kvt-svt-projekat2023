import { Injectable } from '@angular/core';
import { HttpClient, HttpParams } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from 'src/environments/environment';
import { AuthService } from './auth.service';
import { Group } from '../model/group.model';

@Injectable({
  providedIn: 'root'
})
export class SearchService {

  private apiUrlPosts = environment.baseUrl + '/posts';
  private apiUrlGroups = environment.baseUrl + '/groups';
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

    if (params.title) expressions.push(`title:${params.title}:OR`);
    if (params.text) expressions.push(`full_content:${params.text}:OR`);
    if (params.pdfContent) expressions.push(`file_content:${params.pdfContent}:OR`);
    if (params.comments) expressions.push(`comment_content:${params.comments}:OR`);
    if (params.likesMin) expressions.push(`number_of_likes:${params.likesMin}:OR`);
    if (params.likesMax) expressions.push(`number_of_likes:${params.likesMax}:OR`);

    expressions.forEach(expression => {
      httpParams = httpParams.append('expression', expression);
    });

    httpParams = httpParams.append('page', params.page || '0');
    httpParams = httpParams.append('size', params.size || '10');

    return this.http.get<any>(url, { params: httpParams, headers: this.headers });
  }

  simpleSearchGroups(params: any): Observable<any> {
    const url = this.apiUrlGroups + '/search/simple';
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

  advancedSearchGroups(params: any): Observable<any> {
    const url = `${this.apiUrlGroups}/search/advanced`;
    let httpParams = new HttpParams();
    let expressions: string[] = [];

    if (params.name) expressions.push(`name:${params.name}:OR`);
    if (params.description) expressions.push(`description:${params.description}:OR`);
    if (params.pdfContent) expressions.push(`file_content:${params.pdfContent}:OR`);
    if (params.postsMin) expressions.push(`number_of_posts:${params.postsMin}:OR`);
    if (params.postsMax) expressions.push(`number_of_posts:${params.postsMax}:OR`);
    if (params.likesMin) expressions.push(`number_of_likes:${params.likesMin}:OR`);
    if (params.likesMax) expressions.push(`number_of_likes:${params.likesMax}:OR`);
    if (params.likesMax) expressions.push(`rules:${params.rules}:OR`);

      expressions.forEach(expression => {
        httpParams = httpParams.append('expression', expression);
      });

      httpParams = httpParams.append('page', params.page || '0');
      httpParams = httpParams.append('size', params.size || '10');

    return this.http.get<any>(url, { params: httpParams, headers: this.headers });
  }
}
