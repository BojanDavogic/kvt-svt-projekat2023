import { Injectable } from '@angular/core';
import { environment } from 'src/environments/environment';
import { HttpClient } from '@angular/common/http';
import { Post, Comment, Reaction } from '../model/post.model';
import { Observable } from 'rxjs';
import { AuthService } from './auth.service';

@Injectable({
  providedIn: 'root'
})
export class PostService {
  private apiUrl = environment.baseUrl + '/posts';
  private apiUrlGroup = environment.baseUrl + '/groups';
  private headers = this.authService.getAuthenticatedHeaders();

  constructor(private http: HttpClient, private authService: AuthService) { }

  getPostById(postId: number): Observable<Post> {
    const url = `${this.apiUrl}/${postId}`;
    return this.http.get<Post>(url, { headers: this.headers });
  }

  createPost(post: Post, file: File | null): Observable<Post> {
    const formData = new FormData();
    if (file) {
      formData.append('file', file);
    }
    formData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }));
    return this.http.post<Post>(this.apiUrl, formData, { headers: this.headers.delete('Content-Type') });
  }

  createGroupPost(groupId: number, post: Post, file: File | null): Observable<Post> {
    const url = `${this.apiUrlGroup}/${groupId}/posts`;
    const formData = new FormData();
    if (file) {
      formData.append('file', file);
    }
    formData.append('post', new Blob([JSON.stringify(post)], { type: 'application/json' }));
    return this.http.post<Post>(url, formData, { headers: this.headers.delete('Content-Type') });
  }

  updatePost(postId: number, postTitle: string, postContent: string): Observable<Post> {
    const url = `${this.apiUrl}/${postId}`;
    const body = { title: postTitle, content: postContent };
    return this.http.put<Post>(url, body, { headers: this.headers });
  }

  deletePost(postId: number): Observable<void> {
    const url = `${this.apiUrl}/${postId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }

  deleteGroupPost(groupId: number, postId: number): Observable<void> {
    const url = `${this.apiUrlGroup}/${groupId}/posts/${postId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }

  getAllPostsWithoutGroup(): Observable<Post[]> {
    const url = `${this.apiUrl}`;
    return this.http.get<Post[]>(url, { headers: this.headers });
  }

  getPostsByGroupId(groupId: number): Observable<Post[]> {
    const url = `${this.apiUrlGroup}/${groupId}/posts`;
    return this.http.get<Post[]>(url, { headers: this.headers });
  }

  addComment(postId: number, comment: Comment): Observable<Comment> {
    const url = `${this.apiUrl}/${postId}/comments`;
    return this.http.post<Comment>(url, comment, { headers: this.headers });
  }

  getCommentsForPost(postId: number): Observable<Comment[]> {
    const url = `${this.apiUrl}/${postId}/comments`;
    return this.http.get<Comment[]>(url, { headers: this.headers });
  }

  updateComment(commentId: number, newText: string): Observable<Comment> {
    const url = `${this.apiUrl}/comments/${commentId}`;
    const payload = { text: newText };
    return this.http.put<Comment>(url, payload, { headers: this.headers });
  }

  deleteComment(commentId: number): Observable<void> {
    const url = `${this.apiUrl}/comments/${commentId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }

  addReaction(postId: number, reaction: string): Observable<Reaction> {
    const url = `${this.apiUrl}/${postId}/reactions`;
    return this.http.post<Reaction>(url, reaction, { headers: this.headers });
  }

  addReactionForComment(commentId: number, reaction: string): Observable<Reaction> {
    const url = `${this.apiUrl}/comments/${commentId}/reactions`;
    return this.http.post<Reaction>(url, reaction, { headers: this.headers });
  }

  getReactionsForPost(postId: number): Observable<Reaction[]> {
    const url = `${this.apiUrl}/${postId}/reactions`;
    return this.http.get<Reaction[]>(url, { headers: this.headers });
  }

  getReactionsForComment(commentId: number): Observable<Reaction[]> {
    const url = `${this.apiUrl}/comments/${commentId}/reactions`;
    return this.http.get<Reaction[]>(url, { headers: this.headers });
  }

  updateReaction(reactionId: number, newReaction: string): Observable<Reaction> {
    const url = `${this.apiUrl}/reactions/${reactionId}`;
    return this.http.put<Reaction>(url, newReaction, { headers: this.headers });
  }

  updateCommentReaction(commentId: number, reactionId: number, newReaction: string): Observable<Reaction> {
    const url = `${this.apiUrl}/comments/${commentId}/reactions/${reactionId}`;
    return this.http.put<Reaction>(url, newReaction, { headers: this.headers });
  }

  deleteReaction(reactionId: number): Observable<void> {
    const url = `${this.apiUrl}/reactions/${reactionId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }

  deleteCommentReaction(commentId: number, reactionId: number): Observable<void> {
    const url = `${this.apiUrl}/comments/${commentId}/reactions/${reactionId}`;
    return this.http.delete<void>(url, { headers: this.headers });
  }

  addReply(postId: number, commentId: number, reply: Comment): Observable<Comment> {
    const url = `${this.apiUrl}/comments/${commentId}/replies`;
    return this.http.post<Comment>(url, reply, { headers: this.headers });
  }

  getRepliesForComment(commentId: number): Observable<Comment[]> {
    const url = `${this.apiUrl}/comments/${commentId}/replies`;
    return this.http.get<Comment[]>(url, { headers: this.headers });
  }

  // search

  searchPosts(query: string): Observable<Post[]> {
    return this.http.get<Post[]>(`${this.apiUrl}/searchByTitle`, { params: { q: query }, headers: this.headers});
  }

}
