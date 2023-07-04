import { Component, OnInit } from '@angular/core';
import { GroupService } from '../services/group.service';
import { ActivatedRoute } from '@angular/router';
import { Post, Reaction, Comment } from '../model/post.model';
import { PostService } from '../services/post.service';
import { AuthService } from '../services/auth.service';
import { Group } from '../model/group.model';
import { User } from '../model/user.model';

@Component({
  selector: 'app-group-details',
  templateUrl: './group-details.component.html',
  styleUrls: ['./group-details.component.css']
})
export class GroupDetailsComponent implements OnInit {
  groupId: number | undefined;
  group: Group | undefined;

  posts: Post[] = [];
  comments: Comment[] = [];
  newPostContent: string = '';
  commentInput: string = '';
  currentUser!: User;
  selectedButton: string = '';
  buttonColor: string = 'whitesmoke';

  constructor(private route: ActivatedRoute, private groupService: GroupService, private postService: PostService, private authService: AuthService) {
    this.route.params.subscribe(params => {
      this.groupId = +params['id']; // Pretvori vrednost u broj
    });
   }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      const groupIdParam = params.get('id');
      this.groupId = groupIdParam ? parseInt(groupIdParam, 10) : undefined;
      this.getGroupDetails();
    });
  }

  getGroupDetails() {
    if (this.groupId !== undefined) {
      this.groupService.getGroupById(this.groupId).subscribe(data => {
        this.group = data;
      });
  }
  }

  
}
