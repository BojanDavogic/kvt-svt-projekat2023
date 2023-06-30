import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Post, Comment } from '../model/post.model';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = environment.baseUrl + '/posts';

  constructor(private http: HttpClient, private authService: AuthService) { }

  getPostById(postId: number): Observable<Post> {
    const url = `${this.apiUrl}/${postId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Post>(url, { headers });
  }

  createPost(post: Post): Observable<Post> {
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.post<Post>(this.apiUrl, post, { headers });
  }

  updatePost(postId: number, postContent: string): Observable<Post> {
    const url = `${this.apiUrl}/${postId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.put<Post>(url, postContent, { headers });
  }

  deletePost(postId: number): Observable<void> {
    const url = `${this.apiUrl}/${postId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.delete<void>(url, { headers });
  }

  // getAllPosts(): Observable<Post[]> {
  //   const headers = this.authService.getAuthenticatedHeaders();
  //   return this.http.get<Post[]>(this.apiUrl, { headers, responseType: 'json' });
  // }

    getAllPostsWithoutGroup(): Observable<Post[]> {
    const url = `${this.apiUrl}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Post[]>(url, { headers, responseType: 'json' });
  }

  getPostsByGroupId(groupId: number): Observable<Post[]> {
    const url = `${this.apiUrl}/group/${groupId}`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Post[]>(url, { headers, responseType: 'json' });
  }

  addComment(postId: number, comment: Comment): Observable<Comment> {
    const url = `${this.apiUrl}/${postId}/comments`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.post<Comment>(url, comment, { headers, responseType: 'json' });
  }

  getCommentsForPost(postId: number): Observable<Comment[]> {
    const url = `${this.apiUrl}/${postId}/comments`;
    const headers = this.authService.getAuthenticatedHeaders();
    return this.http.get<Comment[]>(url, { headers, responseType: 'json' });
  }

}
