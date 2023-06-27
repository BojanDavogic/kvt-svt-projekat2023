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

  constructor(private http: HttpClient, private authService: AuthService) { }

  getGroupById(groupId: number): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Group>(url, { headers });
  }
  

  createGroup(group: Group): Observable<Group> {
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.post<Group>(this.apiUrl, group, { headers });
  }
  

  updateGroup(groupId: number, group: Group): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.put<Group>(url, group, { headers });
  }
  

  deleteGroup(groupId: number): Observable<void> {
    const url = `${this.apiUrl}/${groupId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.delete<void>(url, { headers });
  }
  

  getAllGroups(): Observable<Group[]> {
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Group[]>(this.apiUrl, { headers, responseType: 'json' });
  }
  
  
}
