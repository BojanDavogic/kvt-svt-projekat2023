import { Component } from '@angular/core';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent {
  posts: any[] = [
    {
      id: 1,
      author: 'John Doe',
      createdAt: new Date(),
      content: 'This is the first post.',
      showComments: false,
      comments: []
    },
    {
      id: 2,
      author: 'Jane Smith',
      createdAt: new Date(),
      content: 'Check out this amazing photo!',
      showComments: false,
      comments: []
    }
  ];

  reactToPost(postId: number, reaction: string) {
    // Implementirajte logiku za reakciju na objavu (like, dislike, heart)
  }

  openComments(postId: number) {
    const post = this.posts.find(p => p.id === postId);
    if (post) {
      post.showComments = !post.showComments;
    }
  }

  addComment(postId: number, commentContent: string) {
    const post = this.posts.find(p => p.id === postId);
    if (post) {
      const comment = {
        author: 'Current User',
        content: commentContent
      };
      post.comments.push(comment);
    }
  }
}
