import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Group } from '../model/group.model';
import { Observable } from 'rxjs';
import { HttpBackend } from '@angular/common/http';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private apiUrl = environment.baseUrl + '/groups';
  private headers = this.authService.getAuthenticatedHeaders();

  constructor(private http: HttpClient, private authService: AuthService) { }

  getGroupById(groupId: number): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.get<Group>(url, { headers: this.headers });
  }
  

  createGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(this.apiUrl, group, { headers: this.headers });
  }
  

  updateGroup(groupId: number, group: Group): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.put<Group>(url, group, { headers: this.headers });
  }
  

  deleteGroup(groupId: number): Observable<void> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }
  

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiUrl, { headers: this.headers });
  }
  
  searchGroups(searchTerm: string): Observable<Group[]> {
    return this.http.get<Group[]>(`${this.apiUrl}/search?term=${searchTerm}`, { headers: this.headers });
  }  
  
}
