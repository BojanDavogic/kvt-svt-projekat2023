import { Component, EventEmitter, Output } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { GroupService } from '../services/group.service';
import { Group } from '../model/group.model';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-search-group',
  templateUrl: './search-group.component.html',
  styleUrls: ['./search-group.component.css']
})
export class SearchGroupComponent {
  @Output() searchResults = new EventEmitter<Group[]>();
  searchForm: FormGroup;

  constructor(private searchService: GroupService, private fb: FormBuilder) {
    this.searchForm = this.fb.group({
      name: [''],
      description: [''],
      pdfContent: [''],
      postsMin: [''],
      postsMax: [''],
      avgLikesMin: [''],
      avgLikesMax: ['']
    });
  }

  onSearch() {
    const searchCriteria = this.searchForm.value;

    this.searchService.searchGroups(searchCriteria).subscribe(
      groups => {
        this.searchResults.emit(groups);
      },
      error => {
        console.error('Greška prilikom pretrage grupa:', error);
      }
    );
  }
}
