import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Group } from '../model/group.model';
import { Observable } from 'rxjs';
import { HttpBackend } from '@angular/common/http';

@Injectable({
  providedIn: 'root'
})
export class GroupService {
  private apiUrl = environment.baseUrl + '/groups';

  constructor(private http: HttpClient) { }

  getGroupById(groupId: number): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.get<Group>(url);
  }

  createGroup(group: Group): Observable<Group> {
    return this.http.post<Group>(this.apiUrl, group);
  }

  updateGroup(groupId: number, group: Group): Observable<Group> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.put<Group>(url, group);
  }

  deleteGroup(groupId: number): Observable<void> {
    const url = `${this.apiUrl}/${groupId}`;
    return this.http.delete<void>(url);
  }

  getAllGroups(): Observable<Group[]> {
    return this.http.get<Group[]>(this.apiUrl);
  }
}
