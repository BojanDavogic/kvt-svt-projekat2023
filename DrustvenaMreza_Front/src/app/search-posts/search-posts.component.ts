import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';
import { SearchService } from '../services/search.service';

@Component({
  selector: 'app-search-posts',
  templateUrl: './search-posts.component.html',
  styleUrls: ['./search-posts.component.css']
})
export class SearchPostsComponent {
  searchForm: FormGroup;
  results: any[] = [];

  constructor(private fb: FormBuilder, private searchService: SearchService) {
    this.searchForm = this.fb.group({
      title: [''],
      text: [''],
      pdfContent: [''],
      comments: [''],
      likesMin: [''],
      likesMax: ['']
    });
  }

  onSearch() {
    const searchParams = this.searchForm.value;
    this.searchService.searchPosts(searchParams).subscribe((data: any) => {
      this.results = data;
    });
  }
}
