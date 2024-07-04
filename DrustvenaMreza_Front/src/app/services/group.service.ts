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
  

  createGroup(group: Group, file: File): Observable<Group> {
    const formData = new FormData();
    formData.append('group', new Blob([JSON.stringify(group)], { type: 'application/json' }));
    formData.append('file', file);
    
    return this.http.post<Group>(this.apiUrl, formData, { headers: this.headers.delete('Content-Type') });
  }
  

  updateGroup(groupId: number, group: Group): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.put<Group>(url, group, { headers: this.headers });
  }

  updateGroupRules(groupId: number, rules: string): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}/updateRules`;
    return this.http.put<Group>(url, rules, { headers: this.headers });
  }
  

  deleteGroup(groupId: number): Observable<void> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }
  

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiUrl, { headers: this.headers });
  } 
  
}
