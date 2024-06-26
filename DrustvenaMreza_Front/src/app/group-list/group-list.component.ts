import { Component, OnInit } from '@angular/core';
import { Group } from '../model/group.model';
import { GroupService } from '../services/group.service';
import { DatePipe } from '@angular/common';

@Component({
  selector: 'app-group-list',
  templateUrl: './group-list.component.html',
  styleUrls: ['./group-list.component.css'],
  providers: [DatePipe]
})
export class GroupListComponent implements OnInit {
  groups: Group[] = [];
  group: Group | undefined;

  constructor(private groupService: GroupService) { }

  ngOnInit() {
    this.loadGroups();
  }

  loadGroups() {
    this.groupService.getAllGroups().subscribe(
      groups => {
        this.groups = groups;
        console.log(this.groups);
      },
      error => {
        console.error('Greška prilikom preuzimanja grupa:', error);
      }
    );
  }
  
  updateGroups(groups: Group[]) {
    this.groups = groups;
  }

  deleteGroup(groupId: number) {
    this.groupService.deleteGroup(groupId).subscribe(() => {
      this.loadGroups();
    });
  }

  addGroup(newGroup: Group): void {
    this.groups.push(newGroup);
    console.log('Dodavanje grupe:', newGroup);
  }

  showPopup: boolean = false;

  showEditPopup: boolean = false;

  openEditPopup(group: Group) {
    this.showEditPopup = true;
    this.group = group;
  }

  openPopup() {
    this.showPopup = true;
  }

  closeEditPopup() {
    this.showEditPopup = false;
  }
  
  closePopup() {
    this.showPopup = false;
  }

  searchTerm: string = '';
  
}
