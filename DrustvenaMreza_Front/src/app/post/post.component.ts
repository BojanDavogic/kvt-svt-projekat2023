import { Component, OnInit } from '@angular/core';
import { PostService } from '../services/post.service'; 
import { Post, Comment, Reaction } from '../model/post.model';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  posts: Post[] = [];
  newPostContent: string = '';
  commentInput: string = '';
  currentUser: any;
  selectedButton: string = '';
  buttonColor: string = 'whitesmoke';

  constructor(private postService: PostService, private authService: AuthService) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadPosts();
  }

  createPost() {
    if (!this.newPostContent.trim()) {
      return;
    }

    this.currentUser = this.authService.getCurrentUser();
    console.log(this.currentUser, "ovo je ulogovani korisnik");
    if (!this.currentUser) {
      console.error('Nepoznat ulogovani korisnik.');
      return;
    }

    const newPost: Post = {
      content: this.newPostContent,
      comments: [],
      isEditing: false,
      isUpdating: false,
      showComments: false,
      updatedContent: '',
      reactions: [],
      selectedReactions: []
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
          post.reactions = [];
          if (post.showComments) {
            this.loadComments(post.id);
          }
          this.loadReactions(post.id);
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
    this.currentUser = this.authService.getCurrentUser();

    if (!this.currentUser || post.postedBy?.username !== this.currentUser.sub) {
      console.error('Nemate dozvolu za izmenu ovog posta.');
      return;
    }
    if (!post.updatedContent.trim()) {
      return;
    }

    post.content = post.updatedContent;
    post.isEditing = false;
    post.isUpdating = true;

    this.postService.updatePost(postId, post.content).subscribe(
      updatedPost => {
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
    this.currentUser = this.authService.getCurrentUser();
    const post = this.posts.find(p => p.id === postId);

  if (!post || !this.currentUser || post.postedBy?.username !== this.currentUser.sub) {
    console.error('Nemate dozvolu za brisanje ovog posta.');
    return;
  }

  this.postService.deletePost(postId).subscribe(() => {
    this.loadPosts();
  });
  }

  reactToPost(postId: number, reaction: string) {
    const post = this.posts.find(p => p.id === postId);
    if (post) {
      const currentUser = this.authService.getCurrentUser();
  
      if (!currentUser) {
        console.error('Nepoznat ulogovani korisnik.');
        return;
      }
  
      const existingReaction = post.reactions.find(r => r.madeBy?.username === currentUser.sub);
  
      if (existingReaction) {
        // Korisnik već ima reakciju na objavu
        if (existingReaction.type === reaction) {
          // Korisnik je već reagovao sa istom reakcijom, treba je ukloniti
          this.postService.deleteReaction(existingReaction.id).subscribe(() => {
            this.loadReactions(post.id);
          });
        } else {
          // Korisnik ima prethodnu reakciju, treba je ažurirati sa novom reakcijom
          this.postService.updateReaction(existingReaction.id, reaction).subscribe(() => {
            this.loadReactions(post.id);
          });
        }
      } else {
        // Korisnik nema prethodnu reakciju, treba dodati novu reakciju na objavu
        const newReaction: Reaction = {
          type: reaction,
          madeBy: currentUser
        };
  
        this.postService.addReaction(post.id, reaction).subscribe(
          createdReaction => {
            post.reactions.push(newReaction);
            this.loadReactions(post.id);
            console.log(newReaction);
          },
          error => {
            console.error('Greška prilikom reagovanja na post:', error);
          }
        );
      }
    }
  }

  isLiked(post: Post): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      return post.reactions.some(reaction => reaction.type === 'LIKE' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }
  
  likePost(postId: number) {
    this.selectedButton = 'like';
    this.reactToPost(postId, 'LIKE');
    const post = this.getPostById(postId);
    if (post) {
      this.loadReactions(post.id);
      console.log(post.reactions);
    }
  }

  isDisliked(post: Post): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      return post.reactions.some(reaction => reaction.type === 'DISLIKE' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }

  dislikePost(postId: number) {
    this.selectedButton = 'dislike';
    this.reactToPost(postId, 'DISLIKE');
    const post = this.getPostById(postId);
    if (post) {
      this.loadReactions(post.id);
      console.log(post.reactions);
    }
  }

  isHearted(post: Post): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser) {
      return post.reactions.some(reaction => reaction.type === 'HEART' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }

  heartPost(postId: number) {
    this.selectedButton = 'heart';
    this.reactToPost(postId, 'HEART');
    const post = this.getPostById(postId);
    if (post) {
      this.loadReactions(post.id);
      console.log(post.reactions);
    }
  }
  

  getPostById(postId: number): Post | undefined {
    return this.posts.find(post => post.id === postId);
  }
  

  hasReaction(reactions: Reaction[], type: string): boolean {
    const currentUser = this.authService.getCurrentUser();
    return reactions.some(reaction => reaction.type === type && reaction.madeBy?.username === currentUser?.sub);
  }
  

  loadReactions(postId: number) {
    this.postService.getReactionsForPost(postId).subscribe(
      reactions => {
        const post = this.posts.find(p => p.id === postId);
        if (post) {
          post.reactions = reactions;
        }
      },
      error => {
        console.error('Greška prilikom preuzimanja reakcija:', error);
      }
    );
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
        text: commentContent,
        updatedText: '',
        isUpdating: false
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

  editComment(comment: Comment) {
    comment.isEditing = true;
    comment.updatedText = comment.text;
  }
  
  deleteComment(post: Post, commentId: number) {
    this.currentUser = this.authService.getCurrentUser();
    const comment = post.comments.find(c => c.id === commentId);

    if (!comment || !this.currentUser || comment.user?.username !== this.currentUser.sub) {
      console.error('Nemate dozvolu za brisanje ovog komentara.');
      return;
    }

    if (confirm('Da li ste sigurni da želite da obrišete ovaj komentar?')) {
      this.postService.deleteComment(commentId).subscribe(() => {
        this.loadComments(post);
      });
    }
  }

  updateComment(post: Post, commentId: number, comment: Comment) {
    const currentUser = this.authService.getCurrentUser();

    if (!currentUser || comment.user?.username !== currentUser.sub) {
      console.error('Nemate dozvolu za izmenu ovog komentara.');
      return;
    }

    if (!comment.updatedText.trim()) {
      return;
    }

    comment.text = comment.updatedText;
    comment.isEditing = false;
    comment.isUpdating = true;

    this.postService.updateComment(commentId, comment.text).subscribe(
      updatedComment => {
        comment.isUpdating = false;
        this.loadComments(post);
      },
      error => {
        console.error('Greška prilikom ažuriranja komentara:', error);
        comment.text = comment.updatedText;
        comment.isEditing = true;
        comment.isUpdating = false;
      }
    );
  }

  cancelEditComment(comment: Comment) {
    comment.isEditing = false;
  } 
}
