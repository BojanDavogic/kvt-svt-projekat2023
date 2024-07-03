import { Component, Input, OnInit } from '@angular/core';
import { PostService } from '../services/post.service'; 
import { Post, Comment, Reaction } from '../model/post.model';
import { AuthService } from '../services/auth.service';
import { User } from '../model/user.model';
import { Group } from '../model/group.model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-post',
  templateUrl: './post.component.html',
  styleUrls: ['./post.component.css']
})
export class PostComponent implements OnInit {
  @Input() group?: Group;

  posts: Post[] = [];
  comments: Comment[] = [];
  newPostTitle: string = '';
  newPostContent: string = '';
  commentInput: string = '';
  currentUser!: User | null;
  selectedButton: string = '';
  buttonColor: string = 'whitesmoke';
  replyInput: any;
  replies: any;
  selectedFile: File | null = null;

  searchQuery: string = '';

  searchResults: any[] = [];
  showSearchResults: boolean = false;


  constructor(private postService: PostService, private authService: AuthService) {}

  ngOnInit() {
    this.currentUser = this.authService.getCurrentUser();
    this.loadPosts();
  }

  onFileSelected(event: any) {
    this.selectedFile = event.target.files[0];
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
      title: this.newPostTitle,
      content: this.newPostContent,
      comments: [],
      isEditing: false,
      isUpdating: false,
      showComments: false,
      updatedTitle: '',
      updatedContent: '',
      reactions: [],
      selectedReactions: []
    };

    let createPostRequest: Observable<Post>;
    if (this.group) {
      createPostRequest = this.postService.createGroupPost(this.group.id, newPost, this.selectedFile);
    } else {
      createPostRequest = this.postService.createPost(newPost, this.selectedFile);
    }

    createPostRequest.subscribe(
      createdPost => {
        this.posts.push(createdPost);
        this.loadPosts();
        this.newPostTitle = '';
        this.newPostContent = '';
        this.selectedFile = null;
      },
      error => {
        console.error('Greška prilikom kreiranja posta:', error);
      }
    );
  }

  loadPosts() {
    let getAllPostsRequest: Observable<Post[]>;
    if (this.group) {
      getAllPostsRequest = this.postService.getPostsByGroupId(this.group.id);
    } else {
      getAllPostsRequest = this.postService.getAllPostsWithoutGroup();
    }

    getAllPostsRequest.subscribe(
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
          if (a.creationDate && b.creationDate) {
            return new Date(a.creationDate).getTime() - new Date(b.creationDate).getTime();
          } else {
            return 0;
          }
        });
        this.posts.reverse();
      },
      error => {
        console.error('Greška prilikom preuzimanja postova:', error);
      }
    );
  }

  searchPosts(): void {
    this.postService.searchPosts(this.searchQuery).subscribe(posts => {
      this.posts = posts;
    });
  }

  onSearchResults(results: any[]): void {
    this.searchResults = results;
    this.showSearchResults = results.length > 0;
  }

  startEditing(post: Post) {
    post.isEditing = true;
    post.updatedTitle = post.title;
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
    if (!post.updatedTitle.trim()) {
      return;
    }
    post.title = post.updatedTitle;
    post.content = post.updatedContent;
    post.isEditing = false;
    post.isUpdating = true;

    this.postService.updatePost(postId, post.title, post.content).subscribe(
      updatedPost => {
        post.isUpdating = false;
      },
      error => {
        console.error('Greška prilikom ažuriranja posta:', error);
        post.title = post.updatedTitle;
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
        if (existingReaction.type === reaction) {          
          this.postService.deleteReaction(existingReaction.id).subscribe(() => {
            this.loadReactions(post.id);
          });
        } else {
          
          this.postService.updateReaction(existingReaction.id, reaction).subscribe(() => {
            this.loadReactions(post.id);
          });
        }
      } else {
        
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
    if (currentUser && post.reactions) {
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
    if (currentUser && post.reactions) {
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
    if (currentUser && post.reactions) {
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

  getCommentById(postId: number, commentId: number): Comment | undefined {
    const post = this.getPostById(postId);
    return post?.comments.find(comment => comment.id === commentId);
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

  loadCommentReactions(postId: number, commentId: number) {
    this.postService.getReactionsForComment(commentId).subscribe(
      reactions => {
        const comment = this.getCommentById(postId, commentId);
        if (comment) {
          comment.reactions = reactions;
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
      this.postService.getCommentsForPost(post.id).subscribe(
        comments => {
          this.comments = comments;
          for(const comment of this.comments) {
            comment.reactions = [];
            this.loadCommentReactions(post.id, comment.id);
          }
          post.comments = comments.map(comment => {
            return {
              ...comment,
              timestamp: comment.timestamp
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
        isUpdating: false,
        reactions: [],
        replies: [],
        showReplies: false
      };
      this.postService.addComment(post.id, newComment).subscribe(
        createdComment => {
          post.comments.push(createdComment);
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
  
  addReactionToComment(postId: number, commentId: number, reaction: string) {
    const post = this.posts.find(p => p.id === postId);
    if (post) {
      const currentUser = this.authService.getCurrentUser();
  
      if (!currentUser) {
        console.error('Nepoznat ulogovani korisnik.');
        return;
      }
      const comment = post.comments.find(c => c.id === commentId);
      if (comment) {
        const userReaction = comment.reactions.find(r => r.madeBy?.username === currentUser.sub);
        
        if (userReaction) {
          if(userReaction.type === reaction){
            this.postService.deleteCommentReaction(comment.id, userReaction.id).subscribe(() => {
              this.loadCommentReactions(post.id, comment.id);
            });
          } else {
            this.postService.updateCommentReaction(comment.id, userReaction.id, reaction).subscribe(() => {
              this.loadCommentReactions(post.id, comment.id);
            });
          }
        } else {
          const newCommentReaction: Reaction = {
            type: reaction,
            madeBy: currentUser
          };

          this.postService.addReactionForComment(comment.id, reaction).subscribe(
            createdReaction => {
              comment.reactions.push(newCommentReaction);
              this.loadCommentReactions(post.id, comment.id);
            },
            error => {
              console.error('Greška pri dodavanju reakcije na komentar', error);
            }
          );
        }
      }
    }
  }

  isLikedComment(comment: Comment): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && comment.reactions) {
      return comment.reactions.some(reaction => reaction.type === 'LIKE' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }
  
  likeComment(postId: number, commentId: number) {
    this.selectedButton = 'like';
    this.addReactionToComment(postId, commentId, 'LIKE');
    const comment = this.getCommentById(postId, commentId);
    if (comment) {
      this.loadCommentReactions(postId, comment.id);
    }
  }

  isDislikedComment(comment: Comment): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && comment.reactions) {
      return comment.reactions.some(reaction => reaction.type === 'DISLIKE' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }
  
  dislikeComment(postId: number, commentId: number) {
    this.selectedButton = 'dislike';
    this.addReactionToComment(postId, commentId, 'DISLIKE');
    const comment = this.getCommentById(postId, commentId);
    if (comment) {
      this.loadCommentReactions(postId, comment.id);
    }
  }
  
  isHeartedComment(comment: Comment): boolean {
    const currentUser = this.authService.getCurrentUser();
    if (currentUser && comment.reactions) {
      return comment.reactions.some(reaction => reaction.type === 'HEART' && reaction.madeBy?.username === currentUser.sub);
    }
    return false;
  }
  
  heartComment(postId: number, commentId: number) {
    this.selectedButton = 'heart';
    this.addReactionToComment(postId, commentId, 'HEART');
    const comment = this.getCommentById(postId, commentId);
    if (comment) {
      this.loadCommentReactions(postId, comment.id);
    }
  }

  replyToComment(post: Post, commentId: number, replyText: string) {
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      console.error('Nepoznat ulogovani korisnik.');
      return;
    }
  
    const comment = post.comments.find(c => c.id === commentId);
    if (!comment) {
      console.error('Nepoznat komentar.');
      return;
    }
  
    const newReply: Comment = {
      text: replyText,
      user: currentUser,
      isEditing: false,
      isUpdating: false,
      updatedText: '',
      reactions: [],
      replies: [],
      showReplies: false
    };
  
    this.postService.addReply(post.id, commentId, newReply).subscribe(
      createdReply => {
        comment.replies.push(createdReply);
        console.log('Novi odgovor:', createdReply);
      },
      error => {
        console.error('Greška prilikom dodavanja odgovora:', error);
      }
    );
  }

  addReply(post: Post, commentId: number, replyInput: string) {
    if (!replyInput.trim()) {
      return;
    }
  
    const currentUser = this.authService.getCurrentUser();
    if (!currentUser) {
      console.error('Nepoznat ulogovani korisnik.');
      return;
    }
  
    const comment = post.comments.find(c => c.id === commentId);
    if (!comment) {
      console.error('Nepoznat komentar.');
      return;
    }
  
    const newReply: Comment = {
      text: replyInput,
      user: currentUser,
      isEditing: false,
      isUpdating: false,
      updatedText: '',
      reactions: [],
      replies: [],
      showReplies: false
    };
  
    this.replyToComment(post, commentId, newReply.text);
    this.replyInput = '';
  }

  openReplies(comment: Comment) {
    if(comment) {
      comment.showReplies = !comment.showReplies;
      if (comment.showReplies) {
        this.loadRepliesForComment(comment);
        comment.showReplies = true;
      }
    }
  }
  
  loadRepliesForComment(comment: Comment) {
    if (comment) {
      this.postService.getRepliesForComment(comment.id).subscribe(
        replies => {
          comment.replies = replies;
        },
        error => {
          console.error('Greška prilikom preuzimanja komentara:', error);
        }
      );
    }
  }
  
}
