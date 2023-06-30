import { Component, OnInit } from '@angular/core';
import { PostService } from '../services/post.service'; 
import { Post, Comment } from '../model/post.model';
import { AuthService } from '../services/auth.service';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  newPostContent: string = '';
  commentInput: string = '';

  constructor(private postService: PostService, private authService: AuthService) {}

  ngOnInit() {
    this.loadPosts();
  }

  createPost() {
    if (!this.newPostContent.trim()) {
      // Provjera praznog sadržaja
      return;
    }

    const currentUser = this.authService.getCurrentUser(); // Dobijanje ulogovanog korisnika
    if (!currentUser) {
      console.error('Nepoznat ulogovani korisnik.');
      return;
    }

    const newPost: Post = {
      content: this.newPostContent,
      comments: [],
      isEditing: false,
      isUpdating: false,
      showComments: false,
      updatedContent: ''
    };

    this.postService.createPost(newPost).subscribe(
      createdPost => {
        this.posts.push(createdPost);
        this.loadPosts();
        this.newPostContent = '';
      },
      error => {
        console.error('Greška prilikom kreiranja posta:', error);
      }
    );
  }

  loadPosts() {
    this.postService.getAllPostsWithoutGroup().subscribe(
      posts => {
        this.posts = posts;
        for (const post of this.posts) {
          if (post.showComments) {
            this.loadComments(post.id);
          }
        }
        this.posts.sort((a, b) => {
          // Provera da li je creationDate definisano pre sortiranja
          if (a.creationDate && b.creationDate) {
            return new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime();
          } else {
            return 0;
          }
        });
        this.posts.reverse();
        console.log(this.posts);
      },
      error => {
        console.error('Greška prilikom preuzimanja postova:', error);
      }
    );
  }

  startEditing(post: Post) {
    post.isEditing = true;
    post.updatedContent = post.content;
  }

  cancelEditing(post: Post) {
    post.isEditing = false;
  }

  updatePost(postId: number, post: Post) {
    if (!post.updatedContent.trim()) {
      // Provjera praznog sadržaja
      return;
    }

    post.content = post.updatedContent;
    post.isEditing = false;
    post.isUpdating = true;

    // Pozovite servis za ažuriranje posta
    this.postService.updatePost(postId, post.content).subscribe(
      updatedPost => {
        // Uspješno ažuriranje posta
        post.isUpdating = false;
      },
      error => {
        console.error('Greška prilikom ažuriranja posta:', error);
        // Poništi ažuriranje i vrati originalni sadržaj i datum
        post.content = post.updatedContent;
        
        post.isEditing = true;
        post.isUpdating = false;
      }
    );
  }

  deletePost(postId: number){
    this.postService.deletePost(postId).subscribe(() => {
      this.loadPosts();
    })
  }

  reactToPost(postId: number, reaction: string) {
    // Implementirajte logiku za reakciju na objavu (like, dislike, heart)
  }

  openComments(post: Post) {
    if(post) {
      post.showComments = !post.showComments;
      if (post.showComments) {
        this.loadComments(post);
      }
    }
  }


  loadComments(post: Post) {
    if (post) {
      console.log(post);
      this.postService.getCommentsForPost(post.id).subscribe(
        comments => {
          post.comments = comments.map(comment => {
            return {
              ...comment,
              timestamp: comment.timestamp // Ažurirajte polje timestamp
            };
          });
        },
        error => {
          console.error('Greška prilikom preuzimanja komentara:', error);
        }
      );
    }
  }
  

  addComment(postId: number, commentContent: string) {
    const post = this.posts.find(p => p.id === postId);
    if (post) {
      const currentUser = this.authService.getCurrentUser();
      if (!currentUser || !currentUser.sub) {
        console.error('Nepoznat ulogovani korisnik.');
        return;
      }
      const newComment: Comment = {
        text: commentContent
      };
      this.postService.addComment(post.id, newComment).subscribe(
        createdComment => {
          post.comments.push(createdComment);
          console.log(createdComment);
          this.commentInput = '';
        },
        error => {
          console.error('Greška prilikom dodavanja komentara:', error);
        }
      );
    }
  }
  
  
}
