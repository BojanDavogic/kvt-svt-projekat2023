import { Component, OnInit } from '@angular/core';
import { GroupService } from '../services/group.service';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'app-group-details',
  templateUrl: './group-details.component.html',
  styleUrls: ['./group-details.component.css']
})
export class GroupDetailsComponent implements OnInit {
  groupId: any;
  group: any;

  constructor(private route: ActivatedRoute, private groupService: GroupService) {
    this.route.params.subscribe(params => {
      this.groupId = +params['id']; // Pretvori vrednost u broj
    });
   }

  ngOnInit() {
    this.route.paramMap.subscribe(params => {
      this.groupId = params.get('id');
      this.getGroupDetails();
    });
  }

  getGroupDetails() {
    this.groupService.getGroupById(this.groupId).subscribe(data => {
      this.group = data;
    });
  }
}
